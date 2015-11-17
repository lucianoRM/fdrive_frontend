package com.example.margonari.fdrive.requests;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Network;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.OpenableColumns;
import android.util.Log;

import com.example.margonari.fdrive.Database;
import com.example.margonari.fdrive.DriveActivity;
import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.LogInActivity;
import com.example.margonari.fdrive.NetworkCallbackClass;
import com.example.margonari.fdrive.R;
import com.example.margonari.fdrive.RegistrationActivity;
import com.example.margonari.fdrive.TypedInputStream;
import com.example.margonari.fdrive.requests.Answers.DownloadFileAnswer;
import com.example.margonari.fdrive.requests.Answers.GetFileAnswer;
import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;
import com.example.margonari.fdrive.requests.Answers.GetUsersAnswer;
import com.example.margonari.fdrive.requests.Answers.LoginAnswer;
import com.example.margonari.fdrive.requests.Answers.SaveFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SearchAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by luciano on 18/10/15.
 */
public class RequestMaker {


    private static String baseUrl;
    private static RequestMaker instance = null;


    public static RequestMaker getInstance(){
        if(instance == null) instance = new RequestMaker();
        //retrieve saved ip
        String ipString = Database.getInstance().get("ip","http://192.168.0.1:8000");
        instance.setIp(ipString);
        return instance;
    }

    public static void setIp(String newIP){

       baseUrl = newIP;

    }


