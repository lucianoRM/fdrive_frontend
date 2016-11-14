package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.GetUsersAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by luciano on 15/11/15.
 */
public interface GetUsersService {
    @GET("/users")
    void getUsers(@Query("email") String email,@Query("token") String token, Callback<GetUsersAnswer> callback);

}
