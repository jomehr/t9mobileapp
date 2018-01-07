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
        sharedPreferences = getSharedPreferences("Registration", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Boolean remember = sharedPreferences.getBoolean("Erinnerung", false);
        if (remember) {
            startActivity(new Intent(Login.this, Homescreen.class));
            finish();
        } else {
            super.onCreate(savedInstanceState);
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
                   if (login() == true) {
                        if(check_remember.isChecked()) {
                            editor.putBoolean("Erinnerung", true);
                            editor.commit();
                        }
                        startActivity(new Intent(Login.this, Homescreen.class));
                        finish();
                    } else {
                       onLoginFailed();
                   }
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }

    private boolean login() {

        ParseServer ps = ParseServer.getInstance(this);
        boolean succes = true;
        boolean result = ps.loginUser(this, edit_username.getText().toString(), edit_password.getText().toString());

        //input vallidation, false if input empty
        if (edit_username.getText().toString().isEmpty() || edit_password.getText().toString().isEmpty()) {
            succes =  false;
        }

        if (!result){
            succes = result;
        }

        return succes;
    }

    private void onLoginFailed() {
        Toast.makeText(this, "Falsche Login-Daten",Toast.LENGTH_SHORT).show();
    }
}
