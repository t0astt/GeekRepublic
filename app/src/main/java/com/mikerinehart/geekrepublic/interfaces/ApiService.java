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

    @GET("/posts?filter[category_name]=news&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"&filter[order]=DSC")
    void getNews(Callback<List<Post>> postList);

    @GET("/posts?filter[category_name]=news&filter[posts_per_page]="+Constants.POSTS_TO_DISPLAY+"&filter[order]=DSC")
    void getMoreNews(@Query("page") int pageNumber, Callback<List<Post>> postList);
}
