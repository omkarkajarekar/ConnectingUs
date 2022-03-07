package com.example.connectingus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    Button signout;
    TextView welcome;
    String user_number;
    Intent intent;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        signout = findViewById(R.id.signout);
        welcome = findViewById(R.id.welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        user_number = firebaseAuth.getCurrentUser().getPhoneNumber();
        welcome.setText(" Welcome user : "+user_number);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}