package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by luciano on 17/10/15.
 */

public interface UserSignUpService{
    @POST("/users")
    void registerUser(@Query("email") String email,@Query("password") String password, Callback<SimpleRequestAnswer> callback);
}




