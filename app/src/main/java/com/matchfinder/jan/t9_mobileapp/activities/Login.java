package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.parse.ParseUser;

/*
 * Created by Jan on 07.12.2017.
 */

public class Login extends AppCompatActivity {

    private EditText edit_username,edit_password;
    public ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("Registration", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean remember = sharedPreferences.getBoolean("Erinnerung", false);
        if (remember) {
            startActivity(new Intent(Login.this, Homescreen.class));
            finish();
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        }

        Button btn_login = findViewById(R.id.login_loginBtn);
        TextView btn_register = findViewById(R.id.login_registerBtn);
        edit_username = findViewById(R.id.login_inputUsername);
        edit_password = findViewById(R.id.login_inputPassword);
        CheckBox check_remember = findViewById(R.id.login_checkBox);
        progressbar =  findViewById(R.id.login_progressBar);

        if(check_remember.isChecked()) {
            editor.putBoolean("Erinnerung", true);
            editor.apply();
        } else {
            editor.putBoolean("Erinnerung", false);
            editor.apply();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                if (!isNetworkAvailable()) {
                    Toast.makeText(Login.this, "keine Internetverbindung", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loginParse();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }


    private void loginParse() {
        ParseServer ps = ParseServer.getInstance(Login.this);
        ps.loginUser(getApplicationContext(), edit_username.getText().toString(), edit_password.getText().toString(), this);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ParseUser.getCurrentUser() == null) {
            progressbar.setVisibility(View.INVISIBLE);
        }
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Falsche Login-Daten",Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
