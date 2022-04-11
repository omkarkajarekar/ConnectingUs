package com.example.connectingus.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connectingus.R;
import com.example.connectingus.models.User;


public class ChatProfile extends AppCompatActivity {

    ImageView chatProf;
    ImageView chatBack;
    TextView chatName;
    TextView chatAbout;
    TextView chatPhone;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);
        getSupportActionBar().hide();

        chatProf=findViewById(R.id.iv_chatProfile);
        chatBack=findViewById(R.id.chatprof_back);
        chatName=findViewById(R.id.chatProfile_name);

        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            user= (User) intent.getSerializableExtra("UserDetails");
            chatProf.setImageResource(user.getImageId());
            chatName.setText(user.getName());
        }

    }
}