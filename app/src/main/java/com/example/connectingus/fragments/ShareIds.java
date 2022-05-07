package com.example.connectingus.fragments;

import com.example.connectingus.models.ContactModel;

import java.util.ArrayList;

public class ShareIds {
    private static ShareIds instance=new ShareIds();

    public static ShareIds getInstance()
    {
        return instance;
    }
    private ArrayList<ContactModel> userIdobj=new ArrayList<>();

    public ArrayList<ContactModel> getUserId() {
        return userIdobj;
    }

    public void setUserId(ContactModel userId) {
        this.userIdobj.add(userId);
    }
}
