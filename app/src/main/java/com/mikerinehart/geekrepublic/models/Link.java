package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mike on 4/25/2015.
 */
public class Link {

    @SerializedName("self") private String selfLink; // Current entity location
    @SerializedName("archives") private String archivesLink;
    @SerializedName("author") private String authorLink;
    @SerializedName("collection") private String collectionLink; // Collection entity is a member of
    @SerializedName("version-history") private String versionHistoryLink;
    @SerializedName("up") private String upLink; // Parent entity location

    public Link(String selfLink, String archivesLink, String authorLink, String collectionLink, String versionHistoryLink, String upLink) {
        this.selfLink = selfLink;
        this.archivesLink = archivesLink;
        this.authorLink = authorLink;
        this.collectionLink = collectionLink;
        this.versionHistoryLink = versionHistoryLink;
        this.upLink = upLink;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getArchivesLink() {
        return archivesLink;
    }

    public void setArchivesLink(String archivesLink) {
        this.archivesLink = archivesLink;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    public String getCollectionLink() {
        return collectionLink;
    }

    public void setCollectionLink(String collectionLink) {
        this.collectionLink = collectionLink;
    }

    public String getVersionHistoryLink() {
        return versionHistoryLink;
    }

    public void setVersionHistoryLink(String versionHistoryLink) {
        this.versionHistoryLink = versionHistoryLink;
    }

    public String getUpLink() {
        return upLink;
    }

    public void setUpLink(String upLink) {
        this.upLink = upLink;
    }
}
