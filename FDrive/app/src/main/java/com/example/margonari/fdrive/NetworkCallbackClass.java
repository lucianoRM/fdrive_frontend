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


    public void onLogoutSuccess(){
        callback.onLogoutSuccess();
    }
    public void onGetFileSuccess(FileMetadata file){
        callback.onGetFileSuccess(file);
    }
    public void onGetUserFilesSuccess(GetUserFilesAnswer answer){
        callback.onGetUserFilesSuccess(answer);
    }

    public void onUploadFileSuccess(){
        callback.onUploadFileSuccess();
    }
    public void onRequestFailure(List<String> errors){
        callback.onRequestFailure(errors);
    }
    public void onConnectionError(){
        callback.onConnectionError();
    }
    public void onFileUploadProgress(long progress) {callback.onFileUploadProgress(progress);}
    public void onDeleteFileSuccess() {callback.onDeleteFileSuccess();}
    public void onSaveFileSuccess(int id) {callback.onSaveFileSuccess(id);}
    public void onCreateFolderSuccess() {callback.onCreateFolderSuccess();}

    public interface NetworkCallback {
        public void onLogoutSuccess();
        public void onGetFileSuccess(FileMetadata file);
        public void onGetUserFilesSuccess(GetUserFilesAnswer answer);
        public void onUploadFileSuccess();
        public void onDeleteFileSuccess();
        public void onSaveFileSuccess(int id);
        public void onCreateFolderSuccess();

        public void onRequestFailure(List<String> errors);

        public void onConnectionError();

        public void onFileUploadProgress(long progress);
    }



}
