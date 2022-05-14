package com.example.connectingus.models;

import android.graphics.Bitmap;

public class StarredModel {
    String msg;
    long Timestamp;
    String user;
    String userId;
    public int imageId;
    public Bitmap image;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public byte[] byteArray;
public StarredModel(){}
    public StarredModel(String msg, long timestamp, String user) {
        this.msg = msg;
        Timestamp = timestamp;
        this.user = user;
    }

    public StarredModel(String msg, long timestamp, String user, int imageId, Bitmap image, byte[] byteArray) {
        this.msg = msg;
        Timestamp = timestamp;
        this.user = user;
        this.imageId = imageId;
        this.image = image;
        this.byteArray = byteArray;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
