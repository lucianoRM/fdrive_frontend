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
    public void onFileDownloadProgress(long progress) {callback.onFileDownloadProgress(progress);}
    public void onFileDownloadSuccess() {callback.onFileDownloadSuccess();}
    public void onSearchSuccess(List<Integer> files) {callback.onSearchSuccess(files);};
    public void search(String typeOfSearch,String element) {callback.search(typeOfSearch, element);}
    public void addTag(String newTag) {callback.addTag(newTag);}
    public void createFolder(String newFolder) {callback.createFolder(newFolder);}
    public void renameFile(String newName) {callback.renameFile(newName);}




    public interface NetworkCallback {

        //On Success methods
        public void onLogoutSuccess();
        public void onGetFileSuccess(FileMetadata file);
        public void onGetUserFilesSuccess(GetUserFilesAnswer answer);
        public void onUploadFileSuccess();
        public void onDeleteFileSuccess();
        public void onSaveFileSuccess(int id);
        public void onCreateFolderSuccess();
        public void onFileDownloadSuccess();
        public void onSearchSuccess(List<Integer> files);

        //On failure methods
        public void onRequestFailure(List<String> errors);


        //On connection error methods
        public void onConnectionError();


        //On progress methods
        public void onFileUploadProgress(long progress);
        public void onFileDownloadProgress(long progress);


        //Non network related methods
        public void search(String typeOfSearch,String element);
        public void addTag(String newTag);
        public void createFolder(String newFolder);
        public void renameFile(String newName);
    }



}
