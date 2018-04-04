package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.matchfinder.jan.t9_mobileapp.R;

/*
 * Created by Jan on 10.12.2017.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "REGISTRATION";

    private EditText edit_email, edit_password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Views
        edit_email = findViewById(R.id.register_inputEmail);
        edit_password = findViewById(R.id.register_inputPassword);

        //Buttons
        findViewById(R.id.register_registerBtn).setOnClickListener(this);
        findViewById(R.id.register_loginBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    public void registerFirebase(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUSerWithEmail:success");
                            startActivity(new Intent(Registration.this, Homescreen.class));
                            finish();
                        } else {
                            Log.w(TAG, "createUSerWithEmail:failure", task.getException());
                            Toast.makeText(Registration.this, "Registration fehlgeschlagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_registerBtn) {
            if (!isNetworkAvailable()) {
                Toast.makeText(Registration.this, "keine Internetverbindung", Toast.LENGTH_SHORT).show();
            } else {
                //registerParse();
                registerFirebase(edit_email.getText().toString(), edit_password.getText().toString());
            }
        } else if (i == R.id.register_loginBtn) {
            startActivity(new Intent(Registration.this, Login.class));
            finish();
        }
    }

/*
    private void registerParse () {

        final String email = edit_email.getText().toString();
        final String username = edit_name.getText().toString();
        final String password = edit_password.getText().toString();

        ParseServer ps = ParseServer.getInstance(Registration.this);
        ps.registerUser(this, email, username, password, this);
    }*/
}
