package com.example.connectingus.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.ContactUs;
import com.example.connectingus.R;
import com.example.connectingus.authentication.FirstActivity;
import com.example.connectingus.support.CreateFolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Settings extends AppCompatActivity {
    ConstraintLayout profile_edit;
    ConstraintLayout invite_friend;
    ConstraintLayout set_wallpaper;
    ConstraintLayout get_help;
    ConstraintLayout do_logout;
    ConstraintLayout get_account_deleted;
    ShapeableImageView profile_pic;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView name;
    TextView about;
    String nameOfUser;
    ArrayList<String> userDetails=new ArrayList<>();
    ProgressDialog progressDialog;
    String userID;
    Uri selectedImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    File localFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && data!= null){
            selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                Log.d(getLocalClassName(),e.getMessage());
            }
            new CreateFolder().saveWallpaper(Settings.this,bitmap);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  // back button
        getSupportActionBar().setTitle("Settings");
        profile_pic = findViewById(R.id.profile_pic);
        profile_edit = findViewById(R.id.profile_edit);
        get_help = findViewById(R.id.get_help);
        set_wallpaper = findViewById(R.id.set_wallpaper);
        do_logout = findViewById(R.id.do_logout);
        get_account_deleted = findViewById(R.id.get_account_deleted);
        invite_friend = findViewById(R.id.invite_friend);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);

        firebaseAuth=FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(userID);
        profile_pic.setImageDrawable(new CreateFolder().getLocalImage(firebaseAuth.getUid(),CreateFolder.MY_PHOTO));
        set_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle(" Do you want to set or remove Background?");
                dialog.setMessage("Please confirm choice");
                dialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(gallery,3);
                    }
                });
                dialog.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new CreateFolder().removeWallpaper(Settings.this);
                    }
                });
                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails.clear();
               Log.d("enpp", "at line no 111");
               for(DataSnapshot Dsnapshot:snapshot.getChildren())
               {
                   userDetails.add(Dsnapshot.getValue().toString());

                   String strname=snapshot.child("name").getValue().toString();
                   String strabout=snapshot.child("about").getValue().toString();
                   name.setText(strname);
                   about.setText(strabout);
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CurrentProfile.class);
                Pair pair = new Pair(profile_pic,"imageTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pair);
                }
                startActivity(intent,options.toBundle());

            }
        });
        invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sub="Download the ConnectingUs chat application \n https://github.com/omkarkajarekar/ConnectingUs/releases/download/ConnectingUs/ConnectingUS.apk";
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });
        get_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle("Select Help From Following : ");
                dialog.setMessage("Please confirm choice");
                dialog.setPositiveButton("Feedback", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Privacy Policy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri=Uri.parse("https://pages.flycricket.io/connectingus-0/privacy.html");
                        Intent privacy=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(privacy);
                    }
                });
                dialog.setNeutralButton("Terms and Conditions", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri=Uri.parse("https://pages.flycricket.io/connectingus-0/terms.html");
                        Intent terms=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(terms);
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();

                //finish();
            }
        });
        do_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle(userDetails.get(2)+" you want to logout ?");
                dialog.setMessage("Please confirm logout");
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        Intent intent=new Intent(Settings.this, FirstActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(Settings.this,userDetails.get(2)+" you logged out successfully",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();


            }
        });
        get_account_deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle(userDetails.get(2)+" are you sure ?");
                dialog.setMessage("If you delete this account it will result in completely removing your account from system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteAccount();
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();


            }
        });
    }

    private void deleteAccount() {
        //FirebaseAuth.getInstance().getCurrentUser().delete();

        progressDialog=new ProgressDialog(Settings.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("We are deleting your account");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.d("enpp", "after progress bar shown"+userID);
        FirebaseDatabase.getInstance().getReference("users")
                .child(userID).removeValue()
                //.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("enpp", "line no 247");
                       firebaseAuth.getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("enpp", "line no 252");
                                        if (task.isSuccessful())
                                        {
                                            Log.d("enpp", "line no 255");
                                            progressDialog.dismiss();
                                            Log.d("enpp", "after progress bar dismissed");
                                            Intent intent=new Intent(Settings.this, FirstActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            Log.d("enpp", "line no 262");
                                            startActivity(intent);
                                            finish();



                                        }
                                        else
                                        {
                                            Log.d(getLocalClassName(),task.getException().getMessage());
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        profile_pic.setImageDrawable(new CreateFolder().getLocalImage(firebaseAuth.getUid(),CreateFolder.MY_PHOTO));
    }
}