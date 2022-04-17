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
    String userID;

    public static ArrayList<ContactModel> arrayList=new ArrayList<ContactModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public String getUserIDs(String number)
    {

        FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dsnapshot) {


                        for(DataSnapshot datas1:dsnapshot.getChildren())
                        {

                            Users user=datas1.getValue(Users.class);

                                if(user.getPhone().equals(number))
                                {
                                    Log.d("qnqq", "number from firebase: "+user.getPhone()+"  :::"+user.getPhone()+"  userID:"+user.getUserID()+" name:"+user.getName());
                                }





                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return "hi";
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
                    //ImageView ivContactProf = findViewById(R.id.iv_image_item_contact);

                    verifyNumber =number.replaceAll("\\s", "");
                    if(!verifyNumber.contains("+91")) {
                        verifyNumber="+91"+verifyNumber;
                    }

                    storageReference = FirebaseStorage.getInstance().getReference();
                    FirebaseDatabase.getInstance()
                               .getReference("users")
                            .orderByChild("phone")
                            .equalTo(verifyNumber)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot.exists())
                                   {
                                       /*FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dsnapshot) {
                                               Users user=null;
                                               for(DataSnapshot datas1:dsnapshot.getChildren())
                                               {
                                                   user=datas1.getValue(Users.class);
                                                   for(ContactModel model:arrayList)
                                                   {
                                                       if(user.getPhone().equals(model.getNumber()))
                                                       {
                                                           model.setUserId(user.getUserID());
                                                           Log.d("qnqq", "number from firebase: "+user.getPhone()+"  :::"+model.getNumber()+"  userID:"+model.getUserId()+" name:"+model.getName());
                                                           getImages(model);
                                                       }
                                                   }
                                                   user=null;
                                               }


                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });*/
                                           // getUserIDs(verifyNumber);
                                      /* FirebaseDatabase.getInstance().getReference("users")
                                               .addValueEventListener(new ValueEventListener() {
                                                   @Override
                                                   public void onDataChange(@NonNull DataSnapshot dsnapshot) {


                                                       for(DataSnapshot datas1:dsnapshot.getChildren())
                                                       {

                                                           Users user=datas1.getValue(Users.class);
                                                           if(user.getPhone().equals(verifyNumber))
                                                           {
                                                               Log.d("qnqq", "number from firebase: "+user.getPhone()+"  :::"+verifyNumber+"  userID:"+user.getUserID()+" name:"+user.getName());
                                                           }


                                                         /*  if(user.getPhone().equals(verifyNumber))
                                                           {
                                                               model.setUserId(user.getUserID());
                                                               userID = user.getUserID();
                                                               Log.d("qnppy details", "  name: "+user.getName()+" user Id:  "+userID);
                                                               break;
                                                           }

                                                       }*/

                                                    /*   if(dsnapshot.getValue(Users.class)!=null)
                                                       {


                                                       }

                                                      /* while(true)
                                                       {
                                                           DataSnapshot datas1= (DataSnapshot) dsnapshot.getChildren();
                                                           if(datas1.hasChildren())
                                                           {
                                                               Users user=datas1.getValue(Users.class);

                                                               if(user.getPhone().equals(verifyNumber))
                                                               {
                                                                   model.setUserId(user.getUserID());
                                                                   userID = user.getUserID();
                                                                   userids="name: "+user.getName()+"user ID: "+userID+"     "+userids;
                                                                   break;
                                                               }

                                                           }
                                                           else
                                                           {
                                                               break;
                                                           }


                                                       }*/
                                                     /*  for(DataSnapshot datas1:dsnapshot.getChildren())
                                                       {

                                                           Users user=datas1.getValue(Users.class);

                                                         if(user.getPhone().equals(verifyNumber))
                                                               {
                                                                   model.setUserId(user.getUserID());
                                                                   userID = user.getUserID();
                                                                   Log.d("qnppy details", "  name: "+user.getName()+" user Id:  "+userID);
                                                                   break;
                                                               }

                                                       }
                                                       //userids+=" COUNT="+dsnapshot.getChildrenCount();
                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError error) {

                                                   }
                                               });*/

                                       //set name and number
                                       model.setName(name);
                                       String tempnum =number.replaceAll("\\s", "");
                                       if(!tempnum.contains("+91")) {
                                           tempnum="+91"+tempnum;
                                       }
                                       model.setNumber(tempnum);
                                      // Bitmap bitmap=((BitmapDrawable)ivContactProf.getDrawable()).getBitmap();
                                       //model.setImage(bitmap);
                                       /*StorageReference pathReference = storageReference.child(userID).child("profile.jpg");

                                       try {
                                           localFile = File.createTempFile("profile", "jpg");
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }

                                       pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                           @Override
                                           public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                               Bitmap bmImg = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                               model.setImage(bmImg);
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception exception) {
                                               Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_person_24);
                                               model.setImage(image);
                                           }
                                       });*/
                                      //Toast.makeText(getApplicationContext(),"UserID = "+userID,Toast.LENGTH_SHORT).show();
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
       // Log.d("list of userids", userids);
    }

    public void getImages(ContactModel model)
    {
        File localFile = null;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageReference.child(model.getUserId()).child("profile.jpg");

        try {
            localFile = File.createTempFile("profile", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalLocalFile = localFile;
        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bmImg = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                model.setImage(bmImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_person_24);
                model.setImage(image);
            }
        });
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