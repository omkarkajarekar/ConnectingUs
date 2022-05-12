package com.example.connectingus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;

import com.example.connectingus.authentication.FirstActivity;
import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.Users;
import com.example.connectingus.support.CreateFolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN=2000;
    FirebaseAuth firebaseAuth;
    String verifyNumber;
    StorageReference storageReference;

    static Context context;
    public static ArrayList<ContactModel> arrayList=new ArrayList<ContactModel>();



    class BgTaskContacts extends AsyncTask<String,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {

            super.onPostExecute(unused);
        }

        @Override
        protected Void doInBackground(String... strings) {
            //SplashActivity obj=new SplashActivity();
            arrayList.clear();
           getContactList();
            getUserIDs();

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
      checkPermission();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser() != null){
                    Intent intent = new Intent(getApplicationContext(), ConversationList.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }

    @Override
    protected void onDestroy() {
       // getCacheDir().delete();
        super.onDestroy();
    }

public  void executeTask()
{
    BgTaskContacts mytask=new BgTaskContacts();
    //SplashActivity obj=new SplashActivity(mytask);
    mytask.execute("null");
}
    public static void deleteCache()
    {
        try
        {
            File dir=context.getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e)
        {
            Log.d("cache delete exception", "deleteCache: "+e.toString());
        }

    }


    private static boolean deleteDir(File dir) {
        if(dir!=null && dir.isDirectory())
        {
            String[] children= dir.list();
            for(int i=0;i< children.length;i++)
            {
                boolean success=deleteDir(new File(dir,children[i]));
                if(!success)
                {
                    return  false;
                }
            }
            return dir.delete();
        }
        else if (dir!=null && dir.isFile())
        {
            return dir.delete();
        }
        else
        {
            return false;
        }

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
                new CreateFolder().createFolderForProfile(SplashActivity.this,model.getUserId(),bmImg,CreateFolder.PROFILE_PHOTO);
                //model.setImage(bmImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
             Bitmap bmImg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_person_24);
             new CreateFolder().createFolderForProfile(SplashActivity.this,model.getUserId(),bmImg,CreateFolder.PROFILE_PHOTO);
                //model.setImage(bmImg);
            }
        });
    }

    public void getUserIDs()
    {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
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
        });
    }
    public  void checkPermission() {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SplashActivity.this,new String[]{Manifest.permission.READ_CONTACTS},100);
        }
        else
        {
            BgTaskContacts mytask=new BgTaskContacts();
            //SplashActivity obj=new SplashActivity(mytask);
            mytask.execute("null");
        }

    }
    private void getContactList() {

        arrayList.clear();
        Uri uri= ContactsContract.Contacts.CONTENT_URI;
        String sort=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";
        Cursor cursor=context.getContentResolver().query(uri,null,null,null,sort);
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
                Cursor phoneCursor=context.getContentResolver().query(uriphone,null,selection,new String[]{id},null);
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

                                        //set name and number
                                        model.setName(name);
                                        String tempnum =number.replaceAll("\\s", "");
                                        if(!tempnum.contains("+91")) {
                                            tempnum="+91"+tempnum;
                                        }
                                        model.setNumber(tempnum);

                                        arrayList.add(model);
                                    }


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

    public static ArrayList<ContactModel> getArrayList()
    {
        return arrayList;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            //when permission granted
            //call getContactList() method
            BgTaskContacts mytask=new BgTaskContacts();
            //SplashActivity obj=new SplashActivity(mytask);
            mytask.execute("null");
        }
        else
        {
            //when permission is denied
            //call check permission method
            checkPermission();
        }
    }


}