package com.iecsemanipal.holocaust.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 24/8/17.
 */
public class RegistrationResponse {
    @Expose
    @SerializedName("status")
    private Boolean status;

    @Expose
    @SerializedName("description")
    private String description;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
