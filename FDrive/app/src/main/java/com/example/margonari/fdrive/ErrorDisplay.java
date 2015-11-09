package com.example.margonari.fdrive;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;

/**
 * Created by luciano on 09/11/15.
 */
public class ErrorDisplay {

    private static ErrorDisplay instance = null;
    public static ErrorDisplay getInstance(){
        if(instance == null) instance = new ErrorDisplay();
        return instance;
    }

    public void showMessage(final Context context,View view, final String message){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("MORE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(message);
                        alertDialog.show();
                    }
                })
                .setActionTextColor(Color.WHITE)
                .show();

    }





}