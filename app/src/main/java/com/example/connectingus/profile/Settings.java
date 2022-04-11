package com.example.connectingus.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.ContactUs;
import com.example.connectingus.R;
import com.example.connectingus.SplashActivity;
import com.example.connectingus.authentication.FirstActivity;
import com.example.connectingus.authentication.ProfileEdit;
import com.example.connectingus.conversation.ConversationList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Settings extends AppCompatActivity {
    ConstraintLayout profile_edit;
    ConstraintLayout invite_friend;
    ConstraintLayout get_help;
    ConstraintLayout do_logout;
    ConstraintLayout get_account_deleted;
    ShapeableImageView profile_pic;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextView name;
    TextView about;
    String nameOfUser;
    ArrayList<String> userDetails=new ArrayList<>();
    ProgressDialog progressDialog;
    String userID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);  // back button
        getSupportActionBar().setTitle("Settings");
        profile_pic = findViewById(R.id.profile_pic);
        profile_edit = findViewById(R.id.profile_edit);
        get_help = findViewById(R.id.get_help);
        do_logout = findViewById(R.id.do_logout);
        get_account_deleted = findViewById(R.id.get_account_deleted);
        invite_friend = findViewById(R.id.invite_friend);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);

        firebaseAuth=FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(userID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                       userDetails.clear();
                       for(DataSnapshot Dsnapshot:snapshot.getChildren())
                       {
                           userDetails.add(Dsnapshot.getValue().toString());
                       }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileEdit.class);
                Pair pair[]= new Pair[3];
                pair[0] = new Pair(profile_pic,"imageTransition");
                pair[1] = new Pair(name,"nameTransition");
                pair[2] = new Pair(about,"aboutTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pair);
                }
                startActivity(intent,options.toBundle());
            }
        });
        invite_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sub="Download the ConnectingUs chat application \n https://play.google.com/store/apps/details?id=com.whatsapp";
                intent.putExtra(Intent.EXTRA_TEXT,sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });
        get_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(intent);
                finish();
            }
        });
        do_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle(userDetails.get(2)+" you want to logout ?");
                dialog.setMessage("Please confirm logout");
                dialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        Intent intent=new Intent(Settings.this, FirstActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(Settings.this,userDetails.get(2)+" you logged out successfully",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();

                    }
                });

                dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();


            }
        });
        get_account_deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Settings.this);
                dialog.setTitle(userDetails.get(2)+" are you sure ?");
                dialog.setMessage("If you delete this account it will result in completely removing your account from system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteAccount();
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();


            }
        });
    }

    private void deleteAccount() {
        //FirebaseAuth.getInstance().getCurrentUser().delete();

        progressDialog=new ProgressDialog(Settings.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("We are deleting your account");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("users")
                .child(userID).removeValue()
                //.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       firebaseAuth.getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(Settings.this, FirstActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();



                                        }
                                        else
                                        {
                                            Toast.makeText(Settings.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
    }
}