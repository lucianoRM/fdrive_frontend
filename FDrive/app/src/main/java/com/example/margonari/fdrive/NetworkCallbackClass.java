package com.example.margonari.fdrive;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;

import java.util.List;
import java.util.Map;

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
    public void onSearchSuccess(Map<Integer,String> files) {callback.onSearchSuccess(files);};
    public void search(String typeOfSearch,String element) {callback.search(typeOfSearch, element);}
    public void addTag(String newTag) {callback.addTag(newTag);}
    public void createFolder(String newFolder) {callback.createFolder(newFolder);}
    public void renameFile(String newName) {callback.renameFile(newName);}
    public void onMetadataUploadSuccess() {callback.onMetadataUploadSuccess();}
    public void onShareSuccess() {callback.onShareSuccess();}
    public void onGetUsersForSharingSuccess(List<String> users,String folderName) {callback.onGetUsersForSharingSuccess(users, folderName);}
    public void share(List<String> selectedUsers) {callback.share(selectedUsers);}
    public void onNewVersionSaveSuccess(int version) {callback.onNewVersionSaveSuccess(version);}
    public void unshare(List<String> selectedUsers) {callback.unshare(selectedUsers);}
    public void downloadFileVersion(int selectedVersion){ callback.downloadFileVersion(selectedVersion);}
    public void openShareFolderDialog(String folderName){ callback.openShareFolderDialog(folderName);}
    public void renameFolder(String oldname,String newName) {callback.renameFolder(oldname, newName);}
    public void getUsers(String folderName) {callback.getUsers(folderName);}
    public void shareFolder(List<String> selectedUsers,String folderName) {callback.shareFolder(selectedUsers,folderName);}





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
        public void onSearchSuccess(Map<Integer,String> files);
        public void onMetadataUploadSuccess();
        public void onShareSuccess();
        public void onGetUsersForSharingSuccess(List<String> users,String folderName);
        public void onNewVersionSaveSuccess(int version);

        //On failure methods
        public void onRequestFailure(List<String> errors);


        //On connection error methods
        public void onConnectionError();


        //On progress methods
        public void onFileUploadProgress(long progress);
        public void onFileDownloadProgress(long progress);


        //Non network related methods
        public void search(String typeOfSearch,String element);
        public void share(List<String> selectedUsers);
        public void shareFolder(List<String> selectedUsers,String folderName);
        public void openShareFolderDialog(String folderName);
        public void renameFolder(String oldname,String newName);
        public void unshare(List<String> selectedUsers);
        public void addTag(String newTag);
        public void getUsers(String folderName);
        public void createFolder(String newFolder);
        public void renameFile(String newName);
        public void downloadFileVersion(int selectedVersion);
    }



}
