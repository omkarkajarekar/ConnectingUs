package com.example.connectingus.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.Users;
import com.example.connectingus.profile.Settings;
import com.example.connectingus.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversationList extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PagerAdapter pagerAdapter;
    Thread thread;
    String verifyNumber;
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
                //if(tab.getPosition()==0)
                //{
                //    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_camera_alt_24);
                //}
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


        thread=null;
        try {
            thread=new Thread(new Runnable() {
                @Override
                public void run() {
                            checkPermission();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(ConversationList.this,e.toString(),Toast.LENGTH_LONG).show();

        }

        thread.start();
    }



    public  void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},100);
        }
        else
        {
            getContactList();
        }

    }

    private void getContactList() {
        arrayList.clear();
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor=getContentResolver().query(uri,null,null,null,sort);
        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                @SuppressLint("Range") String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                @SuppressLint("Range") String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriphone=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                //initialize selection

                String selection=ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";

                //initialize phone cursor
                Cursor phoneCursor=getContentResolver().query(uriphone,null,selection,new String[]{id},null);
                if(phoneCursor.moveToNext())
                {
                    //when phone cursor move to next
                    @SuppressLint("Range") String number=phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //initialize contact model
                    ContactModel model=new ContactModel();
                    ImageView ivContactProf = findViewById(R.id.iv_image_item_contact);

                    verifyNumber =number.replaceAll("\\s", "");
                    if(!verifyNumber.contains("+91")) {
                        verifyNumber="+91"+verifyNumber;
                    }


                    FirebaseDatabase.getInstance()
                               .getReference("users")
                            .orderByChild("phone")
                            .equalTo(verifyNumber)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot.exists())
                                   {
                                       FirebaseDatabase.getInstance().getReference("users")
                                               .addValueEventListener(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot dsnapshot) {
                                                       for(DataSnapshot datas1:dsnapshot.getChildren())
                                                       {

                                                               Users user=datas1.getValue(Users.class);
                                                               if(user.getPhone().equals(verifyNumber))
                                                               {
                                                                   model.setUserId(user.getUserID());
                                                                   break;
                                                               }
                                                       }
                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError error) {

                                                   }
                                               });

                                       //set name and number
                                       model.setName(name);
                                       model.setNumber(number);
                                      // Bitmap bitmap=((BitmapDrawable)ivContactProf.getDrawable()).getBitmap();
                                       //model.setImage(bitmap);
                                       //add model in array list
                                       arrayList.add(model);
                                   }
                                        //Toast.makeText(ConversationList.this,snapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    phoneCursor.close();
                }
            }
            cursor.close();
        }


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            //when permission granted
            //call getContactList() method
            getContactList();
        }
        else
        {
            //when permission is denied
            Toast.makeText(this,"Permission Denied.",Toast.LENGTH_SHORT).show();
            //call check permission method
            checkPermission();
        }
    }

    public static ArrayList<ContactModel> getArrayList()
    {
        return arrayList;
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
}