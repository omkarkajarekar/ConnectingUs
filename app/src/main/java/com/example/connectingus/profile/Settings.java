package com.example.connectingus.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.connectingus.ContactUs;
import com.example.connectingus.R;
import com.example.connectingus.authentication.ProfileEdit;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    ConstraintLayout profile_edit;
    ConstraintLayout invite_friend;
    ConstraintLayout get_help;
    ConstraintLayout do_logout;
    ConstraintLayout get_account_deleted;
    ShapeableImageView profile_pic;
    TextView name;
    TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  // back button
        getSupportActionBar().setTitle("Settings");
        profile_pic = findViewById(R.id.profile_pic);
        profile_edit = findViewById(R.id.profile_edit);
        get_help = findViewById(R.id.get_help);
        do_logout = findViewById(R.id.do_logout);
        get_account_deleted = findViewById(R.id.get_account_deleted);
        invite_friend = findViewById(R.id.invite_friend);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                Pair pair[]= new Pair[3];
                pair[0] = new Pair(profile_pic,"imageTransition");
                pair[1] = new Pair(name,"nameTransition");
                pair[2] = new Pair(about,"aboutTransition");
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
                String sub="Download the connectingus chat application \n https://play.google.com/store/apps/details?id=com.whatsapp";
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });
        get_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(intent);
                finish();
            }
        });
        do_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        get_account_deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}