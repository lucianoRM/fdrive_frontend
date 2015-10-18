package com.example.margonari.fdrive.requests;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luciano on 17/10/15.
 */

public interface UserSignUpService{
    @GET("/users")
    void registerUser(@Query("email") String email,@Query("password") String password, Callback<H> callback);
}




