package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.requests.Answers.SaveFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by luciano on 18/10/15.
 */

class NewVersionBody{

    public String email;
    public String token;
    public String name;
    public String extension;
    public int id;
    public List<String> tags;
    public int size;
    public String path;
    public int version;
    public boolean overwrite = false;
}

class NewFileBody{
    public String email;
    public String token;
    public String name;
    public String extension;
    public String owner;
    public List<String> tags;
    public int size;
    public String path;
}

class AddTagBody{

    public String email;
    public String token;
    public String tag;
    public int id;
}

class RenameBody{
    public String email;
    public String token;
    public String name;
    public int id;
}

public interface SaveFileService {
    @POST("/files")
    void saveFile(@Body NewFileBody body, Callback<SaveFileAnswer> callback);

    @POST("/files")
    void saveFile(@Body NewVersionBody body, Callback<SaveFileAnswer> callback);

    @PUT("/files")
    void saveFile(@Body AddTagBody body,Callback<SaveFileAnswer> callback);

    @PUT("/files")
    void saveFile(@Body RenameBody body,Callback<SaveFileAnswer> callback);

}
