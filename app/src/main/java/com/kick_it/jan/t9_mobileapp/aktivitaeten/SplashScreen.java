package com.kick_it.jan.t9_mobileapp.aktivitaeten;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kick_it.jan.t9_mobileapp.R;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int timeInMS = 500;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Homescreen.class );
                startActivity(intent);
                finish();
            }
        },timeInMS);
    }
}
