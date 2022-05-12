package com.example.connectingus.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.animation.MyBounceInterpolator;
import com.example.connectingus.authentication.ProfileEdit;
import com.example.connectingus.support.CreateFolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class CurrentProfile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView name,about,phone;
    ConstraintLayout layoutname,layoutabout;
    String strname,strabout,strphone;
    StorageReference storageReference;
    String userID;
    File localFile;
    Uri selectedImage;
    ShapeableImageView profile_pic,image_selector;
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
            new CreateFolder().createFolderForProfile(CurrentProfile.this,firebaseAuth.getUid(),bitmap,CreateFolder.MY_PHOTO);

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
        setContentView(R.layout.activity_current_profile);
        this.setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name=findViewById(R.id.currentusername);
        about=findViewById(R.id.currentuserabout);
        phone=findViewById(R.id.currentuserphone);
        layoutname=findViewById(R.id.name_label);
        layoutabout=findViewById(R.id.about_label);
        profile_pic = findViewById(R.id.profile_image);
        image_selector = findViewById(R.id.image_selector);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = firebaseAuth.getCurrentUser().getUid();
        profile_pic.setImageDrawable(new CreateFolder().getLocalImage(firebaseAuth.getUid(),CreateFolder.MY_PHOTO));

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
                intent.putExtra("calling_activity","CurrentProfile");
                Pair pair = new Pair(profile_pic,"imageTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(CurrentProfile.this, pair);
                }
                startActivity(intent,options.toBundle());
                finish();
            }
        });
        //String uabout;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strname=snapshot.child("name").getValue().toString();
                strabout=snapshot.child("about").getValue().toString();
                strphone=snapshot.child("phone").getValue().toString();
                name.setText(strname);
                about.setText(strabout);
                phone.setText(strphone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(getLocalClassName(),error.getMessage());
            }
        });

        layoutname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CurrentProfile.this);
                final View customLayout=getLayoutInflater().inflate(R.layout.dialog_name,null);
                builder.setView(customLayout);
                builder.setTitle("Enter your name");
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText=customLayout.findViewById(R.id.editname);
                        String uname=editText.getText().toString().trim();
                        if(uname.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Name can't be empty",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            updatename(firebaseUser.getUid(),uname);
                            name.setText(uname);
                        }
                    }
                });

                builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        layoutabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), About.class);
                String previousabout=about.getText().toString();
                intent.putExtra("about",previousabout);
                startActivity(intent);
            }
        });
    }

    private boolean updatename(String uid,String name)
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(uid).child("name");
        databaseReference.setValue(name);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        profile_pic.setImageDrawable(new CreateFolder().getLocalImage(firebaseAuth.getUid(),CreateFolder.MY_PHOTO));
    }
}