    public void uploadFile(final NetworkCallbackClass activityCallback,TypedInputStream inputStream,String email,String token,int fileId, int version){

        FileUploadService client = ServiceGenerator.createService(FileUploadService.class, baseUrl);

        client.uploadFile(inputStream, email, token, fileId, version, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onUploadFileSuccess();
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }


            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });


    }


    public void signUp(String email,String password){


        UserSignUpService client = ServiceGenerator.createService(UserSignUpService.class,baseUrl);


        client.registerUser(email, password, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {

                //Registration successful
                if (answer.result == true) {
                    RegistrationActivity.onRegistrationSuccess();
                } else { //registration failure
                    RegistrationActivity.onRegistrationFailure(answer.errors);
                }


            }

            @Override
            public void failure(RetrofitError error) {

                RegistrationActivity.onConnectionError();
            }
        });

    }

    public void getFile(final NetworkCallbackClass activityCallback,String email,String token,int fileId){


        GetFileService client = ServiceGenerator.createService(GetFileService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.getFile(email, token, fileId, new Callback<GetFileAnswer>() {
            @Override
            public void success(GetFileAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onGetFileSuccess(answer.file);
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public void logIn(String email,String password){


        UserLoginService client = ServiceGenerator.createService(UserLoginService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.loginUser(email, password, new Callback<LoginAnswer>() {
            @Override
            public void success(LoginAnswer answer, Response response) {
                Log.d("test", "Called" + answer.result + answer.token);
                //login successful
                if (answer.result) {
                    LogInActivity.onLoginSuccess(answer.token);
                } else {
                    //login failure
                    LogInActivity.onLoginFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LogInActivity.onConnectionError();
            }
        });

    }

    public void deleteFile(final NetworkCallbackClass activityCallback,String email,String token,String path,int fileId){


        DeleteFileService client = ServiceGenerator.createService(DeleteFileService.class,baseUrl);


        // Fetch and print a list of the contributors to this library.
        client.deleteFile(email, token, path, fileId, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    if (answer.result) {
                        activityCallback.onDeleteFileSuccess();
                    } else {
                        activityCallback.onRequestFailure(answer.errors);
                    }
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });

    }


    public void saveFile(final NetworkCallbackClass activityCallback,String email,String token,String fileName,String fileExtension,String owner,long size,String path){


        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        NewFileBody body = new NewFileBody();
        body.email = email;
        body.token = token;
        body.name = fileName;
        body.extension = fileExtension;
        body.owner = owner;
        body.tags = new ArrayList<>();
        body.size = (int)(size/1000);
        body.path = path;

        // Fetch and print a list of the contributors to this library.
        client.saveFile(body, new Callback<SaveFileAnswer>() {
            @Override
            public void success(SaveFileAnswer answer, Response response) {
                //What to do when success
                if (answer.result) {
                    Log.d("test", "id: " + answer.fileID);
                    activityCallback.onSaveFileSuccess(answer.fileID);
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });

    }

    public void saveNewVersion(final NetworkCallbackClass activityCallback,String email,String token,String name,String extension,int id,String path,long size,List<String> tags,int version){

        NewVersionBody body = new NewVersionBody();
        body.email = email;
        body.token = token;
        body.name = name;
        body.extension = extension;
        body.version = version;
        body.tags = tags;
        body.path = path;
        body.size = (int)(size/1000);


        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.saveFile(body, id, new Callback<SaveFileAnswer>() {
            @Override
            public void success(SaveFileAnswer answer, Response response) {
                //What to do when success
                if (answer.result) {
                    activityCallback.onNewVersionSaveSuccess(answer.version);
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });

    }


    public void renameFile(final NetworkCallbackClass activityCallback,String email,String token,int id,String newName){

        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        RenameBody body = new RenameBody();
        body.email = email;
        body.token = token;
        body.name = newName;

        client.saveFile(body, id, new Callback<SaveFileAnswer>() {
            @Override
            public void success(SaveFileAnswer answer, Response response) {
                if(answer.result){
                    activityCallback.onMetadataUploadSuccess();
                }else{
                    activityCallback.onRequestFailure(answer.errors);
                }
            }


            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });


    }

    public void addTag(final NetworkCallbackClass activityCallback,String email,String token,int id,String newTag){

        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        AddTagBody body = new AddTagBody();
        body.email = email;
        body.token = token;
        body.tag = newTag;

        client.saveFile(body, id, new Callback<SaveFileAnswer>() {
            @Override
            public void success(SaveFileAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onMetadataUploadSuccess();
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });

    }

    public void getUserFiles(final NetworkCallbackClass activityCallback,String email,String token,String path){

        GetUserFilesService client = ServiceGenerator.createService(GetUserFilesService.class,baseUrl);

        client.getUserFiles(email, token, path, new Callback<GetUserFilesAnswer>() {
            @Override
            public void success(GetUserFilesAnswer getUserFilesAnswer, Response response) {
                if (getUserFilesAnswer.result)
                    activityCallback.onGetUserFilesSuccess(getUserFilesAnswer);
                else {
                    activityCallback.onRequestFailure(getUserFilesAnswer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test", "Error: " + error.toString());
            }
        });

    }

    public void logout(final NetworkCallbackClass activityCallback,String email,String token){

       UserLogoutService client = ServiceGenerator.createService(UserLogoutService.class,baseUrl);

        client.logoutUser(email, token, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onLogoutSuccess();
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });

    }

    public void downloadFile(final NetworkCallbackClass activityCallback,String email,String token,int fileId,final String fileName,final String fileExtension,final int version){

        FileDownloadService client = ServiceGenerator.createService(FileDownloadService.class,baseUrl);

        client.downloadFile(email, token, fileId, version, new Callback<Response>() {
            @Override
            public void success(Response answer, final Response response) {
                new Thread(new Runnable() {
                    public void run() {
                        File targetFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + fileName + fileExtension);
                        targetFile.deleteOnExit();
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;

                            try {
                                inputStream = response.getBody().in();
                                byte[] buff = new byte[4096];
                                long downloaded = 0;
                                long target = response.getBody().length();
                                OutputStream outStream = new FileOutputStream(targetFile);
                                while (true) {
                                    int read = inputStream.read(buff);
                                    if (read == -1) {
                                        break;
                                    }
                                    outStream.write(buff, 0, read);
                                    //write buff
                                    downloaded += read;
                                    activityCallback.onFileDownloadProgress((downloaded * 100) / target);
                                }
                                outStream.flush();

                            } catch (IOException ignore) {
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        } catch (IOException e) {
                        }
                        activityCallback.onFileDownloadSuccess();
                    }
                }).start();


            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }


        });

    }

    public void createFolder(final NetworkCallbackClass activityClass,String email,String token,String path,String folderName){

        CreateFolderService client = ServiceGenerator.createService(CreateFolderService.class, baseUrl);

        client.createFolder(email, token, path, folderName, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    activityClass.onCreateFolderSuccess();
                } else {
                    activityClass.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityClass.onConnectionError();
            }
        });


    }

    public void search(final NetworkCallbackClass activityCallback,String email,String token,String searchType,String element){

        SearchService client = ServiceGenerator.createService(SearchService.class,baseUrl);

        client.search(email, token, searchType, element, new Callback<SearchAnswer>() {
            @Override
            public void success(SearchAnswer answer, Response response) {

                Map<Integer, String> map = new HashMap<Integer, String>();

                List<SearchAnswer.File> files = new ArrayList<>();
                if (answer.content != null) {
                    files = answer.content.files;
                } else {
                    List<String> error = new ArrayList<String>();
                    error.add("No file found");
                    activityCallback.onRequestFailure(error);
                }

                for (int i = 0; i < files.size(); i++) {
                    SearchAnswer.File file = files.get(i);
                    map.put(file.id, file.path);
                }

                activityCallback.onSearchSuccess(map);
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });



    }

    public void shareFile(final NetworkCallbackClass activityCallback,String email,String token,int fileid,List<String> users){

        ShareFileService client = ServiceGenerator.createService(ShareFileService.class,baseUrl);

        ShareFileBody body = new ShareFileBody();
        body.email = email;
        body.token = token;
        body.id = fileid;
        body.users = users;

        client.share(body, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onShareSuccess();
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();

            }
        });



    }

    public void unshareFile(final NetworkCallbackClass activityCallback,String email,String token,int fileid,List<String> users){

        ShareFileService client = ServiceGenerator.createService(ShareFileService.class,baseUrl);

        ShareFileBody body = new ShareFileBody();
        body.email = email;
        body.token = token;
        body.id = fileid;
        body.users = users;

        client.unshare(body, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if (answer.result) {
                    activityCallback.onShareSuccess();
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();

            }
        });



    }


    public void getUsers(final NetworkCallbackClass activityCallback, final String email,String token){

        GetUsersService client = ServiceGenerator.createService(GetUsersService.class,baseUrl);

        client.getUsers(email, token, new Callback<GetUsersAnswer>() {
            @Override
            public void success(GetUsersAnswer answer, Response response) {
                if (answer.result) {
                    List<String> emails = new ArrayList<String>();
                    for (int i = 0; i < answer.users.size(); i++) {
                        emails.add(answer.users.get(i).email);
                    }
                    activityCallback.onGetUsersForSharingSuccess(emails);
                } else {
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });


    }


    public void renameFolder(final NetworkCallbackClass activityCallback,String email,String token,String path,String oldname,String newname){


        RenameFolderService client = ServiceGenerator.createService(RenameFolderService.class,baseUrl);

        client.renameFolder(email, token, path, oldname, newname, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if(answer.result){
                    activityCallback.onMetadataUploadSuccess();
                }else{
                    activityCallback.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityCallback.onConnectionError();
            }
        });



    }





}
