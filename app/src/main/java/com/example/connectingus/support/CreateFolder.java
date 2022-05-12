package com.example.connectingus.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CreateFolder {
    public static final String PROFILE_PHOTO = "Profile Photo";
    public static final String MY_PHOTO = "My Photo";
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
}
