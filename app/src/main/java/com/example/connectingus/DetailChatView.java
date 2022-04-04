package com.example.connectingus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.fragments.ChatsFragment;
import com.example.connectingus.models.User;

public class DetailChatView extends AppCompatActivity {
    TextView uName;
    User user;
    ImageView ivBack;
    ImageView ivSend;
    ImageView ivMenu;
    ImageView ivProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailchatview);
        getSupportActionBar().hide();

        ivBack=findViewById(R.id.img_back);
        ivProf=findViewById(R.id.img_prof_pic);
        uName=findViewById(R.id.tv_uname);
        ivMenu=findViewById(R.id.img_menu);
        ivSend=findViewById(R.id.sendButton);
        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        {
            user= (User) intent.getSerializableExtra("user");
            ivProf.setImageResource(user.getImageId());
            uName.setText(user.getName());
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), ConversationList.class));
                finish();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(DetailChatView.this, "Message SENT", Toast.LENGTH_SHORT).show();
            }
        });


        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(DetailChatView.this, "Clicked on MENU", Toast.LENGTH_SHORT).show();
            }
        });
    }
}