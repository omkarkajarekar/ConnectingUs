package com.example.connectingus.models;

public class TempMsgModel {

    String message;
    int id;
    Long timestamp;
    String senderID;

    public TempMsgModel(String message, int id) {
        this.message = message;
        this.id=id;
    }
    public TempMsgModel(String message, int id,long timestamp) {
        this.message = message;
        this.id=id;
        this.timestamp = timestamp;
    }

    public TempMsgModel(String senderID, String message) {
        this.message = message;
        this.senderID=senderID;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
