package com.example.connectingus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.models.MessagesModel;

import java.util.ArrayList;

public class DetailChatAdapter extends  RecyclerView.Adapter {

    ArrayList <MessagesModel> messagesModels;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public DetailChatAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        /*if(messagesModels.get(position).getId().equals(Fireba))
        {

        }*/
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return  new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        

    }

    @Override
    public int getItemCount() {

        return messagesModels.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg;
        TextView receiverTime;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg=itemView.findViewById(R.id.textReceived);
            receiverTime=itemView.findViewById(R.id.textReceivedTime);
        }
    }

    public  class SenderViewHolder extends RecyclerView.ViewHolder  {

        TextView senderMsg;
        TextView senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.textSent);
            senderTime=itemView.findViewById(R.id.textSentTime);
        }
    }
}
