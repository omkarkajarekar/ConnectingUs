package com.example.connectingus.models;

public class Users {
    private String phone;
    private String userID;
    private String name;
    private String about;
    private String deviceID;
    private String profile_pic;
    public Users(){ }
    public Users(String phone, String userID){
        this.phone = phone;
        this.userID = userID;
    }
    public Users(String phone, String userID, String name, String about, String deviceID, String profile_pic){
        this.phone = phone;
        this.userID = userID;
        this.name = name;
        this.about = about;
        this.deviceID = deviceID;
        this.profile_pic = profile_pic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
