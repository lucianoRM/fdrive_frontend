package com.example.margonari.fdrive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.margonari.fdrive.R;
import com.example.margonari.fdrive.requests.RequestMaker;

/**
 * Created by luciano on 31/10/15.
 */
public class ConfigurationActivity extends Activity {

    private EditText ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configurations_layout);

        ip = (EditText)findViewById(R.id.ip);
        //Get saved ip and store it in ip
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);
        ip.setText(prefs.getString("ip",getResources().getString(R.string.baseURL)));
        RequestMaker.setIp(ip.getText().toString());



    }


    public void saveIp(View view){

        String ipString = ip.getText().toString();
        SharedPreferences prefs = this.getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("ip", ipString);
        edit.commit();


        //Saves ip into requestMaker
        RequestMaker.setIp(ipString);

        //Closes configurations
        finish();

    }

}
