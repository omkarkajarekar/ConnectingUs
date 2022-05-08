package com.example.connectingus.contact;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.SplashActivity;
import com.example.connectingus.adapters.MainAdapter;
import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.Users;
import com.example.connectingus.profile.Settings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

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
        //startActivity(new Intent(getApplicationContext(), TempDetailChatView.class).putExtra("user", model));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap image = model.getImage();
        byte[] byteArray = null;
        if(image!=null) {
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }
        startActivity(new Intent(getApplicationContext(), TempDetailChatView.class).putExtra("imageByte", byteArray).putExtra("phone", model.getNumber()).putExtra("userID", model.getUserId()).putExtra("name", model.getName()));
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
        return super.onOptionsItemSelected(item);
    }


}