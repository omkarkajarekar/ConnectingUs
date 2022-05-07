package com.example.connectingus.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.Serializable;

public class ContactModel implements Serializable {
    public int imageId;
    public Bitmap image;
    public byte[] byteArray;
    public String name;
    public String number;
    public String userId;
    public String lastMessage;
    public String lastMsgTime;

    public ContactModel(){}

    public ContactModel(String name, String lastMessage, String lastMsgTime, Bitmap image) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.image=image;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] getByteImage(){ return byteArray;}

    public void setByteImage(byte[] byteArray){ this.byteArray = byteArray;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
