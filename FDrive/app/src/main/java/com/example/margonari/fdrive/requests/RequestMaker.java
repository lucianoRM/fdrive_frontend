package com.example.margonari.fdrive.requests;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.margonari.fdrive.DriveActivity;
import com.example.margonari.fdrive.FileMetadata;
import com.example.margonari.fdrive.LogInActivity;
import com.example.margonari.fdrive.NetworkCallbackClass;
import com.example.margonari.fdrive.R;
import com.example.margonari.fdrive.RegistrationActivity;
import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;
import com.example.margonari.fdrive.requests.Answers.LoginAnswer;
import com.example.margonari.fdrive.requests.Answers.SimpleRequestAnswer;

import java.io.File;
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


    public static RequestMaker getInstance(Context context){
        if(instance == null) instance = new RequestMaker();
        //retrieve saved ip
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.sharedConf), Context.MODE_PRIVATE);
        String ipString = prefs.getString("ip", context.getResources().getString(R.string.baseURL));
        instance.setIp(ipString);
        return instance;
    }

    public static void setIp(String newIP){

       baseUrl = newIP;

    }


    public void uploadFile(Context context,Uri uri,String description, final NetworkCallbackClass activityCallback){

        FileUploadService client = ServiceGenerator.createService(FileUploadService.class, baseUrl);

        //Create typedFile to send
        ContentResolver contentResolver = context.getContentResolver();
        String fileType = contentResolver.getType(uri);
        TypedFile file = new TypedFile(fileType,new File(uri.getPath()));

        client.uploadFile(file, description, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                activityCallback.networkMethod();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test","Error: " + error.toString());
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
                    RegistrationActivity.onRegistrationFailure("Error");
                }


            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public void loadFile(String email,String token,int fileId){


        LoadFileService client = ServiceGenerator.createService(LoadFileService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.loadFile(email, token, fileId, new Callback<FileMetadata>() {
            @Override
            public void success(FileMetadata answer, Response response) {
                DriveActivity.onLoadFileSuccess(answer);
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
                if (answer.result == true) {
                    LogInActivity.onLoginSuccess(answer.token);
                } else {
                    //login failure
                    LogInActivity.onLoginFailure("Login error");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public void deleteFile(String email,String token,int fileId){


        DeleteFileService client = ServiceGenerator.createService(DeleteFileService.class,baseUrl);

        DeleteFileBody body = new DeleteFileBody();
        body.email = email;
        body.token = token;
        body.fileId = fileId;

        // Fetch and print a list of the contributors to this library.
        client.deleteFile(body, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", String.valueOf(answer.result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }


    public void saveFile(String email,String token,String fileName,String fileExtension,String owner,List<String> tags){


        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        SaveFileBody body = new SaveFileBody();
        body.email = email;
        body.token = token;
        body.name = fileName;
        body.extension = fileExtension;
        body.owner = owner;
        body.tags = tags;

        // Fetch and print a list of the contributors to this library.
        client.saveFile(body, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", answer.errors.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public void getUserFiles(String email,String token,String path){

        GetUserFilesService client = ServiceGenerator.createService(GetUserFilesService.class,baseUrl);

        client.getUserFiles(email, token, path, new Callback<GetUserFilesAnswer>() {
            @Override
            public void success(GetUserFilesAnswer getUserFilesAnswer, Response response) {
                if (getUserFilesAnswer.result)
                    DriveActivity.onGetUserFilesSuccess(getUserFilesAnswer);
                else {
                    DriveActivity.onRequestFailure(getUserFilesAnswer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test", "Error: " + error.toString());
            }
        });

    }

    public void logout(String email,String token){

       UserLogoutService client = ServiceGenerator.createService(UserLogoutService.class,baseUrl);

        client.logoutUser(email, token, new Callback<SimpleRequestAnswer>() {
            @Override
            public void success(SimpleRequestAnswer answer, Response response) {
                if(answer.result) {
                    DriveActivity.onLogoutSuccess();
                }
                else {
                    DriveActivity.onRequestFailure(answer.errors);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                DriveActivity.onConnectionError();
            }
        });

    }






}
