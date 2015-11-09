package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.RequestAnswer;


import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.Query;

/**
 * Created by luciano on 16/10/15.
 */
public interface UserLoginService {
    @GET("/login")
    void loginUser(@Query("email") String email,@Query("password") String password,Callback<RequestAnswer> callback);
    }

