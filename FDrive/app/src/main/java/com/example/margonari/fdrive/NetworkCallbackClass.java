package com.example.margonari.fdrive;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;

import java.util.List;

/**
 * Created by luciano on 10/11/15.
 */
public class NetworkCallbackClass {

    NetworkCallback callback;

    public NetworkCallbackClass(NetworkCallback callback) {
        this.callback = callback;
    }

    public void networkMethod() {
        String message = "This came from YourCallbackClass";
        callback.onUploadFileSuccess(message);
    }

    public void onLogoutSuccess(){
        callback.onLogoutSuccess();
    }
    public void onLoadFileSuccess(FileMetadata file){
        callback.onLoadFileSuccess(file);
    }
    public void onGetUserFilesSuccess(GetUserFilesAnswer answer){
        callback.onGetUserFilesSuccess(answer);
    }

    public void onUploadFileSuccess(String message){
        callback.onUploadFileSuccess(message);
    }
    public void onRequestFailure(List<String> errors){
        callback.onRequestFailure(errors);
    }
    public void onConnectionError(){
        callback.onConnectionError();
    }

    public interface NetworkCallback {
        public void onLogoutSuccess();
        public void onLoadFileSuccess(FileMetadata file);
        public void onGetUserFilesSuccess(GetUserFilesAnswer answer);
        public void onUploadFileSuccess(String message);
        public void onRequestFailure(List<String> errors);
        public void onConnectionError();
    }



}
