package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 20/10/17.
 */
public class Event {
    @Expose
    @SerializedName("eventID")
    private String eventID;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("place")
    private String venue;

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("time")
    private String time;

    @Expose
    @SerializedName("category")
    private String category;

    @Expose
    @SerializedName("audienceType")
    private String audienceType;

    @Expose
    @SerializedName("description")
    private String description;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAudienceType() {
        return audienceType;
    }

    public void setAudienceType(String audienceType) {
        this.audienceType = audienceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
