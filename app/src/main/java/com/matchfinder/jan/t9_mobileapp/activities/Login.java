package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
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
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;

/*
 * Created by Jan on 07.12.2017.
 */

public class Login extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private EditText edit_username,edit_password;
    private CheckBox check_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("Registration", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean remember = sharedPreferences.getBoolean("Erinnerung", false);
        if (remember) {
            startActivity(new Intent(getApplicationContext(), Homescreen.class));
            finish();
        } else {
            setContentView(R.layout.activity_login);
        }

        final Button btn_login = findViewById(R.id.login_loginBtn);
        TextView btn_register = findViewById(R.id.login_registerBtn);
        edit_username = findViewById(R.id.login_inputUsername);
        edit_password = findViewById(R.id.login_inputPassword);
        check_remember = findViewById(R.id.login_checkBox);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                    startActivity(new Intent(Login.this, Homescreen.class));
                } catch (Exception e) {
                    e.printStackTrace()
                    ;}
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

        ParseServer ps = ParseServer.getInstance(this);
        ps.loginUser(this, edit_username.getText().toString(), edit_password.getText().toString());


/*      String email = edit_email.getText().toString();
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
        }*/
    }

    private boolean validate() {
        boolean valid = true;

        String email = edit_username.getText().toString();
        String password = edit_password.getText().toString();

        if (email.isEmpty()) {
            edit_username.setError("Gebe einen Nutzernamen ein");
            valid = false;
        } else {
            edit_username.setError(null);
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
