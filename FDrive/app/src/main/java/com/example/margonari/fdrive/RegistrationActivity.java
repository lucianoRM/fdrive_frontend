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
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText textName, textSurname, textEmail, textPassword, textConfirmPassword;
    private TextView labelErrorName, labelErrorSurname, labelErrorEmail, labelErrorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        labelErrorName = (TextView) findViewById(R.id.textViewErrorName);
        labelErrorSurname = (TextView) findViewById(R.id.textViewErrorSurname);
        labelErrorEmail = (TextView) findViewById(R.id.textViewErrorEmail);
        labelErrorPassword = (TextView) findViewById(R.id.textViewErrorPassword);

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
        textConfirmPassword = (EditText) findViewById(R.id.editTextPasswordConf);
        textPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    textConfirmPassword.setVisibility(View.VISIBLE);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void checkFields(View view) {
        String name = textName.getText().toString();
        String surname = textSurname.getText().toString();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        String passwordConfirm = textConfirmPassword.getText().toString();


        if (name.equals("")) {
            labelErrorName.setVisibility(View.VISIBLE);
            textName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            labelErrorName.setVisibility(View.INVISIBLE);
        }

        if (surname.equals("")) {
            labelErrorSurname.setVisibility(View.VISIBLE);
            textSurname.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            labelErrorSurname.setVisibility(View.INVISIBLE);
        }

        if (email.equals("")) {
            labelErrorEmail.setVisibility(View.VISIBLE);
            textEmail.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            labelErrorEmail.setVisibility(View.INVISIBLE);
        }

        if (password.length() < 6) {
            labelErrorPassword.setVisibility(View.VISIBLE);
            labelErrorPassword.setText("The password must have at least 6 characters");
            textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            labelErrorPassword.setVisibility(View.INVISIBLE);
            if (! password.contentEquals(passwordConfirm)) {
                labelErrorPassword.setVisibility(View.VISIBLE);
                labelErrorPassword.setText("The password confirmation does not match");
                textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }
        }
    }


}
