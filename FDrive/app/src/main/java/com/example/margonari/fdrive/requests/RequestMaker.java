package com.example.margonari.fdrive.requests;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.margonari.fdrive.R;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luciano on 18/10/15.
 */
public class RequestMaker {

    public static void signUp(String baseUrl,String email,String password){


        UserSignUpService client = ServiceGenerator.createService(UserSignUpService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.registerUser(email,password,new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test",String.valueOf(answer.result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public static void loadFile(String baseUrl,String email,String token,int fileId){


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

    public static void logIn(String baseUrl,String email,String password){


        UserLoginService client = ServiceGenerator.createService(UserLoginService.class,baseUrl);

        // Fetch and print a list of the contributors to this library.
        client.loginUser(email, password, new Callback<RequestAnswer>() {
            @Override
            public void success(RequestAnswer answer, Response response) {
                //What to do when success
                Log.d("Test", "Result: " + String.valueOf(answer.result) + " Token: "  + answer.token);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }

    public static void deleteFile(String baseUrl,String email,String token,int fileId){


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


    public static void saveFile(String baseUrl,String email,String token,String fileName,String fileExtension,String owner,List<String> tags){


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
