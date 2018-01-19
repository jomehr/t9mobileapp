package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;

/*
 * Created by Jan on 10.12.2017.
 */

public class Registration extends AppCompatActivity{

    //private static final String PREFER_NAME = "Registration";

    private EditText edit_email, edit_name, edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button btn_register = findViewById(R.id.register_registerBtn);
        TextView btn_login =  findViewById(R.id.register_loginBtn);
        edit_email = findViewById(R.id.register_inputEmail);
        edit_name = findViewById(R.id.register_inputName);
        edit_password = findViewById(R.id.register_inputPassword);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(Registration.this, "keine Internetverbindung", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validate()) {
                    onRegistrationFailed();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            registerParse();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });
    }


    private boolean validate() {
        boolean valid = true;

        String email = edit_email.getText().toString();
        String username = edit_name.getText().toString();
        String password = edit_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Gebe eine valide Email-Adresse ein");
            valid = false;
        } else {
            edit_email.setError(null);
        }

        if (username.isEmpty() || username.length() < 3 || username.length() > 10) {
            edit_name.setError("Name muss zwischen 3 und 10 Zeichen lang sein");
            valid = false;
        } else {
            edit_name.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            edit_password.setError("Passwort muss zwischen 4 und 15 Zeichen lang sein");
            valid = false;
        } else {
            edit_password.setError(null);
        }

        return valid;
    }

    private void registerParse () {

        final String email = edit_email.getText().toString();
        final String username = edit_name.getText().toString();
        final String password = edit_password.getText().toString();

        ParseServer ps = ParseServer.getInstanceWithPublicReadAccess(Registration.this);
        ps.registerUser(this, email, username, password, this);
    }

    private void onRegistrationFailed() {
        Toast.makeText(this, "Registrierung fehlgeschlagen",Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
