package com.example.margonari.fdrive.requests;

import com.example.margonari.fdrive.requests.Answers.SaveFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by luciano on 18/10/15.
 */

class SaveFileBody{
    public String email;
    public String token;
    public String name;
    public String extension;
    public String owner;
    public List<String> tags;
    public int size;
    public String path;

}

public interface SaveFileService {
    @POST("/files")
    void saveFile(@Body SaveFileBody body, Callback<SaveFileAnswer> callback);
}
