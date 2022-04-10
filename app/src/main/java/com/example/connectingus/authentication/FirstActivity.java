package com.example.connectingus.authentication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.connectingus.R;

public class FirstActivity extends AppCompatActivity {
    Button agree;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        agree = findViewById(R.id.agree);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(FirstActivity.this, NumberVerify.class);
                startActivity(intent);
                finish();
            }
        });
    }
}