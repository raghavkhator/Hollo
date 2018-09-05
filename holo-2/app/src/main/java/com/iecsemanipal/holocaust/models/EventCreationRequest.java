package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 24/10/17.
 */
public class EventCreationRequest {
    @Expose
    @SerializedName("apikey")
    private String apiKey;

    @Expose
    @SerializedName("data")
    private Event event;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
