package com.mikerinehart.geekrepublic.interfaces;

import com.mikerinehart.geekrepublic.Constants;
import com.mikerinehart.geekrepublic.models.Post;

import java.util.Collection;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Mike on 4/25/2015.
 */
public interface ApiService {

    @GET("/posts?filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getAllArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=news" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getNewsArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=security" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getSecurityArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=gaming" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getGamingArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=mobile" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getMobileArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=technology" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getTechnologyArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=culture" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getCultureArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);

    @GET("/posts?filter[category_name]=gadgets" +
            "&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"" +
            "&filter[post_status]=publish" +
            "&filter[order]=DSC")
    void getGadgetsArticles(@Query("page") int pageNumber, Callback<List<Post>> articleList);
}
