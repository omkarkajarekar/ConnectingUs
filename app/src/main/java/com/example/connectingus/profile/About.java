package com.example.connectingus.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.models.Aboutmsg;
import com.example.connectingus.R;
import com.example.connectingus.adapters.SingleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class About extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Aboutmsg> aboutmsgs=new ArrayList<>();
    private SingleAdapter adapter;
    TextView updatedabout;
    LinearLayout linearlayoutupdate;
    Button button;
    boolean flag;
    String uabout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        this.setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String previousabout=intent.getStringExtra("about");
        updatedabout=findViewById(R.id.updatedabout);
        updatedabout.setText(previousabout);
        linearlayoutupdate=findViewById(R.id.linearlayoutupdate);

        button=findViewById(R.id.showitem);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        adapter=new SingleAdapter(this,aboutmsgs);
        recyclerView.setAdapter(adapter);
        flag=false;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        CreateList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelected()!=null)
                {
                    updatedabout.setText(adapter.getSelected().getMsg());
                    uabout=updatedabout.getText().toString();
                    updateabout(firebaseUser.getUid(),uabout);
                }
                else
                    Toast.makeText(getApplicationContext(),"No Selection",Toast.LENGTH_LONG).show();
            }
        });

        linearlayoutupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(About.this);
                final View customLayout=getLayoutInflater().inflate(R.layout.dialog_name,null);
                builder.setView(customLayout);
                builder.setTitle("Add About");
                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText=customLayout.findViewById(R.id.editname);
                        uabout=editText.getText().toString().trim();
                        if(uabout.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"About can't be empty",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Aboutmsg msg=new Aboutmsg();
                            msg.setMsg(uabout);
                            int j;
                            for(j=0;j<aboutmsgs.size();j++)
                            {
                                if(uabout.equalsIgnoreCase(aboutmsgs.get(j).getMsg()))
                                {
                                    flag=true;
                                    break;
                                }
                            }
                            if(flag)
                            {
                                flag=false;
                            }
                            else
                            {
                                aboutmsgs.add(0,msg);
                                adapter.setAboutmsgs(aboutmsgs);
                                j=0;
                            }
                            Toast.makeText(getApplicationContext(),"Please click about in the list to confirm and then update!",Toast.LENGTH_LONG).show();
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
    }

    private boolean updateabout(String uid,String uabout)
    {
        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(uid).child("about");
        databaseReference.setValue(uabout);
        return true;
    }

    private void CreateList() {
        aboutmsgs=new ArrayList<>();
        String strmsg[]={"Glory to God!","Can't talk, ConnectingUs only","At work","Available","Busy","it's me!","Neutral","Everything happens for a reason!","Happy","Not alaways available, try your luck!"};
        for(int i=0;i<10;i++)
        {
            Aboutmsg msg=new Aboutmsg();
            msg.setMsg(strmsg[i]);
            aboutmsgs.add(msg);
        }
        adapter.setAboutmsgs(aboutmsgs);
    }
}