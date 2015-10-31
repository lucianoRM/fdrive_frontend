package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.margonari.fdrive.requests.RequestMaker;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    public static View mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;
        mainView = (View) findViewById(android.R.id.content);


        //sets ip
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);
        String ip = prefs.getString("ip", getResources().getString(R.string.baseURL));
        RequestMaker.setIp(getResources().getString(R.string.baseURL));


    }

    public static void toRegisterActivity(View view) {
        mainContext.startActivity(new Intent(mainContext, RegistrationActivity.class));
    }

    public static void openDrive(View view){
        mainContext.startActivity(new Intent(mainContext, DriveActivity.class)); //Just to test
    }

    public static void toLogInActivity(View view) {
        mainContext.startActivity(new Intent(mainContext, LogInActivity.class));
    }

}
