package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 25/10/17.
 */
public class AttendanceRequest {
    @Expose
    @SerializedName("apikey")
    private String apiKey;

    @Expose
    @SerializedName("data")
    private Attendance attendance;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
}
