package com.example.margonari.fdrive;

import android.content.Context;
import android.content.SharedPreferences;

import org.slf4j.LoggerFactory;

/**
 * Created by luciano on 11/11/15.
 */
public class Database {

    private static Database instance = null;
    private Context context = null;

    private Database(){
        context = MainActivity.mainContext;
    }

    public static Database getInstance(){
        if(instance == null) instance = new Database();
        return instance;
    }

    public void put(String key,String value){
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);
        LoggerFactory.getLogger(getClass()).info("New " + key + " key, value: " + value);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String get(String key,String defaultReturnValue){
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);
        return prefs.getString(key,defaultReturnValue); //defaultReturnValue is returned if db doesn't have key
    }


}
