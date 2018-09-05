package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 25/10/17.
 */
public class Attendance {
    @Expose
    @SerializedName("MemID")
    private String memID;

    @Expose
    @SerializedName("eventID")
    private String eventID;

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
