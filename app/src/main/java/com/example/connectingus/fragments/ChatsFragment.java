package com.example.connectingus.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.contact.SyncContacts;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.databinding.FragmentChatsBinding;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.DBHelper;
import com.example.connectingus.models.ShareIds;
import com.example.connectingus.profile.ChatProfile;
import com.example.connectingus.profile.CurrentProfile;
import com.example.connectingus.profile.ExpandImageActivity;
import com.example.connectingus.support.CreateFolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    static final int callRequest=1;
    DBHelper DB;
    public MenuItem deleteitem;
    Boolean checkinsertdata;
    ContactModel delete_model;
    String delete_name;
    String delete_userId;
    ShapeableImageView profile_pic;
    public boolean openchat=true;
    static Activity activity;
    private FragmentChatsBinding binding;
    FloatingActionButton fab;
    CustomAdapter customAdapter;
    String userId="";
    String callToModel="";
    ArrayList<ContactModel> listuserId=new ArrayList<>();
    ArrayList<ContactModel> userArrayList=new ArrayList<>();
    int j;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentChatsBinding.inflate(inflater,container,false);
        View view=binding.getRoot();
        activity = getActivity();
        DB=new DBHelper(getContext());
        //updateList();
        getPreviousList();
        binding.listview.setClickable(true);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    public void updateList()
    {
        listuserId.clear();
        for(ContactModel idobj : ShareIds.getInstance().getUserId())
        {
            listuserId.add(idobj);
        }
        for(ContactModel uidobj : listuserId)
        {
            userId=uidobj.getUserId();
            try
            {
                if(!userId.isEmpty()) {
                    String nm=uidobj.getName();
                    for(j=0;j<userArrayList.size();j++)
                    {
                        if(nm.equals(userArrayList.get(j).getName()))
                        {
                            userArrayList.remove(j);
                            //code to delete entry from SQLite
                            Boolean checkdeletedata=DB.deleteUserData(uidobj.getUserId());
                            break;
                        }
                    }
                    userArrayList.add(0,uidobj);
                    try {
                        checkinsertdata=DB.insertUserData(uidobj.getName(),uidobj.getUserId(),uidobj.getLastMessage(),uidobj.getLastMsgTime());
                    }
                    catch(Exception exp)
                    {
                        exp.printStackTrace();
                    }
                    if(checkinsertdata)
                        Log.i("ChatFragment.java","Chat Inserted");
                    else
                        Log.e("ChatFragment.java","Chat not Inserted");
                    customAdapter=new CustomAdapter(userArrayList,getActivity());
                    customAdapter.notifyDataSetChanged();
                    binding.listview.setAdapter(customAdapter);
                }
            }
            catch(Exception exp)
            {
                exp.printStackTrace();
            }
        }
    }

    public void getPreviousList()
    {
        ContactModel contactModel=null;
        Cursor res=DB.getData();
        if(res.getCount()==0)
        {
            Toast.makeText(getActivity(),"No recent chats are available!",Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("ChatFragment.java","No of records"+res.getCount());
        while(res.moveToNext())
        {
            contactModel=new ContactModel();
            contactModel.setName(res.getString(0));
            contactModel.setUserId(res.getString(1));
            contactModel.setLastMessage(res.getString(2));
            contactModel.setLastMsgTime(res.getString(3));

            ShareIds.getInstance().setUserId(contactModel);
        }
        if(contactModel!=null)
        {
            listuserId.clear();
            for(ContactModel idobj : ShareIds.getInstance().getUserId())
            {
                listuserId.add(idobj);
            }
            for(ContactModel uidobj : listuserId)
            {
                userId=uidobj.getUserId();
                try
                {
                    if(!userId.isEmpty()) {
                        String nm=uidobj.getName();
                        for(j=0;j<userArrayList.size();j++)
                        {
                            if(nm.equals(userArrayList.get(j).getName()))
                            {
                                userArrayList.remove(j);
                                break;
                            }
                        }
                        userArrayList.add(0,uidobj);
                        customAdapter=new CustomAdapter(userArrayList,getActivity());
                        customAdapter.notifyDataSetChanged();
                        binding.listview.setAdapter(customAdapter);
                    }
                }
                catch(Exception exp)
                {
                    exp.printStackTrace();
                }
            }
        }
    }

    public class CustomAdapter extends BaseAdapter implements Filterable
    {
        private List<ContactModel> itemsModelList;
        private List<ContactModel> itemsModelListFiltered;
        private Context context;

        public CustomAdapter(){}
        public CustomAdapter(List<ContactModel> itemsModelList, Context context) {
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
            TextView username=view1.findViewById(R.id.personName);
            TextView lastMsg=view1.findViewById(R.id.lastMessage);
            TextView time=view1.findViewById(R.id.msgtime);
            profile_pic=(ShapeableImageView)view1.findViewById(R.id.profile_pic);
            username.setText(itemsModelListFiltered.get(i).getName());
            lastMsg.setText(itemsModelListFiltered.get(i).getLastMessage());
            time.setText(itemsModelListFiltered.get(i).getLastMsgTime());
            profile_pic.setImageDrawable(new CreateFolder().getLocalImage(itemsModelListFiltered.get(i).getUserId(),CreateFolder.PROFILE_PHOTO));
            //profile_pic.setImageBitmap(itemsModelListFiltered.get(i).getImage());

            profile_pic.setOnClickListener(new View.OnClickListener() {
                ContactModel model=itemsModelListFiltered.get(i);
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
                            startActivity(new Intent(activity, TempDetailChatView.class).putExtra("UserDetails",model));
                            dialog.cancel();
                        }
                    });
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            callToModel=model.getNumber();
                            dialog.cancel();
                            checkPermission();
                        }
                    });
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent goChatProf = new Intent(activity, ChatProfile.class);
                            goChatProf.putExtra("UserDetails",model);
                            goChatProf.putExtra("calling_activity","ConversationList");
                            dialog.cancel();
                            startActivity(goChatProf);

                        }
                    });
                    expanded_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(activity, ExpandImageActivity.class);
                            intent.putExtra("user",model);
                            intent.putExtra("calling_activity","ConversationList");
                            Pair pair = new Pair(expanded_pic,"imageTransition");
                            ActivityOptions options = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                options = ActivityOptions.makeSceneTransitionAnimation(activity, pair);
                            }
                            startActivity(intent,options.toBundle());
                            dialog.hide();
                        }
                    });
                    expanded_pic.setImageDrawable(new CreateFolder().getLocalImage(model.getUserId(),CreateFolder.PROFILE_PHOTO));

                    dialog.show();
                }
            });

            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(openchat)
                    {
                        ContactModel model=itemsModelListFiltered.get(i);
                        startActivity(new Intent(activity, TempDetailChatView.class).putExtra("UserDetails",model));
                    }
                }
            });

            view1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    openchat=false;
                    deleteitem.setVisible(true);
                    delete_model=itemsModelListFiltered.get(i);
                    delete_name=delete_model.getName();
                    delete_userId=delete_model.getUserId();
                    return false;
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
                        List<ContactModel> resultData=new ArrayList<>();
                        for(ContactModel contactModel:itemsModelList)
                        {
                            if (contactModel.getName().toLowerCase().contains(searchStr))
                            {
                                resultData.add(contactModel);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    itemsModelListFiltered=(List<ContactModel>) filterResults.values;
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
        deleteitem=menu.findItem(R.id.Delete);
        //item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try
                {
                    customAdapter.getFilter().filter(newText);
                }
                catch(Exception exp)
                {
                    exp.printStackTrace();
                }
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
        if(id==R.id.Delete)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("Delete this chat?");
            builder.setMessage("Messages will only be removed from this device and your devices on the newer version of ConnectingUs");
            builder.setPositiveButton("DELETE CHAT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for(j=0;j<userArrayList.size();j++)
                    {
                        if(delete_name.equals(userArrayList.get(j).getName()))
                        {
                            userArrayList.remove(j);
                            break;
                        }
                    }
                    for(int k=0;k<ShareIds.getInstance().userIdobj.size();k++)
                    {
                        if(delete_name.equals(ShareIds.getInstance().userIdobj.get(k).getName()))
                        {
                            ShareIds.getInstance().userIdobj.remove(k);
                        }
                    }
                    Boolean checkdeletedata=DB.deleteUserData(delete_userId);
                    if(checkdeletedata)
                        Log.i("ChatFragment.java","Chat deleted");
                    else
                        Log.e("ChatFragment.java","Chat not deleted");
                    customAdapter=new CustomAdapter(userArrayList,getActivity());
                    customAdapter.notifyDataSetChanged();
                    binding.listview.setAdapter(customAdapter);
                    deleteitem.setVisible(false);
                    openchat=true;
                }
            });

            builder.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    openchat=true;
                    deleteitem.setVisible(false);
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
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

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},callRequest);
        }
        else
        {
            startCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if(requestCode==callRequest && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            startCall(); //when permission granted
        }
        else
        {
            //when permission is denied
            //call check permission method
            checkPermission();
        }
    }

    private void startCall()
    {
        Intent dial=new Intent(Intent.ACTION_CALL);
        dial.setData(Uri.parse("tel:"+callToModel));
        startActivity(dial);
    }
}