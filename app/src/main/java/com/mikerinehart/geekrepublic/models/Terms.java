package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mike on 4/25/2015.
 */
public class Terms {

    @SerializedName("post_tag") private PostTag[] postTags;
    @SerializedName("category") private Category[] category;
    @SerializedName("post_format") private PostFormat[] postFormat;

}
