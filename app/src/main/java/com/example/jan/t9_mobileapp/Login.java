package com.example.jan.t9_mobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jan on 07.12.2017.
 */

public class Login extends AppCompatActivity {
    private static final String PREFER_NAME = "Registration";
    private SharedPreferences sharedPreferences;

    Button btn_login;
    TextView btn_register;
    EditText edit_email,edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button)findViewById(R.id.login_loginBtn);
        btn_register = (TextView) findViewById(R.id.login_registerBtn);
        edit_email = (EditText)findViewById(R.id.login_inputEmail);
        edit_password = (EditText)findViewById(R.id.login_inputPassword);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        sharedPreferences = getSharedPreferences(PREFER_NAME, 0);
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        String prefEmail = null;
        String prefPw = null;

        if (sharedPreferences.contains("Email")) {
            prefEmail = sharedPreferences.getString("Email", null);
        }
        if (sharedPreferences.contains("Passwort")) {
            prefPw = sharedPreferences.getString("Passwort", null);
        }
        if (email.equals(prefEmail) && password.equals(prefPw)) {
            startActivity(new Intent(getApplicationContext(), Homescreen.class));
            finish();
        }else {
            onLoginFailed();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Gebe eine valide Email-Adresse ein");
            valid = false;
        } else {
            edit_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            edit_password.setError("Passwort muss zwischen 4 und 15 Zeichen lang sein");
            valid = false;
        } else {
            edit_password.setError(null);
        }

        return valid;
    }

    private void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Falsche Login-Daten",Toast.LENGTH_SHORT).show();
    }
}
