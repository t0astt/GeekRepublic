package com.mikerinehart.geekrepublic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikerinehart.geekrepublic.interfaces.ApiService;

import retrofit.RestAdapter;

/**
 * Created by Mike on 4/25/2015.
 */
public class RestClient {

    private static final String BASE_URL = "https://geekrepublic.org/wp-json";
    private ApiService apiService;

    public RestClient()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                //.setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService()
    {
        return apiService;
    }

}
