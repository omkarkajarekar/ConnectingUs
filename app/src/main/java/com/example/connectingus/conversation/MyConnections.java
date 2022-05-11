package com.example.connectingus.conversation;

import java.util.ArrayList;

public class MyConnections {
    static ArrayList<String> userIDs = new ArrayList<>();
    public MyConnections(){}
    public MyConnections(String user){
        userIDs.add(user);
    }
}
