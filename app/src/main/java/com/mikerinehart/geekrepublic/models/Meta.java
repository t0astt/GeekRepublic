package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mike on 4/25/2015.
 */

public class Meta {

    @SerializedName("links") private Link links;

    public Meta(Link links) {
        this.links = links;
    }

    public Link getLinks() {
        return links;
    }

    public void setLinks(Link links) {
        this.links = links;
    }
}
