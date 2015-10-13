package com.example.margonari.fdrive;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText textName, textSurname, textEmail, textPassword, textConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textName = (EditText) findViewById(R.id.editTextName);
        textName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textName.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                } else {
                    textName.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });

        textSurname = (EditText) findViewById(R.id.editTextSurname);
        textSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textSurname.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                } else {
                    textSurname.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });

        textEmail = (EditText) findViewById(R.id.editTextEmail);
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

        textPassword = (EditText) findViewById(R.id.editTextPassword);
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

        textConfirmPassword = (EditText) findViewById(R.id.editTextPasswordConf);
        textConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textConfirmPassword.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                } else {
                    textConfirmPassword.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });



    }
/*
    public void clickedTextName(View view) {
        EditText textName = (EditText) findViewById(R.id.editTextName);
        textName.setText("");
        textName.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
    }
*/

}
