package com.example.connectingus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    ConstraintLayout profile_edit;
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
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileEdit.class);
                Pair pair[]= new Pair[3];
                pair[0] = new Pair(profile_pic,"imageTransition");
                pair[1] = new Pair(name,"nameTransition");
                pair[2] = new Pair(about,"aboutTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pair);
                }
                startActivity(intent,options.toBundle());
                //startActivity(intent);
            }
        });

    }
}