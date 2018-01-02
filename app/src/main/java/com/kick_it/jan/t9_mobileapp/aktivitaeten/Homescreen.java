package com.kick_it.jan.t9_mobileapp.aktivitaeten;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import com.kick_it.jan.t9_mobileapp.R;
import com.kick_it.jan.t9_mobileapp.menu.menu_data_privacy;
import com.kick_it.jan.t9_mobileapp.menu.menu_developer;
import com.kick_it.jan.t9_mobileapp.menu.menu_faq;
import com.kick_it.jan.t9_mobileapp.menu.menu_settings;

public class Homescreen extends AppCompatActivity {

    private final int REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setTitle(R.string.app_name);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //Listener für "click" auf Banner1
        RelativeLayout banner1 = findViewById(R.id.homescreen_findGameContainer);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityIfNeeded(new Intent(Homescreen.this, EventRadar.class), REQUEST);
                //startActivity(new Intent(Homescreen.this, EventRadar.class));
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
                startActivity(new Intent(this, Search.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, menu_settings.class));
                return true;
            case R.id.action_developer:
                startActivity(new Intent(this, menu_developer.class));
                return true;
            case R.id.action_faq:
                startActivity(new Intent(this, menu_faq.class));
                return true;
            case R.id.action_sign_out:
                startActivity(new Intent(this, Login.class));
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
