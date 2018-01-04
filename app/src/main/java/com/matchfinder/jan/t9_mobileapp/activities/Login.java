package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;

/*
 * Created by Jan on 07.12.2017.
 */

public class Login extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText edit_email,edit_password;
    private CheckBox check_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("Registration", 0);
        editor = sharedPreferences.edit();
        editor.apply();

        Button btn_login = findViewById(R.id.login_loginBtn);
        TextView btn_register = findViewById(R.id.login_registerBtn);
        edit_email = findViewById(R.id.login_inputEmail);
        edit_password = findViewById(R.id.login_inputPassword);
        check_remember = findViewById(R.id.login_checkBox);

        Boolean remember = sharedPreferences.getBoolean("Erinnerung", false);
        if (remember) {
            edit_email.setText(sharedPreferences.getString("Email", null));
            edit_password.setText(sharedPreferences.getString("Passwort", null));
            check_remember.setChecked(true);
        }

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

    private void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        if(check_remember.isChecked()) {
            editor.putBoolean("Erinnerung", true);
            editor.commit();
        }

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

    private boolean validate() {
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
