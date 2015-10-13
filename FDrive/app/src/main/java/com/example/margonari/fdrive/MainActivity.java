package com.example.margonari.fdrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void toRegisterActivity(View view) {
        startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
    }

    public void openDrive(View view){
        startActivity(new Intent(MainActivity.this, DriveActivity.class)); //Just to test
    }

    public void toLogInActivity(View view) {
        startActivity(new Intent(MainActivity.this, LogInActivity.class));
    }

}
