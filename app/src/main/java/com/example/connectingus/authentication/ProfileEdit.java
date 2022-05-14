package com.example.connectingus.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.SplashActivity;
import com.example.connectingus.animation.MyBounceInterpolator;
import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.models.Users;
import com.example.connectingus.profile.ExpandImageActivity;
import com.example.connectingus.support.CreateFolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;


public class ProfileEdit extends AppCompatActivity {
    Button next;
    TextInputEditText name;
    TextInputEditText about;
    TextInputEditText phone;
    String userID,deviceID;
    String user_number,user_name,user_about,user_profile_pic;
    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri selectedImage;
    File localFile;
    ShapeableImageView profile_pic,image_selector;
    static boolean flag = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && data!= null){
            selectedImage = data.getData();
            profile_pic.setImageURI(selectedImage);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                Log.d(getLocalClassName(),e.getMessage());
            }
            new CreateFolder().createFolderForProfile(ProfileEdit.this,firebaseAuth.getUid(),bitmap,CreateFolder.MY_PHOTO);
           //user_profile_pic = selectedImage.getPath();
            uploadImageToFireStorage();
        }
    }
    public  void uploadImageToFireStorage(){
        StorageReference fileref = storageReference.child(userID).child("profile.jpg");
        fileref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Profile Picture Updated",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Profile Picture Updation Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        next = findViewById(R.id.next);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        phone = findViewById(R.id.phone);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_number = firebaseAuth.getCurrentUser().getPhoneNumber();
        phone.setText(user_number);
        deviceID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        userID = firebaseAuth.getCurrentUser().getUid();
        profile_pic = findViewById(R.id.profile_image);

        image_selector = findViewById(R.id.image_selector);
        StorageReference pathReference = storageReference.child(userID).child("profile.jpg");

        try {
            localFile = File.createTempFile("profile", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmImg = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                new CreateFolder().createFolderForProfile(getApplicationContext(),firebaseAuth.getUid(),bmImg,CreateFolder.MY_PHOTO);
                profile_pic.setImageBitmap(bmImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        image_selector.startAnimation(myAnim);

        image_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,3);
            }
        });

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpandImageActivity.class);
                intent.putExtra("name","Profile Photo");
                intent.putExtra("calling_activity","ProfileEdit");
                Pair pair = new Pair(profile_pic,"imageTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(ProfileEdit.this, pair);
                }
                startActivity(intent,options.toBundle());
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flag=true;

                user_name = name.getText().toString().trim();
                user_about = about.getText().toString().trim();
                if(profile_pic.getDrawable() == null) {
                    Toast.makeText(ProfileEdit.this, "Please Select Profile Photo", Toast.LENGTH_LONG).show();
                    flag = false;
                }
                if(name.getText().toString().isEmpty() && about.getText().toString().isEmpty()){
                    name.setError("Please fill name");
                    about.setError("Please fill about");
                    flag = false;
                }
                else if(name.getText().toString().isEmpty()) {
                    name.setError("Please fill name");
                    flag = false;
                }
                else if(about.getText().toString().isEmpty()) {
                    about.setError("Please fill about");
                    flag = false;
                }
                else{
                    Users users = new Users(user_number,userID,user_name,user_about,deviceID,user_profile_pic);
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("users").child(userID);
                    databaseReference.setValue(users);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            flag = true;
                            Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Failed to add data",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                Intent intent=new Intent(getApplicationContext(), ConversationList.class);
                if(flag == true) {
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}