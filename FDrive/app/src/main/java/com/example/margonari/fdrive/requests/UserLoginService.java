package com.example.margonari.fdrive.requests;

import java.util.List;
import java.util.Map;


import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luciano on 16/10/15.
 */
public interface UserLoginService {
    @GET("/login")
    void loginUser(@Query("email") String email,@Query("password") String password,Callback<RequestAnswer> callback);
    }

