package com.kick_it.jan.t9_mobileapp.aktivitaeten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kick_it.jan.t9_mobileapp.R;

/*
 * Created by Christopher on 11.12.2017.
 */

public class LeagueNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaguenews);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
