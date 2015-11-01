package com.example.margonari.fdrive.requests;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.margonari.fdrive.DriveActivity;
import com.example.margonari.fdrive.LogInActivity;
import com.example.margonari.fdrive.R;
import com.example.margonari.fdrive.RegistrationActivity;

import java.io.File;
import java.net.URI;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by luciano on 18/10/15.
 */
public class RequestMaker {


    private static String baseUrl = "http://192.168.0.1:8000";

    public static void setIp(String newIP){

       baseUrl = newIP;

    }



    public static void uploadFile(Context context,Uri uri,String description){

        FileUploadService client = ServiceGenerator.createService(FileUploadService.class,baseUrl);

        //Create typedFile to send
        ContentResolver contentResolver = context.getContentResolver();
        String fileType = contentResolver.getType(uri);
        TypedFile file = new TypedFile(fileType,new File(uri.toString()));

        client.uploadFile(file, description, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }


    public static void signUp(String email,String password){


        UserSignUpService client = ServiceGenerator.createService(UserSignUpService.class,baseUrl);


        client.registerUser(email,password,new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                Log.d("test","Called" + answer.result);
                RegistrationActivity.requestAnswer = answer;
                RegistrationActivity.onSuccess();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public static void loadFile(String email,String token,int fileId){


        LoadFileService client = ServiceGenerator.createService(LoadFileService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.loadFile(email, token, fileId, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", String.valueOf(answer.result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public static void logIn(String email,String password){


        UserLoginService client = ServiceGenerator.createService(UserLoginService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.loginUser(email, password, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                Log.d("test","Called" + answer.result + answer.token);
                LogInActivity.requestAnswer = answer;
                LogInActivity.onSuccess();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public static void deleteFile(String email,String token,int fileId){


        DeleteFileService client = ServiceGenerator.createService(DeleteFileService.class,baseUrl);

        DeleteFileBody body = new DeleteFileBody();
        body.email = email;
        body.token = token;
        body.fileId = fileId;

        // Fetch and print a list of the contributors to this library.
        client.deleteFile(body, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", String.valueOf(answer.result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }


    public static void saveFile(String email,String token,String fileName,String fileExtension,String owner,List<String> tags){


        SaveFileService client = ServiceGenerator.createService(SaveFileService.class,baseUrl);

        SaveFileBody body = new SaveFileBody();
        body.email = email;
        body.token = token;
        body.name = fileName;
        body.extension = fileExtension;
        body.owner = owner;
        body.tags = tags;

        // Fetch and print a list of the contributors to this library.
        client.saveFile(body, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", answer.errors.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }



}
