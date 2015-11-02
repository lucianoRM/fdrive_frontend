package com.example.margonari.fdrive;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.RequestAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;

public class LogInActivity extends AppCompatActivity {

    private static EditText textEmail, textPassword;
    private static Button buttonLogin;
    private static ProgressBar progressBar;
    private static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        //Instantiates prefenreces
        preferences = getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);


        //Sets the progress bar as invisible
        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        buttonLogin = (Button) findViewById(R.id.buttonLogIn);

        setToolbar();


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


        //Gets persisted information and loads the fields
        textEmail.setText(preferences.getString("email",""));


    }

    public void requestLogin(View view){

        //disable button
        toggleUi(false);
        buttonLogin.setBackgroundResource(R.color.buttonsColor);
        RequestMaker.getInstance().logIn(textEmail.getText().toString(), textPassword.getText().toString());

    }



    public static void onLoginSuccess(String token){

        //Persist token
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("token", token);
        edit.commit();

        //Open drive
        MainActivity.openDrive(MainActivity.mainView);

        //Sets the button back to clickable
        toggleUi(true);

    }

    public static void onLoginFailure(String error) {

        buttonLogin.setBackgroundResource(R.color.errorRed);
        //Sets the button back to clickable
        toggleUi(true);
    }

    public static void onConnectionError(String error) {

    }

    private static void toggleUi(boolean enable){
        if(enable){
            //Sets the button back to clickable
            buttonLogin.setClickable(true);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }else{
            //Prevents the button from being clicked more than once
            buttonLogin.setClickable(false);

            //show progress bar
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_action_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_action_bar_settings:
                startActivity(new Intent(LogInActivity.this,ConfigurationActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}



