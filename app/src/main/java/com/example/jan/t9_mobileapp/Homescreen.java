package com.example.jan.t9_mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(myToolbar);
        setTitle(R.string.title_homescreen);
    }
}
