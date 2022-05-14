package com.example.connectingus.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.connectingus.R;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.profile.Settings;
import com.example.connectingus.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;

public class ConversationList extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
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
}