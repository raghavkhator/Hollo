package com.iecsemanipal.holocaust.models;

import io.realm.RealmObject;

/**
 * Created by anurag on 24/8/17.
 */
public class Pending extends RealmObject {
    private String memID;

    private String name;

    private String regNo;

    private String contactNo;

    private String email;

    public Pending(){
    }

    public Pending(Member member) {
        memID = member.getMemID();
        name = member.getName();
        regNo = member.getRegNo();
        contactNo = member.getContactNo();
        email = member.getEmail();
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

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
