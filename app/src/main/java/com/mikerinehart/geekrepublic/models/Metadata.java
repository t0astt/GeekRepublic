package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mike on 4/25/2015.
 */
public class Metadata {

    @SerializedName("id") private int id; // Internal metadata ID
    @SerializedName("key") private String key; // Metadata field name
    @SerializedName("value") private String value; // Metadata field value

    public Metadata(int id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
