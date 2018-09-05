package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 25/10/17.
 */
public class GetEventsRequest {
    @Expose
    @SerializedName("apikey")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
