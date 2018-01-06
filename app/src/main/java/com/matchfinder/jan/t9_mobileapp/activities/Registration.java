package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Intent;
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
                try {
                    register();
                } catch  (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }

    private void register() {
        ParseServer ps = ParseServer.getInstance(this);

        String email = edit_email.getText().toString();
        String username = edit_name.getText().toString();
        String password = edit_password.getText().toString();

        if (!validate(email, username, password)) {
            onRegistrationFailed();
            return;
        }

        ps.registerUser(this, email, username, password);


/*      SharedPreferences sharedPreferences = getSharedPreferences(PREFER_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String email = edit_email.getText().toString();
        String name = edit_name.getText().toString();
        String password = edit_password.getText().toString();

        String curMail = sharedPreferences.getString("Email", null);

        if (sharedPreferences.contains("Email") &&
                curMail.equals(email)){
            Toast.makeText(getApplicationContext(), "Account existiert bereits",Toast.LENGTH_SHORT).show();
        } else {
            //editor.clear().commit();
            editor.putString("Email", email);
            editor.putString("Name", name);
            editor.putString("Passwort", password);
            editor.apply();
            Toast.makeText(getApplicationContext(), "Daten bearbeitet",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Registration.this, Login.class));
            finish();
        }*/
    }


    private boolean validate(String email, String username, String password) {
        boolean valid = true;

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

    private void onRegistrationFailed() {
        Toast.makeText(this, "Registrierung fehlgeschlagen",Toast.LENGTH_SHORT).show();
    }
}
