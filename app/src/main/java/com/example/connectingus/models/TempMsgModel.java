package com.example.connectingus.models;

public class TempMsgModel {

    String message;
    int id;
    public TempMsgModel(String message, int id) {
        this.message = message;
        this.id=id;
    }

    public TempMsgModel() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
