package com.example.margonari.fdrive.requests;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by luciano on 31/10/15.
 */
public interface FileUploadService {
    @Multipart
    @POST("/filesupload")
    void uploadFile(@Part("upload") TypedFile file, @Part("description") String description, Callback<RequestAnswer> callback);
}
