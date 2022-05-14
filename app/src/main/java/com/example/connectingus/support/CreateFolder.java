package com.example.connectingus.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.connectingus.R;
import com.example.connectingus.adapters.TempMsgAdapter;
import com.example.connectingus.models.TempMsgModel;
import com.example.connectingus.profile.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateFolder {
    public static final String PROFILE_PHOTO = "Profile Photo";
    public static final String MY_PHOTO = "My Photo";
    public static final String WALLPAPER = "Wallpaper";
    public static final String EXPORT = "Export Chat";
    static int isStored=0;
    public void createFolderForProfile(Context context, String userID, Bitmap bitmap,String SubFolder){
        String folder_main = "ConnectingUs";
        //File f = Environment.getExternalStoragePublicDirectory(folder_main);
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File sub = new File(f, SubFolder);
        if (!sub.exists()) {
            sub.mkdirs();
        }
        File test = new File(sub , userID+".jpeg");
        try {
            if(!test.exists()) {
                test.createNewFile();
            }
            OutputStream stream = null;
            stream = new FileOutputStream(test);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            isStored++;
            if(isStored==1) {
                Toast.makeText(context, "Images saved in EXTERNAL STORAGE", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            Log.d("CreateFolder.java",e.getMessage());
        }
    }
    public Drawable getLocalImage(String userID,String SubFolder){
        File f = new File(Environment.getExternalStorageDirectory(), "ConnectingUs");
        File sub = new File(f, SubFolder);
        File test = new File(sub , userID+".jpeg");
        return Drawable.createFromPath(test.toString());
    }
    public void saveWallpaper(Context context, Bitmap bitmap){
        String folder_main = "ConnectingUs";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File sub = new File(f, WALLPAPER);
        if (!sub.exists()) {
            sub.mkdirs();
        }
        File test = new File(sub , "wallpaper"+".jpeg");
        try {
            if(!test.exists()) {
                test.createNewFile();
            }
            OutputStream stream = null;
            stream = new FileOutputStream(test);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            Toast.makeText(context, "Chat Wallpaper is set", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Log.d("CreateFolder.java",e.getMessage());
        }
    }
    public Drawable getWallpaper(){
        File f = new File(Environment.getExternalStorageDirectory(), "ConnectingUs");
        File sub = new File(f, WALLPAPER);
        if(!sub.exists()){
            return new ColorDrawable(0xF6F6F6);
        }
        File test = new File(sub , "wallpaper"+".jpeg");
        return Drawable.createFromPath(test.toString());
    }
    public void removeWallpaper(Context context){
        File f = new File(Environment.getExternalStorageDirectory(), "ConnectingUs");
        File sub = new File(f, WALLPAPER);
        if (sub.isDirectory())
        {
            String[] children = sub.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(sub, children[i]).delete();
            }
        }
        Toast.makeText(context, "Chat Wallpaper is removed", Toast.LENGTH_LONG).show();
    }
    public void exportChat(Context context, ArrayList<TempMsgModel> tempMsgModels,String sender,String receiver){
        String folder_main = "ConnectingUs";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File sub = new File(f, EXPORT);
        if (!sub.exists()) {
            sub.mkdirs();
        }
        File test = new File(sub , "ConnectingUs Chat with "+receiver+".txt");
        try {
            if(!test.exists()) {
                test.createNewFile();
            }
            FileWriter writer = null;
            writer = new FileWriter(test);
            if(tempMsgModels.size() == 0)
                return;
            for(int i=0;i< tempMsgModels.size();i++) {
                long timestamp = tempMsgModels.get(i).getTimestamp();
                String message = tempMsgModels.get(i).getMessage();
                String id = null;
                if(tempMsgModels.get(i).getId()==1)
                    id = sender;
                else
                    id = receiver;
                Date date=new Date(timestamp);
                SimpleDateFormat formatTime=new SimpleDateFormat("hh:mm a");
                String time=formatTime.format(date);
                SimpleDateFormat formatDate=new SimpleDateFormat("dd-MMM-yyyy");
                String dt=formatDate.format(date);
                writer.write(dt+", "+time+" - "+id+" : "+message+"\n");
            }
            writer.close();
            Toast.makeText(context, "Chat is Exported", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Log.d("CreateFolder.java",e.getMessage());
        }
    }
}
