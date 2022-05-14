package com.example.connectingus.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.R;

public class FirstActivity extends AppCompatActivity {
    Button agree;
    Intent intent;
    TextView terms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getSupportActionBar().hide();
        agree = findViewById(R.id.agree);
        terms = findViewById(R.id.terms);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(FirstActivity.this, NumberVerify.class);
                startActivity(intent);
                finish();
            }
        });

        String privacyTerms=getResources().getString(R.string.terms);

        SpannableString ss =new SpannableString(privacyTerms);

        ClickableSpan privacy =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Uri uri=Uri.parse("https://pages.flycricket.io/connectingus-0/privacy.html");
                Intent privacy=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(privacy);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };


        ClickableSpan termConditions =new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Uri uri=Uri.parse("https://pages.flycricket.io/connectingus-0/terms.html");
                Intent terms=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(terms);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

         ss.setSpan(privacy,60,74, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         ss.setSpan(termConditions,79,97, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        terms.setText(ss);
        terms.setMovementMethod(LinkMovementMethod.getInstance());
    }

}