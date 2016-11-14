package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.SearchAnswer;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Streaming;

/**
 * Created by luciano on 14/11/15.
 */
public interface SearchService {
    @GET("/searches")
    void search(@Query("email") String email, @Query("token") String password, @Query("typeofsearch") String searchType,@Query("element") String element,Callback<SearchAnswer> callback);

}