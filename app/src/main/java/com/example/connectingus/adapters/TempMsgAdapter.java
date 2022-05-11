package com.example.connectingus.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.models.TempMsgModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class TempMsgAdapter extends RecyclerView.Adapter
{
    ArrayList<TempMsgModel> tempMsgModels;
    Context context;
    static int flag = 0;
    public static HashSet<Integer> positions = new HashSet<>();
    public TempMsgAdapter(){}
    public TempMsgAdapter(ArrayList<TempMsgModel> tempMsgModels, Context context) {
        this.tempMsgModels = tempMsgModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
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
    public int getItemViewType(int position) {

        if(tempMsgModels.get(position).getId()==1)
        {
            return 1;
        }
        else
        {
            return 2;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TempMsgModel tempMsgModel=tempMsgModels.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tempMsgModel.setSelected(!tempMsgModel.isSelected());
                if(tempMsgModel.isSelected()) {
                    holder.itemView.setBackgroundResource(R.color.blue_background);
                    flag++;
                    positions.add(position);
                    TempDetailChatView.delete_selected.setVisibility(View.VISIBLE);
                }
                else {
                    holder.itemView.setBackgroundResource(R.color.chat_background);
                    flag--;
                    positions.remove(position);
                    //int i = positions.indexOf(position);
                    if(flag==0)
                        TempDetailChatView.delete_selected.setVisibility(View.INVISIBLE);
                }
                return false;
            }

        });
        if(holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMsg.setText(tempMsgModel.getMessage());
            Date date=new Date(tempMsgModel.getTimestamp());
            SimpleDateFormat formatTime=new SimpleDateFormat("hh:mm a");
            String time=formatTime.format(date);
            ((SenderViewHolder)holder).senderTime.setText(time);
        }
        else
        {
            ((ReceiverViewHolder)holder).receiverMsg.setText(tempMsgModel.getMessage());
            Date date=new Date(tempMsgModel.getTimestamp());
            SimpleDateFormat formatTime=new SimpleDateFormat("hh:mm a");
            String time=formatTime.format(date);
            ((ReceiverViewHolder)holder).receiverTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return tempMsgModels.size();
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

    public  class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg;
        TextView senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.textSent);
            senderTime = itemView.findViewById(R.id.textSentTime);
        }
    }
}
