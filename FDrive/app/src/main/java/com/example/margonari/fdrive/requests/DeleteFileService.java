package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;

/**
 * Created by luciano on 18/10/15.
 */

class DeleteFileBody{
    public String email;
    public String token;
    public int fileId;

}

public interface DeleteFileService {
    @DELETE("/files")
    void deleteFile(@Body DeleteFileBody body, Callback<SimpleRequestAnswer> callback);

}
