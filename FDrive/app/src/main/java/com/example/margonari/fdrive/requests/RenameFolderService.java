package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.GetFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by luciano on 17/11/15.
 */
public interface RenameFolderService {
    @POST("/renamefolder")
    void renameFolder(@Query("email") String email,@Query("token") String password,@Query("path") String path,@Query("nameold") String oldname,@Query("namenew") String newname, Callback<SimpleRequestAnswer> callback);

}