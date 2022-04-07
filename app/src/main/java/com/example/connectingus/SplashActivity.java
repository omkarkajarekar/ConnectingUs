package com.example.connectingus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.connectingus.authentication.FirstActivity;
import com.example.connectingus.conversation.ConversationList;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN=1000;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getApplicationContext(), ConversationList.class);
                    //Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                    //Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }
}