package com.example.connectingus.fragments;

import java.util.ArrayList;

public class ShareIds {
    private static ShareIds instance=new ShareIds();

    public static ShareIds getInstance()
    {
        return instance;
    }
    private ArrayList<String> userId=new ArrayList<>();

    public ArrayList<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.add(userId);
    }

    public int getArraySize(){return this.userId.size();}
}
