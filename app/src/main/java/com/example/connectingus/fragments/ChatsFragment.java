package com.example.connectingus.fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.transition.AutoTransition;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.conversation.DetailChatView;
import com.example.connectingus.R;
import com.example.connectingus.contact.SyncContacts;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.databinding.FragmentChatsBinding;
import com.example.connectingus.models.User;
import com.example.connectingus.profile.ExpandImageActivity;
import com.example.connectingus.profile.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    static Activity activity;
    private FragmentChatsBinding binding;
    FloatingActionButton fab;
    CustomAdapter customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //getActivity();
        //View v=inflater.inflate(R.layout.list_item,container,false);
        binding=FragmentChatsBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        activity = getActivity();
        int[] imageId={R.drawable.shrishti,R.drawable.ansar,R.drawable.kalpana,R.drawable.marie,R.drawable.muniba,R.drawable.rahi,R.drawable.sandeep,R.drawable.sunita,R.drawable.tina};
        String[] name={"Shrishti","Ansar","Kalpana","Marie","Muniba","Rahi","Sandeep","Sunita","Tina"};
        String[] lastMessage={"How are you?","Hi","Yes","I know","That's nice!","Good day!","I know","Why not?","See you!"};
        String[] lastmsgTime={"1:45am","1:30am","12:00am","11:45pm","11:30pm","11:15pm","11:00pm","10:05pm","09:05pm"};

        ArrayList<User> userArrayList=new ArrayList<>();

        for(int i=0;i<imageId.length;i++)
        {
            User user=new User(name[i],lastMessage[i],lastmsgTime[i],imageId[i]);
            userArrayList.add(user);
        }

        //ListAdapter listAdapter=new ListAdapter(getActivity(), userArrayList);
        //binding.listview.setAdapter(listAdapter);
        customAdapter=new CustomAdapter(userArrayList,getActivity());
        binding.listview.setAdapter(customAdapter);
        binding.listview.setClickable(true);
        //binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //        Toast.makeText(getActivity(),"Open "+name[i]+"'s chat activity!",Toast.LENGTH_LONG).show();
        //    }
        //});
        setHasOptionsMenu(true);
        //return inflater.inflate(R.layout.fragment_chats, container, false);
        return view;
    }

    public class CustomAdapter extends BaseAdapter implements Filterable
    {
        private List<User> itemsModelList;
        private List<User> itemsModelListFiltered;
        private Context context;

        public CustomAdapter(List<User> itemsModelList, Context context) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered=itemsModelList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1=getLayoutInflater().inflate(R.layout.list_item,null);
            ImageView imageView=view1.findViewById(R.id.profile_pic);
            TextView username=view1.findViewById(R.id.personName);
            TextView lastMsg=view1.findViewById(R.id.lastMessage);
            TextView time=view1.findViewById(R.id.msgtime);

            imageView.setImageResource(itemsModelListFiltered.get(i).getImageId());
            username.setText(itemsModelListFiltered.get(i).getName());
            lastMsg.setText(itemsModelListFiltered.get(i).getLastMessage());
            time.setText(itemsModelListFiltered.get(i).getLastMsgTime());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog=new Dialog(activity);
                    Intent intent;
                    dialog.setContentView(R.layout.profile_pic_expand);
                    dialog.setTitle(itemsModelListFiltered.get(i).getName());
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    ImageView expanded_pic = dialog.findViewById(R.id.expand_pic);
                    ImageView message = dialog.findViewById(R.id.message);
                    ImageView call = dialog.findViewById(R.id.call);
                    ImageView info = dialog.findViewById(R.id.info);
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(activity, TempDetailChatView.class).putExtra("user",itemsModelListFiltered.get(i)));
                        }
                    });
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(activity,"Calling "+itemsModelListFiltered.get(i).getName(),Toast.LENGTH_LONG).show();
                        }
                    });
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(activity,"Displaying info "+itemsModelListFiltered.get(i).getName(),Toast.LENGTH_LONG).show();
                        }
                    });
                    expanded_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, ExpandImageActivity.class);
                            intent.putExtra("name",itemsModelListFiltered.get(i).getName());
                            intent.putExtra("calling_activity","ConversationList");
                            intent.putExtra("image",itemsModelListFiltered.get(i).getImageId());
                            //intent.putExtra("image",);
                            Pair pair = new Pair(expanded_pic,"imageTransition");
                            ActivityOptions options = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                options = ActivityOptions.makeSceneTransitionAnimation(activity, pair);
                            }
                            startActivity(intent,options.toBundle());
                            dialog.hide();
                        }
                    });
                    expanded_pic.setImageResource(itemsModelListFiltered.get(i).getImageId());
                    dialog.show();
                }
            });

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(activity, TempDetailChatView.class).putExtra("user",itemsModelListFiltered.get(i)));
                }
            });
            return view1;
        }

        @Override
        public Filter getFilter() {
            Filter filter=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults filterResults=new FilterResults();
                    if(charSequence==null || charSequence.length()==0)
                    {
                        filterResults.count=itemsModelList.size();
                        filterResults.values=itemsModelList;
                    }
                    else
                    {
                        String searchStr=charSequence.toString().toLowerCase();
                        List<User> resultData=new ArrayList<>();
                        for(User user:itemsModelList)
                        {
                            if (user.getName().toLowerCase().contains(searchStr))
                            {
                                resultData.add(user);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    itemsModelListFiltered=(List<User>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_menu,menu);
        MenuItem item=menu.findItem(R.id.Search);
        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.Search)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fab=view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SyncContacts.class);
                startActivity(intent);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}