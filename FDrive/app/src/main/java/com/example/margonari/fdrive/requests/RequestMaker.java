package com.example.margonari.fdrive.requests;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.margonari.fdrive.R;

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
        client.registerUser(email,password,new Callback<H>() {
            @Override
            public void success(H r, Response response) {
                //What to do when success
                Log.d("Test",String.valueOf(r.result));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Test", error.toString());
            }
        });

    }
}
