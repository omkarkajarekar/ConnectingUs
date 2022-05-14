package com.example.connectingus.conversation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.connectingus.R;
import com.example.connectingus.adapters.MainAdapter;
import com.example.connectingus.adapters.StarredAdapter;
import com.example.connectingus.adapters.TempMsgAdapter;
import com.example.connectingus.models.StarredModel;
import com.example.connectingus.models.TempMsgModel;

import java.util.ArrayList;

public class StarredMessage extends AppCompatActivity {
RecyclerView recyclerView;
    ArrayList<StarredModel> starredMessages=new ArrayList<>();
    StarredAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Starred Messages");
        setContentView(R.layout.activity_starred_message);
        recyclerView=findViewById(R.id.starred_recycler_view);

        starredMessages=TempDetailChatView.getStarredMessages();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new StarredAdapter(this,starredMessages);
        recyclerView.setAdapter(adapter);


    }
}