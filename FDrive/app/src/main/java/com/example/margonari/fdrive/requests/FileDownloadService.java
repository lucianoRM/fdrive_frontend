package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.TypedInputStream;
import com.example.margonari.fdrive.requests.Answers.DownloadFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;

/**
 * Created by luciano on 12/11/15.
 */
public interface FileDownloadService {
    @GET("/files/{id}/data")
    @Streaming
    void downloadFile(@Query("email") String email, @Query("token") String password, @Path("id") int id, Callback<Response> callback);
}
