package com.example.connectingus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ContactUs extends AppCompatActivity {
    Button next;
    TextInputLayout subject_layout;
    TextInputLayout help_layout;
    TextInputEditText subject;
    TextInputEditText help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setTitle("Help");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        subject_layout = findViewById(R.id.subject_layout);
        help_layout = findViewById(R.id.help_label);
        next = findViewById(R.id.next);
        subject = findViewById(R.id.subject);
        help = findViewById(R.id.help);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_subject = "";
                String email_context = "";
                if(subject.getText().toString().equals(""))
                    subject_layout.setError("Please enter Subject");
                else
                    email_subject = subject.getText().toString().trim();

                if(subject.getText().toString().equals(""))
                    subject_layout.setError("Please enter Query");
                else
                    email_context = help.getText().toString().trim();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"pdat.pbl.group15@email.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
                intent.putExtra(Intent.EXTRA_TEXT, email_context);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }
}