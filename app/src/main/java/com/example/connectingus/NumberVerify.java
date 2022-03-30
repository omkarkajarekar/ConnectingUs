package com.example.connectingus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class NumberVerify extends AppCompatActivity {
    static Context context;
    ConstraintLayout layout;
    Button next;
    String number;
    TextInputLayout ph_label;
    TextInputEditText phone_number;
    FirebaseAuth  firebaseAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;
    public static String verificationID;
    static Bundle mystate;
    static final String KEY_VERIFICATION_ID = "key_verification_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mystate = savedInstanceState;
        setContentView(R.layout.verify_phone);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        context = getApplicationContext();
        next = findViewById(R.id.next);
        phone_number = findViewById(R.id.phone_number);
        ph_label = findViewById(R.id.ph_label);
        layout = findViewById(R.id.layout);
        firebaseAuth = FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = phone_number.getText().toString();
                if(number.length()==10){
                    verifyPhoneNumber("+91"+number);
                    Toast.makeText(getApplicationContext(),"Sent For Verification",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NumberVerify.this, OTPVerify.class);
                    intent.putExtra("number", number);
                    intent.putExtra("verificationID", verificationID);
                    startActivity(intent);
                    finish();
                }
                else{
                    ph_label.setError("Please Enter Valid Phone Number");
                }
            }
        });


    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID,verificationID);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationID = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }
    public void verifyPhoneNumber(String phone_number){
        firebaseAuth = FirebaseAuth.getInstance();
        setCallbacks();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setActivity(this)
                .setPhoneNumber(phone_number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void setCallbacks(){
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                new OTPVerify().authenticateUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = s;
                if (verificationID == null && mystate != null) {
                    onRestoreInstanceState(mystate);
                }
                token = forceResendingToken;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }
    /*public void authenticateUser(PhoneAuthCredential credential){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }*/
}
