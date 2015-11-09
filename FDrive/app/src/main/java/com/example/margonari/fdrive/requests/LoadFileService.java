package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.requests.Answers.RequestAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by luciano on 18/10/15.
 */
public interface LoadFileService {
    @GET("/users")
    void loadFile(@Query("email") String email,@Query("token") String password,@Query("id") int id, Callback<FileMetadata> callback);

}
