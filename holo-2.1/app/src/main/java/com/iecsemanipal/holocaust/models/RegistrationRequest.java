package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anurag on 24/8/17.
 *
 * Modified by Raghav, Shashank, Roshan on 5/09/2018
 */
public class RegistrationRequest {
    @Expose
    @SerializedName("eventname")
    private String eventName;

    @Expose
    @SerializedName("eventpasskey")
    private String eventpasskey;

    @Expose
    @SerializedName("data")
    private String data;

    public RegistrationRequest(String name, String pass, Member member){
        try {
            JSONObject nameObj = new JSONObject();
            nameObj.put("name", "name");
            nameObj.put("value", member.getName());

            JSONObject memIDObj = new JSONObject();
            memIDObj.put("name", "MemID");
            memIDObj.put("value", member.getMemID());

            JSONObject regNoObj = new JSONObject();
            regNoObj.put("name", "regNo");
            regNoObj.put("value", member.getRegNo());

            JSONObject mobileObj = new JSONObject();
            mobileObj.put("name", "mobile");
            mobileObj.put("value", member.getContactNo());

            JSONObject emailObj = new JSONObject();
            emailObj.put("name", "email");
            emailObj.put("value", member.getEmail());

            JSONArray fields = new JSONArray();
            fields.put(nameObj);
           fields.put(emailObj);
            fields.put(regNoObj);
            fields.put(memIDObj);
            fields.put(mobileObj);

            JSONObject dataObj = new JSONObject();
            dataObj.put("fields", fields);

            eventName = name;
            eventpasskey = pass;
            data = dataObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventpasskey() {
        return eventpasskey;
    }

    public void setEventpasskey(String eventpasskey) {
        this.eventpasskey = eventpasskey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
