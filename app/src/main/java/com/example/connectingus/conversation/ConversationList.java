package com.example.connectingus.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.adapters.MainAdapter;
import com.example.connectingus.contact.SyncContacts;
import com.example.connectingus.fragments.ChatsFragment;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.Users;
import com.example.connectingus.profile.Settings;
import com.example.connectingus.adapters.PagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConversationList extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    Thread thread;
    String verifyNumber;
    StorageReference storageReference;
    File localFile;
    String userId;
    BroadcastReceiver mReceiver;
    public static ArrayList<ContactModel> arrayList=new ArrayList<ContactModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_conversation_list);
        this.setTitle("ConnectingUs");
        tabLayout=findViewById(R.id.tablayout);
        viewPager2=findViewById(R.id.viewpager2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        FragmentManager fm= getSupportFragmentManager();
        pagerAdapter =new PagerAdapter(fm,getLifecycle());
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.setScrollPosition(1,0f,true);
        viewPager2.setCurrentItem(1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.NewGroup:
                Toast.makeText(ConversationList.this,"Clicked on New Group!",Toast.LENGTH_LONG).show();
                break;
            case R.id.Star:
                Toast.makeText(ConversationList.this,"Clicked on Starred Messages!",Toast.LENGTH_LONG).show();
                break;
            case R.id.Settings:
                Intent intent=new Intent(this, Settings.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        ChatsFragment chatsFragment = new ChatsFragment();
        if(chatsFragment.deleteitem.isVisible())
        {
            chatsFragment.openchat=true;
            chatsFragment.deleteitem.setVisible(false);
            Intent intent=new Intent(ConversationList.this,ConversationList.class);
            startActivity(intent);
        }
    }*/
}