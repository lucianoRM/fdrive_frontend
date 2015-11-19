package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by luciano on 10/11/15.
 */
public interface UserLogoutService {
    @GET("/logout")
    void logoutUser(@Query("email") String email,@Query("token") String token, Callback<SimpleRequestAnswer> callback);
}
