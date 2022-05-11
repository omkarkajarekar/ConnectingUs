package com.example.connectingus.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.adapters.TempMsgAdapter;
import com.example.connectingus.models.MessagesModel;
import com.example.connectingus.models.ShareIds;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.TempMsgModel;
import com.example.connectingus.profile.ChatProfile;
import com.example.connectingus.support.CreateFolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class TempDetailChatView extends AppCompatActivity {

    String name,userid,phone;
    byte[] byteArray;
    ImageView ivProf;
    //ImageButton ibSend;
    Button ibSend;
    TextView tvUname;
    EditText etM;
    ContactModel contactModel;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String userId="";   //BWeN36Tlv3PeshBphBdkhafBhL73//vIHAf4DYO2Vu8pbikfpMKv1yNp82//wgX0rZSyWlOhoSgw0EShyUCS1YL2
    String senderID;
    String receiverID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailchatview);
        ibSend=findViewById(R.id.sendButton);

        //ibSend.setVisibility(View.INVISIBLE);
        ibSend.setEnabled(false);

        recyclerView=findViewById(R.id.recyclerview1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // back button

        getSupportActionBar().setDisplayShowTitleEnabled(false);  // hide Title

        getSupportActionBar().setDisplayShowCustomEnabled(true); // Custom Appbar
        LayoutInflater layoutInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_appbar,null);
        getSupportActionBar().setCustomView(view);

        relativeLayout=findViewById(R.id.reltvlyout);
        ivProf=findViewById(R.id.iv_prof_pic);
        tvUname=findViewById(R.id.tv_uname);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        senderID = firebaseAuth.getUid();

        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            contactModel= (ContactModel) intent.getSerializableExtra("UserDetails");
            receiverID = contactModel.getUserId();
            ivProf.setImageDrawable(new CreateFolder().getLocalImage(contactModel.getUserId(),CreateFolder.PROFILE_PHOTO));
            tvUname.setText(contactModel.getName());
            userId=contactModel.getUserId();
        }

        etM=findViewById(R.id.edMsg);

        etM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str=etM.getText().toString().trim();
                if(!str.isEmpty())
                {
                    ibSend.setEnabled(true);
                    //ibSend.setVisibility(View.VISIBLE);
                }
                else
                {
                    ibSend.setEnabled(false);
                    //ibSend.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final ArrayList<TempMsgModel> tempMsgModels=new ArrayList<>();
        final TempMsgAdapter tempMsgAdapter=new TempMsgAdapter(tempMsgModels,this);
        final String senderRoom = senderID + receiverID;
        final String receiverRoom = receiverID + senderID;

        recyclerView.setAdapter(tempMsgAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        databaseReference.child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tempMsgModels.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){

                            TempMsgModel model = snapshot1.getValue(TempMsgModel.class);
                            tempMsgModels.add(model);
                        }
                        tempMsgAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=etM.getText().toString().trim();
                //TempMsgModel tempMsgModel=new TempMsgModel(msg,1);
                //tempMsgModels.add(tempMsgModel);
                //tempMsgAdapter.notifyDataSetChanged();

                TempMsgModel model = new TempMsgModel(senderID,msg);
                model.setId(1);
                model.setTimestamp(new Date().getTime());
                etM.setText("");

                databaseReference.child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                model.setId(0);
                                databaseReference.child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
                ShareIds.getInstance().setUserId(contactModel);
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChatProf = new Intent(getApplicationContext(), ChatProfile.class);
                intentChatProf.putExtra("UserDetails", contactModel);
                intentChatProf.putExtra("calling_activity","TempDetailChatView");
                startActivity(intentChatProf);
                finish();
            }
        });
    }


    /*//Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_chat_menus,menu);
        return super.onCreateOptionsMenu(menu);
    }*/
}