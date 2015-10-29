package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    public static View mainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;
        mainView = (View) findViewById(android.R.id.content);


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
