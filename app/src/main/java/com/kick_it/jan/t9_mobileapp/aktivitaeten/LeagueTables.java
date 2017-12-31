package com.kick_it.jan.t9_mobileapp.aktivitaeten;

/**
 * Created by Christopher on 11.12.2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.kick_it.jan.t9_mobileapp.R;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class LeagueTables extends AppCompatActivity {


    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaguetables);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.tabels);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void expandableFirstLeague(View view) {
        expandableLayout1 =  findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableSecondLeague(View view) {
        expandableLayout2 =  findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableThirdLeague(View view) {
        expandableLayout3 =  findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
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
