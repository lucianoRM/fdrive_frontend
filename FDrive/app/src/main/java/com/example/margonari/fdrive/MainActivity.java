package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        /*mainScreenWebView = (WebView) findViewById(R.id.mainScreenWebView);
        mainScreenWebView.loadUrl("file:///android_asset/giphy.gif");
        mainScreenWebView.getSettings().setLoadWithOverviewMode(true);
        mainScreenWebView.getSettings().setUseWideViewPort(true);*/


        setToolbar();


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
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
