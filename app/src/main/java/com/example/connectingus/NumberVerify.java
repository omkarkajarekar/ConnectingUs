package com.example.connectingus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NumberVerify extends AppCompatActivity {
    ConstraintLayout layout;
    Button next;
    String number;
    TextInputLayout ph_label;
    TextInputEditText phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        next = findViewById(R.id.next);
        phone_number = findViewById(R.id.phone_number);
        ph_label = findViewById(R.id.ph_label);
        layout = findViewById(R.id.layout);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = phone_number.getText().toString();
                if(number.length()==10){
                    Intent intent = new Intent(NumberVerify.this, OTPVerify.class);
                    intent.putExtra("number", number);
                    startActivity(intent);
                }
                else{
                    ph_label.setError("Please Enter Valid Phone Number");
                }
            }
        });
    }
}
