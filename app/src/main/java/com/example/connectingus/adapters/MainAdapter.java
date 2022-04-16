package com.example.connectingus.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connectingus.models.ContactModel;
import com.example.connectingus.R;
import com.example.connectingus.contact.RecyclerViewInterface;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Filterable {
     private RecyclerViewInterface recyclerViewInterface;
     Activity activity;
     ArrayList<ContactModel> arrayList;
    ArrayList<ContactModel> searcharrayList;
     public MainAdapter(Activity activity,ArrayList<ContactModel> arrayList,RecyclerViewInterface recyclerViewInterface)
     {
         this.recyclerViewInterface=recyclerViewInterface;
         this.activity=activity;
         this.arrayList=arrayList;
         this.searcharrayList=new ArrayList<>(arrayList);
         notifyDataSetChanged();
     }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,parent,false);

        return new ViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         ContactModel model=arrayList.get(position);
         holder.tvName.setText(model.getName());
         holder.tvNumber.setText(model.getNumber());
         //holder.ivImage.setImageBitmap(model.getImage());
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }



    private  final Filter arrayFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ContactModel> filteredList=new ArrayList<>();

            if(charSequence==null || charSequence.length()==0)
            {
                filteredList.addAll(searcharrayList);
            }
            else
            {
                String filterPattern=charSequence.toString().toLowerCase().trim();
                for(ContactModel cm:searcharrayList)
                {
                    if(cm.name.toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(cm);
                    }
                }
            }
           FilterResults results=new FilterResults();
            results.values=filteredList;
            results.count=filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                       arrayList.clear();
                       arrayList.addAll( (ArrayList)filterResults.values);
                       notifyDataSetChanged();
        }
    };
    public Filter getFilter() {

        return arrayFilter;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        ShapeableImageView ivImage;
        public ViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            tvNumber=itemView.findViewById(R.id.tv_number);
            //ivImage=itemView.findViewById(R.id.iv_image_item_contact);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
