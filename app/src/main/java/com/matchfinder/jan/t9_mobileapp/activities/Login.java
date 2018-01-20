package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = findViewById(R.id.login_loginBtn);
        TextView btn_forgotPassword = findViewById(R.id.login_forgotPasswordBtn);
        TextView btn_register = findViewById(R.id.login_registerBtn);
        edit_username = findViewById(R.id.login_inputUsername);
        edit_password = findViewById(R.id.login_inputPassword);

        ParseServer.getInstance(this);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Homescreen.class));
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(Login.this, "keine Internetverbindung", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginParse();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        btn_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this, "wird noch implementiert", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginParse() {
        ParseServer ps = ParseServer.getInstance(Login.this);
        ps.loginUser(this, edit_username.getText().toString(), edit_password.getText().toString(), this);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.close_app)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }
}
