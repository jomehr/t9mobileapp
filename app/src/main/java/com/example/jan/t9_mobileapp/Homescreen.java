package com.example.jan.t9_mobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(R.string.app_name);

        //Listener für "click" auf Profile_Button
        Button btn_profile = (Button) findViewById(R.id.btnProfile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, Profile.class));
            }
        });

        //Listener für "click" auf Team_Button
        Button btn_team = (Button) findViewById(R.id.btnTeam);
        btn_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, Team.class));
            }
        });

        //Listener für "click" auf EventRadar_Button
        Button btn_EventRadar = (Button) findViewById(R.id.btnEventRadar);
        btn_EventRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, EventRadar.class));
            }
        });

        //Listener für "click" auf League_Button
        Button btn_League = (Button) findViewById(R.id.btnLeague);
        btn_League.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, League.class));
            }
        });

        //Listener für "click" auf Search_Button
        Button btn_search = (Button) findViewById(R.id.btnSearch);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, Search.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


}
