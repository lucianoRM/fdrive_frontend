package com.example.margonari.fdrive.requests;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;
import com.example.margonari.fdrive.requests.Answers.LoginAnswer;
import com.example.margonari.fdrive.requests.Answers.SaveFileAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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


    public void uploadFile(final NetworkCallbackClass activityCallback,TypedInputStream inputStream,String email,String token,int fileId){

        FileUploadService client = ServiceGenerator.createService(FileUploadService.class, baseUrl);

        client.uploadFile(inputStream, email, token, fileId, new Callback<SimpleRequestAnswer>() {
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
        client.getFile(email, token, fileId, new Callback<FileMetadata>() {
            @Override
            public void success(FileMetadata answer, Response response) {
                activityCallback.onGetFileSuccess(answer);
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
                    activityCallback.onDeleteFileSuccess();
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

        SaveFileBody body = new SaveFileBody();
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

    public void downloadFile(final NetworkCallbackClass activityCallback,String email,String token,int fileId){

        FileDownloadService client = ServiceGenerator.createService(FileDownloadService.class,baseUrl);

        client.downloadFile(email, token, fileId, new Callback<Response>() {
            @Override
            public void success(final Response answer, final Response response) {
                new Thread(new Runnable() {
                    public void run() {
                        Log.d("test", "Download success");
                        File targetFile = new File("path");
                        targetFile.deleteOnExit();
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;

                                try {
                                    inputStream = response.getBody().in();
                                    byte[] buff = new byte[4096];
                                    long downloaded = 0;
                                    long target = response.getBody().length();

                                    Log.d("test", "File size is: " + Long.toString(target));
                                    OutputStream outStream = new FileOutputStream(targetFile);
                                    while (true) {

                                        int read = inputStream.read(buff);
                                        if (read == -1) {
                                            break;
                                        }
                                        outStream.write(buff,0,read);
                                        //write buff
                                        downloaded += read;
                                        Log.d("test",Long.toString(read));
                                        Log.d("test",Long.toString(downloaded));

                                    }

                                    outStream.flush();

                                } catch (IOException ignore) {
                                    ignore.printStackTrace();
                                } finally {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test",error.toString());
                activityCallback.onConnectionError();
            }
        });

    }

    public void createFolder(final NetworkCallbackClass activityClass,String email,String token,String path,String folderName){

        CreateFolderService client = ServiceGenerator.createService(CreateFolderService.class, baseUrl);

        client.createFolder(email, token, path,folderName, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if(answer.result) {
                    activityClass.onCreateFolderSuccess();
                }
                else{
                    activityClass.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                activityClass.onConnectionError();
            }
        });


    }






}
