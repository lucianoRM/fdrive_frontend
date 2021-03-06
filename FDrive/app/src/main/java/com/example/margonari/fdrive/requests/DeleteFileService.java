package com.example.margonari.fdrive.requests;

import android.telecom.Call;

import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luciano on 18/10/15.
 */


public interface DeleteFileService {
    @DELETE("/files/{id}")
    void deleteFile(@Query("email") String email,@Query("token") String token,@Query("path") String path,@Path("id") int id, Callback<SimpleRequestAnswer> callback);

    @GET("/recoverfile")
    void recoverFile(@Query("email") String email,@Query("token") String token,@Query("id") int id,Callback<SimpleRequestAnswer> callback);

}
