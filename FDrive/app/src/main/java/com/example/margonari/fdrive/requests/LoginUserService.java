package com.example.margonari.fdrive.requests;

import java.util.List;
import java.util.Map;


import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.Callback;
import retrofit.http.Path;

/**
 * Created by luciano on 16/10/15.
 */
public interface LoginUserService {
    @GET("/login?email={email}&password={password}")
    void loginUser(@Path("email") String email,@Path("password") String password,Callback<Response> callback);
    }

