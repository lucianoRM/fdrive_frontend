package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.TypedInputStream;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by luciano on 31/10/15.
 */
public interface FileUploadService {
    @Multipart
    @POST("/files/{id}/{version}/data")
    void uploadFile(@Part("upload") TypedInputStream file,@Query("email") String email, @Query("token") String token,@Path("id") int id, @Path("version") int version, Callback<SimpleRequestAnswer> callback);
}
