package com.matchfinder.jan.t9_mobileapp.aktivitaeten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.menu.*;

/*
 * Created by Jan on 17.11.2017.
 */

public class BunteLiga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunteliga);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setTitle(R.string.bunte_liga);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Clicklistener links to activity News
        RelativeLayout news_btn = findViewById(R.id.newsContainer);
        news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BunteLiga.this, LeagueNews.class));
            }
        });
        //Clicklistener links to Fragment Teamsearch
        RelativeLayout teamsearch_btn = findViewById(R.id.searchTeamContainer);
        teamsearch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BunteLiga.this, Search.class));
            }
        });
        //Clicklistener links to activity Tables
        RelativeLayout tables_btn = findViewById(R.id.tableContainer);
        tables_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BunteLiga.this, LeagueTables.class));
            }
        });
        //Clicklistener links to activity Team
        RelativeLayout team_btn = findViewById(R.id.teamContainer);
        team_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BunteLiga.this, Team.class));
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
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
