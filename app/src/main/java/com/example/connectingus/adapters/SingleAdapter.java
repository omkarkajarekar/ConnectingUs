package com.example.connectingus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.models.Aboutmsg;
import com.example.connectingus.R;

import java.util.ArrayList;

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.SingleViewHolder> {


    private Context context;
    private ArrayList<Aboutmsg> aboutmsgs;
    private int checkedPosition=-1; //-1: no default selection 0:first item selected

    public SingleAdapter(Context context, ArrayList<Aboutmsg> aboutmsgs) {
        this.context = context;
        this.aboutmsgs = aboutmsgs;
    }

    public void setAboutmsgs(ArrayList<Aboutmsg> aboutmsgs){
        this.aboutmsgs=new ArrayList<>();
        this.aboutmsgs=aboutmsgs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_about,parent,false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, int position) {
        holder.bind(aboutmsgs.get(position));
    }

    @Override
    public int getItemCount() {
        return aboutmsgs.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        public SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView= itemView.findViewById(R.id.aboutitem);
            imageView=itemView.findViewById(R.id.imageviewitem);
        }

        void bind(final Aboutmsg aboutmsg){
            if(checkedPosition==-1)
            {
                imageView.setVisibility(View.GONE);
            }
            else
            {
                if(checkedPosition==getAdapterPosition())
                {
                    imageView.setVisibility(View.VISIBLE);
                }
                else
                {
                    imageView.setVisibility(View.GONE);
                }
            }
            textView.setText(aboutmsg.getMsg());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setVisibility(View.VISIBLE);
                    if(checkedPosition!=getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition=getAdapterPosition();
                    }
                }
            });

            /*itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    imageView.setVisibility(View.VISIBLE);
                    if(checkedPosition!=getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition=getAdapterPosition();
                    }
                    return true;
                }
            });*/
        }
    }

    public Aboutmsg getSelected(){
        if(checkedPosition != -1)
        {
            return aboutmsgs.get(checkedPosition);
        }
        return null;
    }
}
