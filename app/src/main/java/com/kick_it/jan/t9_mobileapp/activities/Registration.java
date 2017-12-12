package com.kick_it.jan.t9_mobileapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kick_it.jan.t9_mobileapp.R;

/*
 * Created by Jan on 10.12.2017.
 */

public class Registration extends AppCompatActivity{

    private static final String PREFER_NAME = "Registration";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btn_register;
    TextView btn_login;
    EditText edit_email, edit_name, edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn_register = (Button)findViewById(R.id.register_registerBtn);
        btn_login = (TextView) findViewById(R.id.register_loginBtn);
        edit_email = (EditText)findViewById(R.id.register_inputEmail);
        edit_name = (EditText)findViewById(R.id.register_inputName);
        edit_password = (EditText)findViewById(R.id.register_inputPassword);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }

    public void register() {

        if (!validate()) {
            onRegistrationFailed();
            return;
        }

        sharedPreferences = getSharedPreferences(PREFER_NAME,0);
        editor = sharedPreferences.edit();

        String email = edit_email.getText().toString();
        String name = edit_name.getText().toString();
        String password = edit_password.getText().toString();

        String curMail = sharedPreferences.getString("Email", null);

        if (sharedPreferences.contains("Email") && curMail.equals(email)){
            Toast.makeText(getApplicationContext(), "Account existiert bereits",Toast.LENGTH_SHORT).show();
        } else {
            //editor.clear().commit();
            editor.putString("Email", email);
            editor.putString("Name", name);
            editor.putString("Passwort", password);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Daten bearbeitet",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Registration.this, Login.class));
            finish();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = edit_email.getText().toString();
        String name = edit_name.getText().toString();
        String password = edit_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Gebe eine valide Email-Adresse ein");
            valid = false;
        } else {
            edit_email.setError(null);
        }

        if (name.isEmpty() || name.length() < 3 || name.length() > 15) {
            edit_name.setError("Name muss zwischen 3 und 15 Zeichen lang sein");
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

    public void onRegistrationFailed() {
        Toast.makeText(getApplicationContext(), "Registrierung fehlgeschlagen",Toast.LENGTH_SHORT).show();
    }
}
