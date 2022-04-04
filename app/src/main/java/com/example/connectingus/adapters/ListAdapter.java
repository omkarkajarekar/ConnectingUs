package com.example.connectingus.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.connectingus.R;
import com.example.connectingus.models.User;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {

    public ListAdapter(FragmentActivity context, ArrayList<User> userArrayList)
    {
        super(context, R.layout.list_item,userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user=getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView imageView=convertView.findViewById(R.id.profile_pic);
        TextView username=convertView.findViewById(R.id.personName);
        TextView lastMsg=convertView.findViewById(R.id.lastMessage);
        TextView time=convertView.findViewById(R.id.msgtime);

        imageView.setImageResource(user.imageId);
        username.setText(user.name);
        lastMsg.setText(user.lastMessage);
        time.setText(user.lastMsgTime);
        return convertView;
    }
}
