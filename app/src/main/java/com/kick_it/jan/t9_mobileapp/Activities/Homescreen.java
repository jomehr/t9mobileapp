package com.kick_it.jan.t9_mobileapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import com.kick_it.jan.t9_mobileapp.R;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        //Listener für "click" auf Banner1
        RelativeLayout banner1 = findViewById(R.id.newsContainer);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, EventRadar.class));
            }
        });

        RelativeLayout banner2 = findViewById(R.id.searchTeamContainer);
        banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, CreateEvent.class));
            }
        });

        RelativeLayout banner3 = findViewById(R.id.tableContainer);
        banner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, BunteLiga.class));
            }
        });

        /*
        //TeamButton
        RelativeLayout banner4 = findViewById(R.id.banner_btn4);
        banner4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, Team.class));
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(Homescreen.this, Search.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(Homescreen.this, Profile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
