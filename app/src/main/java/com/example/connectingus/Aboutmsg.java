package com.example.connectingus;

import java.io.Serializable;

public class Aboutmsg implements Serializable {
    private  boolean isChecked=false;
    private String msg;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
