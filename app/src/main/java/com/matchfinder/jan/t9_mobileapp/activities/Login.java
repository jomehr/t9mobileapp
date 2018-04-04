package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
 * Created by Jan on 07.12.2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LOGIN";

    private EditText edit_email, edit_password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Views
        edit_email = findViewById(R.id.login_inputUsername);
        edit_password = findViewById(R.id.login_inputPassword);

        //Button
        findViewById(R.id.login_loginBtn).setOnClickListener(this);
        findViewById(R.id.login_registerBtn).setOnClickListener(this);
        findViewById(R.id.login_forgotPasswordBtn).setOnClickListener(this);

/*
        ParseServer.getInstance(this);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, Homescreen.class));
            finish();
        }*/
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loginFirebase(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(Login.this, Homescreen.class));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Login fehlgeschlagen", Toast.LENGTH_SHORT).show();
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

        if (password.isEmpty()) {
            edit_password.setError("Gebe das Passwort ein");
            valid = false;
        } else {
            edit_password.setError(null);
        }

        return valid;
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_loginBtn) {
            if (!isNetworkAvailable()) {
                Toast.makeText(Login.this, "keine Internetverbindung", Toast.LENGTH_SHORT).show();
            } else {
                //loginParse();
                loginFirebase(edit_email.getText().toString(), edit_password.getText().toString());
            }
        } else if (i == R.id.login_registerBtn) {
            startActivity(new Intent(Login.this, Registration.class));
        } else if (i == R.id.login_forgotPasswordBtn) {
            //TODO implement Email-Reset
            //startActivity(new Intent(Login.this, ResetEmail.class));
        }
    }


/*    private void loginParse() {
        ParseServer ps = ParseServer.getInstance(Login.this);
        ps.loginUser(this, edit_username.getText().toString(), edit_password.getText().toString(), this);
    }*/
}
