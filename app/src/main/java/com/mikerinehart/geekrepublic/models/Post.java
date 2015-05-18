package com.mikerinehart.geekrepublic.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("ID") private int id;
    @SerializedName("title") private String title;
    @SerializedName("featured_image") private FeaturedImage featuredImage;
//    @SerializedName("date") private Date dateCreated;
//    @SerializedName("date_gmt") private Date dateCreatedGMT;
//    @SerializedName("modified") private Date dateModified;
//    @SerializedName("modified_gmt") private Date dateModifiedGMT;
//    @SerializedName("date_tz") private String timezone;
//    @SerializedName("modified_tz") private String modifiedTimezone;
//    @SerializedName("status") private String status;
//    @SerializedName("type") private String type;
//    @SerializedName("name") private String name;
//    @SerializedName("author") private Author author;
//    @SerializedName("password") private String password;
    @SerializedName("content") private String content;
//    @SerializedName("excerpt") private String excerpt;
//    @SerializedName("content_raw") private String contentRaw;
//    @SerializedName("except_raw") private String excerptRaw;
//    @SerializedName("parent") private int parentID;
//    @SerializedName("link") private String url;
//    @SerializedName("guid") private String guid;
//    @SerializedName("menu_order") private int order; // Sort posts. Larger values should be sorted before smaller values
//    @SerializedName("comment_status") private String commentStatus = "closed"; // Either 'open' or 'closed'. Missing value defaults to closed
//    @SerializedName("ping_status") private String pingStatus = "closed"; // Either 'open' or 'closed'. Missing value defaults to closed
//    @SerializedName("sticky") private boolean sticky; // Sticky posts should be displayed before other posts
//    @SerializedName("post_thumbnail") private Media thumbnail;
//    @SerializedName("post_format") private String format; // Indicates how meta fields should be displayed. 'standard', 'aside', 'gallery', 'link', 'status', 'quote', 'video', 'audio', 'chat'
//    @SerializedName("terms") private Terms terms;
//    @SerializedName("post_meta") private Metadata metadata;
//    @SerializedName("meta") private Meta meta;

    public Post(int id, String title, FeaturedImage featuredImage, String content) {
        this.id = id;
        this.title = title;
        this.featuredImage = featuredImage;
        this.content = content;
    }


//    public Post(int id, String title, FeaturedImage featuredImage, Date dateCreated, Date dateCreatedGMT, Date dateModified, Date dateModifiedGMT, String timezone, String modifiedTimezone, String status, String type, String name, Author author, String password, String content, String excerpt, String contentRaw, String excerptRaw, int parentID, String url, String guid, int order, String commentStatus, String pingStatus, boolean sticky, Media thumbnail, String format, Terms terms, Metadata metadata, Meta meta) {
//        this.id = id;
//        this.title = title;
//        this.featuredImage = featuredImage;
//        this.dateCreated = dateCreated;
//        this.dateCreatedGMT = dateCreatedGMT;
//        this.dateModified = dateModified;
//        this.dateModifiedGMT = dateModifiedGMT;
//        this.timezone = timezone;
//        this.modifiedTimezone = modifiedTimezone;
//        this.status = status;
//        this.type = type;
//        this.name = name;
//        this.author = author;
//        this.password = password;
//        this.content = content;
//        this.excerpt = excerpt;
//        this.contentRaw = contentRaw;
//        this.excerptRaw = excerptRaw;
//        this.parentID = parentID;
//        this.url = url;
//        this.guid = guid;
//        this.order = order;
//        this.commentStatus = commentStatus;
//        this.pingStatus = pingStatus;
//        this.sticky = sticky;
//        this.thumbnail = thumbnail;
//        this.format = format;
//        this.terms = terms;
//        this.metadata = metadata;
//        this.meta = meta;
//    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public FeaturedImage getFeaturedImage() {
        return featuredImage;
    }

//    public Date getDateCreated() {
//        return dateCreated;
//    }
//
//    public Date getDateCreatedGMT() {
//        return dateCreatedGMT;
//    }
//
//    public Date getDateModified() {
//        return dateModified;
//    }
//
//    public Date getDateModifiedGMT() {
//        return dateModifiedGMT;
//    }
//
//    public String getTimezone() {
//        return timezone;
//    }
//
//    public String getModifiedTimezone() {
//        return modifiedTimezone;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public Author getAuthor() {
//        return author;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
    public String getContent() {
        return content;
    }
//
//    public String getExcerpt() {
//        return excerpt;
//    }
//
//    public String getContentRaw() {
//        return contentRaw;
//    }
//
//    public String getExcerptRaw() {
//        return excerptRaw;
//    }
//
//    public int getParentID() {
//        return parentID;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public String getGuid() {
//        return guid;
//    }
//
//    public int getOrder() {
//        return order;
//    }
//
//    public String getCommentStatus() {
//        return commentStatus;
//    }
//
//    public String getPingStatus() {
//        return pingStatus;
//    }
//
//    public boolean isSticky() {
//        return sticky;
//    }
//
//    public Media getThumbnail() {
//        return thumbnail;
//    }
//
//    public String getFormat() {
//        return format;
//    }
//
//    public Terms getTerms() {
//        return terms;
//    }
//
//    public Metadata getMetadata() {
//        return metadata;
//    }
//
//    public Meta getMeta() {
//        return meta;
//    }
}
