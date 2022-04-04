package com.example.connectingus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.connectingus.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OTPVerify extends AppCompatActivity {
    static Context context;
    static String number;
    ConstraintLayout layout;
    Button resend_sms,wrong_number;
    TextView timer;
    TextView ph_number;
    String verificationID;
    Button btn_continue;
    String OTP;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_otp);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context = getApplicationContext();
        timer = findViewById(R.id.timer);
        resend_sms = findViewById(R.id.resend_sms);
        wrong_number = findViewById(R.id.wrong_number);
        layout = findViewById(R.id.layout);
        ph_number = findViewById(R.id.ph_number);
        linear = findViewById(R.id.linear);
        btn_continue = findViewById(R.id.btn_continue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            number = extras.getString("number");
            verificationID = extras.getString("verificationID");
        }
        ph_number.setText("Verify +91"+number);
        setTimer();
        resend_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend_sms.setEnabled(false);
                new NumberVerify().verifyPhoneNumber("+91"+number);
                setTimer();
            }
        });
        wrong_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,NumberVerify.class);
                startActivity(intent);
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTP = linear.getText().toString();
                if(OTP.length()==6){
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(NumberVerify.verificationID, OTP);
                        authenticateUser(credential);
                    }
                    catch (Exception e){
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(context,"Enter Valid OTP",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void setTimer(){
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("00 : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("00 : 00");
                resend_sms.setEnabled(true);
            }
        }.start();
    }
    public void authenticateUser(PhoneAuthCredential credential){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ProfileEdit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
