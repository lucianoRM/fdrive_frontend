package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by luciano on 07/11/15.
 */


public interface GetUserFilesService {
    @GET("/userfiles")
    void getUserFiles(@Query("email") String email,@Query("token") String token,@Query("path") String path,Callback<GetUserFilesAnswer> callback);
}