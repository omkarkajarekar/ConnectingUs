package com.example.connectingus.conversation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.adapters.TempMsgAdapter;
import com.example.connectingus.fragments.ChatsFragment;
import com.example.connectingus.fragments.ShareIds;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.TempMsgModel;
import com.example.connectingus.models.User;
import com.example.connectingus.profile.ChatProfile;
import com.example.connectingus.support.CreateFolder;

import java.util.ArrayList;

public class TempDetailChatView extends AppCompatActivity {

    String name,userid,phone;
    byte[] byteArray;
    ImageView ivProf;
    ImageButton ibSend;
    ImageButton ibRecv;
    TextView tvUname;
    EditText etM;
    ContactModel contactModel;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    String userId="";   //BWeN36Tlv3PeshBphBdkhafBhL73//vIHAf4DYO2Vu8pbikfpMKv1yNp82//wgX0rZSyWlOhoSgw0EShyUCS1YL2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailchatview);
        ibSend=findViewById(R.id.sendButton);
        ibRecv=findViewById(R.id.receiveButton);

        ibSend.setVisibility(View.INVISIBLE);
        ibRecv.setVisibility(View.INVISIBLE);

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

        Intent intent=getIntent();
        //Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");
        //
        /*byteArray = intent.getByteArrayExtra("imageByte");
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ivProf.setImageBitmap(image);
        }
        catch (Exception e){
            ivProf.setImageResource(R.drawable.ic_baseline_person_24);
        }
        name = intent.getStringExtra("name");
        tvUname.setText(name);
        userId=intent.getStringExtra("userID");
        phone = intent.getStringExtra("phone");*/

        if(intent.getExtras()!=null)
        {
            contactModel= (ContactModel) intent.getSerializableExtra("UserDetails");
            //ivProf.setImageBitmap(contactModel.getImage());
            //ivProf.setImageResource(contactModel.getImageId());
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
                    ibSend.setVisibility(View.VISIBLE);
                    ibRecv.setVisibility(View.VISIBLE);
                }
                else
                {
                    ibSend.setVisibility(View.INVISIBLE);
                    ibRecv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final ArrayList<TempMsgModel> tempMsgModels=new ArrayList<>();
        final TempMsgAdapter tempMsgAdapter=new TempMsgAdapter(tempMsgModels,this);
        recyclerView.setAdapter(tempMsgAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=etM.getText().toString().trim();
                TempMsgModel tempMsgModel=new TempMsgModel(msg,1);
                tempMsgModels.add(tempMsgModel);
                tempMsgAdapter.notifyDataSetChanged();
                etM.setText("");
                ShareIds.getInstance().setUserId(contactModel);
            }
        });

        ibRecv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=etM.getText().toString().trim();
                TempMsgModel tempMsgModel=new TempMsgModel(msg,2);
                tempMsgModels.add(tempMsgModel);
                tempMsgAdapter.notifyDataSetChanged();
                etM.setText("");
            }
        });


        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentChatProf = new Intent(getApplicationContext(), ChatProfile.class);
                intentChatProf.putExtra("UserDetails", contactModel);
                /*intentChatProf.putExtra("userID",userId);
                intentChatProf.putExtra("name",name);
                intentChatProf.putExtra("phone",phone);
                intentChatProf.putExtra("imageByte",byteArray);*/
                intentChatProf.putExtra("calling_activity","TempDetailChatView");
                startActivity(intentChatProf);
                finish();
            }
        });
    }


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_chat_menus,menu);
        return super.onCreateOptionsMenu(menu);
    }

}