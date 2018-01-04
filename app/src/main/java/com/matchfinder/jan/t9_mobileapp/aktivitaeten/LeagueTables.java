package com.matchfinder.jan.t9_mobileapp.aktivitaeten;

/*
 * Created by Christopher on 11.12.2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.matchfinder.jan.t9_mobileapp.R;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.matchfinder.jan.t9_mobileapp.util.CustomLeagueTableList;

public class LeagueTables extends AppCompatActivity {

    /*
        Hardcoded , should import stats from Database
     */
    private final int[] firstLeagueRanks = {1, 2, 3, 4, 5, 6};
    private final String[] firstLeagueTeamNames = {"FEK", "GRA", "TOW", "TOW", "TOW", "TOW"};
    private final int[] firstLeagueGamesPlayed = {5, 5, 6, 6, 9, 9};
    private final int[] firstLeagueGoalDiff = {14, 17, -9, -9, -9, -9};
    private final int[] firstLeaguePoints = {13, 11, 9, 9, 9, 9};

    private final int[] secondLeagueRanks = {1, 2, 3, 4, 5, 6};
    private final String[] secondLeagueTeamNames = {"FEK", "GRA", "TOW", "TOW", "TOW", "TOW"};
    private final int[] secondLeagueGamesPlayed = {5, 5, 6, 6, 9, 9};
    private final int[] secondLeagueGoalDiff = {14, 17, -9, -9, -9, -9};
    private final int[] secondLeaguePoints = {13, 11, 9, 9, 9, 9};

    private final int[] thirdLeagueRanks = {1, 2, 3, 4, 5, 6};
    private final String[] thirdLeagueTeamNames = {"FEK", "GRA", "TOW", "TOW", "TOW", "TOW"};
    private final int[] thirdLeagueGamesPlayed = {5, 5, 6, 6, 9, 9};
    private final int[] thirdLeagueGoaldDiff = {14, 17, -9, -9, -9, -9};
    private final int[] thirdLeaguePoints = {13, 11, 9, 9, 9, 9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_tables);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle(R.string.tabels);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CustomLeagueTableList firstLeagueAdapter = new CustomLeagueTableList(this,
                firstLeagueRanks, firstLeagueTeamNames, firstLeagueGamesPlayed, firstLeagueGoalDiff, firstLeaguePoints);
        ListView firstleague = findViewById(R.id.firstleagueTable);
        firstleague.setAdapter(firstLeagueAdapter);

        CustomLeagueTableList secondLeagueAdapter = new CustomLeagueTableList(this,
                secondLeagueRanks, secondLeagueTeamNames, secondLeagueGamesPlayed, secondLeagueGoalDiff, secondLeaguePoints);
        ListView secondleague = findViewById(R.id.secondleagueTable);
        secondleague.setAdapter(secondLeagueAdapter);

        CustomLeagueTableList thirdLeagueAdapter = new CustomLeagueTableList(this,
                thirdLeagueRanks, thirdLeagueTeamNames, thirdLeagueGamesPlayed, thirdLeagueGoaldDiff, thirdLeaguePoints);
        ListView thirdleague = findViewById(R.id.thirdleagueTable);
        thirdleague.setAdapter(thirdLeagueAdapter);

    }

    public void expandableFirstLeague(View view) {
        ExpandableRelativeLayout expandableLayout1 =  findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableSecondLeague(View view) {
        ExpandableRelativeLayout expandableLayout2 =  findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableThirdLeague(View view) {
        ExpandableRelativeLayout expandableLayout3 =  findViewById(R.id.expandableLayout3);
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
