package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.LoginAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by luciano on 15/11/15.
 */


class ShareFileBody{
    public String email;
    public String token;
    public int id;
    public List<String> users;

}

class ShareFolderBody{
    public String email;
    public String token;
    public String path;
    public List<String> users;

}


public interface ShareFileService {
    @POST("/share")
    void share(@Body ShareFileBody body,Callback<SimpleRequestAnswer> callback);

    @PUT("/unshare")
    void unshare(@Body ShareFileBody body,Callback<SimpleRequestAnswer> callback);

    @POST("/share/folder")
    void shareFolder(@Body ShareFolderBody body,Callback<SimpleRequestAnswer> callback);

}
