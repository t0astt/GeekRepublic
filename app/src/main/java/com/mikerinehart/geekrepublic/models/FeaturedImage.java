package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mike on 4/25/2015.
 */
public class FeaturedImage {

    @SerializedName("ID") private int id;
    @SerializedName("source") private String sourceURL;

    public FeaturedImage(int id, String sourceURL) {
        this.id = id;
        this.sourceURL = sourceURL;
    }

    public int getId() {
        return id;
    }

    public String getSourceURL() {
        return sourceURL;
    }
}
