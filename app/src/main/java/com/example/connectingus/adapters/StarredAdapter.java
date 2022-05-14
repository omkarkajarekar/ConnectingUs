package com.example.connectingus.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.R;
import com.example.connectingus.models.StarredModel;
import com.example.connectingus.support.CreateFolder;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StarredAdapter extends RecyclerView.Adapter<StarredAdapter.ViewHolder>{
    ArrayList<StarredModel> starredMessages;
    Activity activity;
    public StarredAdapter(Activity ac,ArrayList<StarredModel> list)
    {
        this.starredMessages=list;
        this.activity=ac;
    }



    public StarredAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.starred_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StarredAdapter.ViewHolder holder, int position) {
        StarredModel model=starredMessages.get(position);
        holder.message.setText(model.getMsg());
        Date date=new Date(model.getTimestamp());
        SimpleDateFormat formatTime=new SimpleDateFormat("hh:mm a");
        String time=formatTime.format(date);
        holder.time.setText(time);
        SimpleDateFormat formatDate=new SimpleDateFormat("dd-MMM-yyyy");
        String dt=formatDate.format(date);
        holder.date.setText(dt);
        holder.user.setText(model.getUser());
        holder.ivImage.setImageDrawable(new CreateFolder().getLocalImage(model.getUserId(),CreateFolder.PROFILE_PHOTO));

    }

    @Override
    public int getItemCount() {
        return starredMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,time,date,user;
        ShapeableImageView ivImage;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            ivImage=itemView.findViewById(R.id.user_image);
            message=itemView.findViewById(R.id.msg);
            time=itemView.findViewById(R.id.msgTime);
            date=itemView.findViewById(R.id.msgDate);
            user=itemView.findViewById(R.id.tv_user_name);

        }
    }
}
