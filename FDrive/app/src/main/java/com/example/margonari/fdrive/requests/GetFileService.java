package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.requests.Answers.GetFileAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luciano on 18/10/15.
 */
public interface GetFileService {
    @GET("/files/{id}/metadata")
    void getFile(@Query("email") String email,@Query("token") String password,@Path("id") int id, Callback<GetFileAnswer> callback);

}
