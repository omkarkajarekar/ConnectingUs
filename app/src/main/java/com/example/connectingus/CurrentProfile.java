package com.example.connectingus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentProfile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView name,about,phone;
    ConstraintLayout layoutname,layoutabout;
    String strname,strabout,strphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_profile);
        this.setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name=findViewById(R.id.currentusername);
        about=findViewById(R.id.currentuserabout);
        phone=findViewById(R.id.currentuserphone);
        layoutname=findViewById(R.id.name_label);
        layoutabout=findViewById(R.id.about_label);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        //String uabout;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strname=snapshot.child("name").getValue().toString();
                strabout=snapshot.child("about").getValue().toString();
                strphone=snapshot.child("phone").getValue().toString();
                name.setText(strname);
                about.setText(strabout);
                phone.setText(strphone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        layoutname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CurrentProfile.this);
                final View customLayout=getLayoutInflater().inflate(R.layout.dialog_name,null);
                builder.setView(customLayout);
                builder.setTitle("Enter your name");
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText=customLayout.findViewById(R.id.editname);
                        String uname=editText.getText().toString().trim();
                        if(uname.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Name can't be empty",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            updatename(firebaseUser.getUid(),uname);
                            name.setText(uname);
                        }
                    }
                });

                builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });

        layoutabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),About.class);
                String previousabout=about.getText().toString();
                intent.putExtra("about",previousabout);
                startActivity(intent);
            }
        });
    }

    private boolean updatename(String uid,String name)
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(uid).child("name");
        databaseReference.setValue(name);
        return true;
    }
}