package com.example.connectingus.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.authentication.ProfileEdit;
import com.example.connectingus.conversation.ConversationList;
import com.example.connectingus.conversation.TempDetailChatView;
import com.example.connectingus.models.ContactModel;
import com.example.connectingus.models.User;
import com.example.connectingus.support.CreateFolder;


public class ChatProfile extends AppCompatActivity {
    String name,userid,phone;
    byte[] byteArray;
    static final int callRequest=1;
    String calling_activity;
    ImageView chatProf;
    TextView chatName;
    TextView chatAbout;
    TextView chatPhone;
    ImageButton chatBackBtn;
    Button gotoChat;
    Button call;
    ContactModel contactModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);
        getSupportActionBar().hide();

        chatProf=findViewById(R.id.iv_chatProfile);
        chatBackBtn=findViewById(R.id.chatprof_back);
        chatName=findViewById(R.id.chatProfile_name);
        chatAbout=findViewById(R.id.chatProfile_about);
        chatPhone=findViewById(R.id.chatProfile_phoneno);
        gotoChat=findViewById(R.id.chatProfile_gotoChat);
        call=findViewById(R.id.chatProfile_call);

        chatBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goBackToActivity(calling_activity);
            }
        });

        gotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TempDetailChatView.class);
                intent.putExtra("SelectedContact", contactModel);
                startActivity(intent);
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        Intent intent=getIntent();
        //Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");
        //
        /*byteArray = intent.getByteArrayExtra("imageByte");
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            chatProf.setImageBitmap(image);
        }
        catch (Exception e){
            chatProf.setImageResource(R.drawable.ic_baseline_person_24);
        }
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        userid = intent.getStringExtra("userID");
        chatName.setText(name);
        chatPhone.setText(phone);*/
        calling_activity=intent.getStringExtra("calling_activity");
        if(intent.getExtras()!=null)
        {
            contactModel= (ContactModel) intent.getSerializableExtra("UserDetails");
            calling_activity=intent.getStringExtra("calling_activity");
            //chatProf.setImageBitmap(contactModel.getImage());
            //chatProf.setImageResource(contactModel.getImageId());
            chatProf.setImageDrawable(new CreateFolder().getLocalImage(contactModel.getUserId(),CreateFolder.PROFILE_PHOTO));
            chatName.setText(contactModel.getName());
            chatPhone.setText(contactModel.getNumber());
        }


    }

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},callRequest);
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
        dial.setData(Uri.parse("tel:"+chatPhone.getText().toString()));
        startActivity(dial);
    }

    public void goBackToActivity(String parentActivity)
    {
        Intent parent=null;
        if(parentActivity.equals("ConversationList"))
        {
            parent = new Intent(getApplicationContext(), ConversationList.class);
        }
        else if(calling_activity.equals("TempDetailChatView"))
        {
            parent = new Intent(getApplicationContext(), TempDetailChatView.class);
            parent.putExtra("UserDetails",contactModel);
            /*parent.putExtra("userID",userid);
            parent.putExtra("name",name);
            parent.putExtra("phone",phone);
            parent.putExtra("imageByte",byteArray);*/
        }
        parent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(parent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBackToActivity(calling_activity);
        super.onBackPressed();
    }
}