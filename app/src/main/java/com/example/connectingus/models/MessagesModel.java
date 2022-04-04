package com.example.connectingus.models;

public class MessagesModel
{
    String id;
    String msg;
    Long timestamp;

    public MessagesModel(){}

    public MessagesModel(String id, String msg, Long timestamp) {
        this.id = id;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public MessagesModel(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
