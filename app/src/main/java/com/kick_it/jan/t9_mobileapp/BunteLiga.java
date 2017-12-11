package com.kick_it.jan.t9_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Jan on 17.11.2017.
 */

public class BunteLiga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunteliga);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Bunte Liga");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout team_btn = findViewById(R.id.team_btn);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
