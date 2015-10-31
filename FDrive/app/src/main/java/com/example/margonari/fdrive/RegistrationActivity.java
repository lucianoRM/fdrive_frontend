package com.example.margonari.fdrive;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.margonari.fdrive.requests.RequestAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;

import retrofit.client.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText textName, textSurname, textEmail, textPassword, textConfirmPassword;
    private TextView labelErrorName, labelErrorSurname, labelErrorEmail, labelErrorPassword;
    private static Button buttonNewAccount;
    private static ProgressBar progressBar;
    public static RequestAnswer requestAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Sets the progress bar as invisible
        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);


        buttonNewAccount = (Button) findViewById(R.id.buttonNewAccount);

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
    public boolean checkFields(View view) {
        String name = textName.getText().toString();
        String surname = textSurname.getText().toString();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        String passwordConfirm = textConfirmPassword.getText().toString();


        if (name.equals("")) {
            labelErrorName.setVisibility(View.VISIBLE);
            textName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            return false;
        } else {
            labelErrorName.setVisibility(View.INVISIBLE);
        }

        if (surname.equals("")) {
            labelErrorSurname.setVisibility(View.VISIBLE);
            textSurname.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            return false;
        } else {
            labelErrorSurname.setVisibility(View.INVISIBLE);
        }

        if (email.equals("")) {
            labelErrorEmail.setVisibility(View.VISIBLE);
            textEmail.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            return false;
        } else {
            labelErrorEmail.setVisibility(View.INVISIBLE);
        }

        if (password.length() < 6) {
            labelErrorPassword.setVisibility(View.VISIBLE);
            labelErrorPassword.setText("The password must have at least 6 characters");
            textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            return false;
        } else {
            labelErrorPassword.setVisibility(View.INVISIBLE);
            if (! password.contentEquals(passwordConfirm)) {
                labelErrorPassword.setVisibility(View.VISIBLE);
                labelErrorPassword.setText("The password confirmation does not match");
                textPassword.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                return false;
            }
        }
        return true;
    }

    public void requestRegister(View view){

        //Prevents the button from being clicked more than once
        toggleUi(false);

        boolean isOk = checkFields(view);
        if(isOk) {
            RequestMaker.signUp(getString(R.string.baseURL), textEmail.getText().toString(), textPassword.getText().toString());
        }else{
            //If the fields are wrong, enables the button, there is no request
            toggleUi(true);
        }
    }


    public static void onSuccess(){

        if(requestAnswer != null) {
            if (requestAnswer.result == false) {
                Log.d("test",String.valueOf(requestAnswer.result) + requestAnswer.errors.toString());
                buttonNewAccount.setBackgroundResource(R.color.errorRed);
            }else{
                Log.d("test", String.valueOf(requestAnswer.result) + requestAnswer.errors.toString());
                MainActivity.toLogInActivity(MainActivity.mainView);
            }
        }

        //Sets the button back to clickable
        toggleUi(true);

    }

    private static void toggleUi(boolean enable){
        if(enable){
            //Sets the button back to clickable
            buttonNewAccount.setClickable(true);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }else{
            //Prevents the button from being clicked more than once
            buttonNewAccount.setClickable(false);

            //show progress bar
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

    }

}
