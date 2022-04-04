package com.example.connectingus.conversation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connectingus.R;
import com.example.connectingus.models.User;

public class ChatActivity extends AppCompatActivity {
    ImageView imageView;
    TextView personName,lastMessage;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.setTitle("ConnectingUS");
        imageView=findViewById(R.id.imageViewItem);
        personName=findViewById(R.id.personName);
        lastMessage=findViewById(R.id.lastMessage);

        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            user= (User) intent.getSerializableExtra("user");
            imageView.setImageResource(user.getImageId());
            personName.setText(user.getName());
            lastMessage.setText(user.lastMessage);
        }
    }
}