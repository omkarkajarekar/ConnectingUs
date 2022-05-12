package com.example.connectingus.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.adapters.TempMsgAdapter;
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
    TempMsgAdapter tempMsgAdapter;
    ArrayList<TempMsgModel> tempMsgModels;
    public static ImageView delete_selected;
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
    String senderRoom;
    String receiverRoom;
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
        delete_selected = findViewById(R.id.delete_selected);
        delete_selected.setVisibility(View.INVISIBLE);

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



        tempMsgModels=new ArrayList<>();

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        delete_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Selected : "+TempMsgAdapter.positions.toString(),Toast.LENGTH_SHORT).show();
            }
        });

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
                        tempMsgAdapter=new TempMsgAdapter(tempMsgModels,TempDetailChatView.this);
                        recyclerView.setAdapter(tempMsgAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        //tempMsgAdapter.notifyDataSetChanged();
        super.onResume();
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_chat_menus,menu);
        MenuItem menuItem=menu.findItem(R.id.search_chat_action);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search accross the chat");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tempMsgAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();
        if(itemid==R.id.clear_chat){
            FirebaseDatabase.getInstance().getReference("Chats")
            .child(senderRoom).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {

                  }
              });
            tempMsgModels.clear();
            tempMsgAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}