package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.example.margonari.fdrive.requests.RequestMaker;

public class MainActivity extends AppCompatActivity {

    public static Context mainContext;
    public static View mainView;
    private WebView mainScreenWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainContext = this;
        mainView = (View) findViewById(android.R.id.content);
        mainScreenWebView = (WebView) findViewById(R.id.mainScreenWebView);
        mainScreenWebView.loadUrl("file:///android_asset/nextbit-cloud-transparent.gif");
        mainScreenWebView.getSettings().setLoadWithOverviewMode(true);
        mainScreenWebView.getSettings().setUseWideViewPort(true);
        mainScreenWebView.setBackgroundColor(Color.TRANSPARENT);


        setToolbar();


        loginIfPossible();

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


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_action_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
    }


    //Checks if there is information to login, if positive, it logs automatically. Doesn't actually sends a login request, just enters the drive. If information is old(e.g: token out of date), it will logout automatically while in DriveActivity
    private void loginIfPossible(){
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String token = prefs.getString(email+".token","");


        if(!email.equals("") && !token.equals("")){
            openDrive(mainView);
        }


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
                startActivity(new Intent(MainActivity.this,ConfigurationActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
