package com.example.margonari.fdrive;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.RequestAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;

public class LogInActivity extends AppCompatActivity {

    private static EditText textEmail, textPassword;
    private static Button buttonLogin;
    public static RequestAnswer requestAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        buttonLogin = (Button) findViewById(R.id.buttonLogIn);

        textEmail = (EditText) findViewById(R.id.editTextEmailLogIn);
        textEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textEmail.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                } else {
                    textEmail.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });

        textPassword = (EditText) findViewById(R.id.editTextPasswordLogIn);
        textPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                } else {
                    textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });

    }

    public void requestLogin(View view){

        //Prevents the button from being clicked more than once
        buttonLogin.setClickable(false);

        RequestMaker.logIn(getString(R.string.baseURL), textEmail.getText().toString(), textPassword.getText().toString());

    }



    public static void onSuccess(){

        if(requestAnswer != null) {
            if (requestAnswer.result == false) {
                Log.d("test", String.valueOf(requestAnswer.result) + requestAnswer.errors.toString());
                buttonLogin.setBackgroundResource(R.color.errorRed);
            }else{
                Log.d("test", String.valueOf(requestAnswer.result) + requestAnswer.errors.toString());

                //Loads information into drive to make file requests
                DriveActivity.token = requestAnswer.token;
                DriveActivity.email = textEmail.getText().toString();
                MainActivity.openDrive(MainActivity.mainView);
            }
        }

        //Sets the button back to clickable
        buttonLogin.setClickable(true);

    }
}
