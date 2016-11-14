package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.margonari.fdrive.R;
import com.example.margonari.fdrive.requests.RequestMaker;

import org.slf4j.LoggerFactory;

/**
 * Created by luciano on 31/10/15.
 */
public class ConfigurationActivity extends Activity {

    private static EditText ipView;
    private static String ipString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configurations_layout);

        ipView = (EditText)findViewById(R.id.ip);

        //Get saved ip and store it in ip
        ipString = Database.getInstance().get("ip",getResources().getString(R.string.baseURL));
        ipView.setText(ipString);


    }


    public void saveIp(View view){

        ipString = ipView.getText().toString();
        Database.getInstance().put("ip",ipString);

        LoggerFactory.getLogger(getClass()).info("New ip set to" + ipString);

        //Closes configurations
        finish();

    }



}
