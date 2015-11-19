package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.TypedInputStream;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by luciano on 13/11/15.
 */
public interface CreateFolderService {
    @POST("/folders")
    void createFolder(@Query("email") String email, @Query("token") String token,@Query("path") String path,@Query("name") String folderName, Callback<SimpleRequestAnswer> callback);
}
