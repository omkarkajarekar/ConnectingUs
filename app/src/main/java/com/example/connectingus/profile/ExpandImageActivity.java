package com.example.connectingus.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.connectingus.R;
import com.example.connectingus.authentication.ProfileEdit;
import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.fragments.ChatsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ExpandImageActivity extends AppCompatActivity {
    String calling_activity;
    ImageView profile_pic;
    FirebaseAuth firebaseAuth;
    String userID;
    StorageReference storageReference;
    File localFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_image);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profile_pic = findViewById(R.id.profile_pic);
        Intent intent = getIntent();
        firebaseAuth= FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
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
                profile_pic.setImageBitmap(bmImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        calling_activity = intent.getStringExtra("calling_activity");
        getSupportActionBar().setTitle(intent.getStringExtra("name"));

    }
    private Intent getParentActivityIntentImplement() {
        Intent i = null;
        if(calling_activity.equals("ConversationList")) {
            i = new Intent(getApplicationContext(), ConversationList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if(calling_activity.equals("ProfileEdit")){
            i = new Intent(getApplicationContext(), ProfileEdit.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if(calling_activity.equals("CurrentProfile")){
            i = new Intent(getApplicationContext(), CurrentProfile.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return i;
    }
    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        return getParentActivityIntentImplement();
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(calling_activity.equals("ConversationList"))
            startActivity(new Intent(getApplicationContext(), ConversationList.class));
        else if(calling_activity.equals("ProfileEdit"))
            startActivity(new Intent(getApplicationContext(), ProfileEdit.class));
        else if(calling_activity.equals("CurrentProfile"))
            startActivity(new Intent(getApplicationContext(), CurrentProfile.class));
        //finish();
    }
}