package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by anurag on 17/8/17.
 */
public class Member extends RealmObject {
    @Expose
    @SerializedName("MemID")
    private String memID;

    @Expose
    @SerializedName("Name")
    private String name;

    @Expose
    @SerializedName("RegNo")
    private String regNo;

    @Expose
    @SerializedName("Mobile")
    private String contactNo;

    @Expose
    @SerializedName("Email")
    private String email;

    private String timeStamp;

    public Member(){

    }

    public Member(Pending pending){
        memID = pending.getMemID();
        name = pending.getName();
        regNo = pending.getRegNo();
        contactNo = pending.getContactNo();
        email = pending.getEmail();
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) { this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
