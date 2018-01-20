package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.*;

/*
 * Created by Jan on 17.11.2017.
 */

public class League extends AppCompatActivity {
    private static  final String PREFER_NAME_TEAMDATA = "TeamData";
    private SharedPreferences sharedPreferencesTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            getSupportActionBar().setTitle(R.string.league);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Clicklistener links to activity News
        RelativeLayout news_btn = findViewById(R.id.newsContainer);
        news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(League.this, LeagueNews.class));
            }
        });
        //Clicklistener links to Fragment Teamsearch
        RelativeLayout teamsearch_btn = findViewById(R.id.searchTeamContainer);
        teamsearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(League.this, Search.class));
            }
        });
        //Clicklistener links to activity Tables
        RelativeLayout tables_btn = findViewById(R.id.tableContainer);
        tables_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(League.this, LeagueTables.class));
            }
        });
        //Clicklistener links to activity Team
        RelativeLayout team_btn = findViewById(R.id.teamContainer);
        sharedPreferencesTeam = getSharedPreferences(PREFER_NAME_TEAMDATA, Context.MODE_PRIVATE);
        team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO set defValue to false and implement CreateTeam logic and layout
                if (sharedPreferencesTeam.getBoolean("inTeam", true)) {
                    startActivity(new Intent(League.this, Team.class));
                }else {
                    startActivity(new Intent(League.this, CreateTeam.class));
                }
            }
        });
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
                ParseServer ps =ParseServer.getInstance(this);
                if (ps.logOut()) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }else {
                    Toast.makeText(this, "Fehler beim Logout",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
