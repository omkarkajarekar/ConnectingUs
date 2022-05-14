package com.example.connectingus.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.example.connectingus.R;
import com.example.connectingus.SplashActivity;
import com.example.connectingus.adapters.MainAdapter;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.models.ContactModel;
import java.util.ArrayList;
import java.util.HashSet;

public class SyncContacts extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList=new ArrayList<ContactModel>();
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Select contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_sync_contacts);
        recyclerView=findViewById(R.id.recycler_view);
        arrayList= SplashActivity.getArrayList();
        HashSet<ContactModel> hashset = new HashSet<ContactModel>();
        hashset.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(hashset );
        // set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialize adapter
        adapter=new MainAdapter(this,arrayList,this);
        //set adapter
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setSubtitle(adapter.getItemCount()+" contacts");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem=menu.findItem(R.id.search_action);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Contact");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(int position) {
        ContactModel model=arrayList.get(position);
        startActivity(new Intent(getApplicationContext(), TempDetailChatView.class).putExtra("UserDetails", model));
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemid = item.getItemId();

        if(itemid==R.id.invite)
        {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            String sub="Download the ConnectingUs chat application \n https://play.google.com/store/apps/details?id=com.whatsapp";

            intent.putExtra(Intent.EXTRA_TEXT,sub);
            startActivity(Intent.createChooser(intent,"Share using"));

        }
        if(itemid==R.id.refresh)
        {
            /*SplashActivity obj=new SplashActivity();
            SplashActivity.BgTaskContacts m=new SplashActivity().BgTaskContacts();
            SplashActivity.BgTaskContacts mytask=new SplashActivity.BgTaskContacts();
            //SplashActivity obj=new SplashActivity(mytask);
            mytask.execute("null");*/
            new SplashActivity().executeTask();
        }
        return super.onOptionsItemSelected(item);
    }


}