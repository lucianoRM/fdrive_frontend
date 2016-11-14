package com.example.margonari.fdrive;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;

import java.util.List;

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
        if(message.length() > 56) {
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
        }else{
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .setActionTextColor(Color.WHITE)
                    .show();
        }

    }

    public void showMessages(final Context context,View view, final List<String> messages){
        String wholeMessage = "";
        for(int i = 0;i < messages.size(); i++){
            wholeMessage+=messages.get(i);
            if(i != messages.size() - 1) wholeMessage+="\n";
        }
        showMessage(context,view,wholeMessage);
    }






}
