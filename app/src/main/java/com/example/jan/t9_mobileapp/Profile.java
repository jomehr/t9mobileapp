package com.example.jan.t9_mobileapp;

import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Jan on 13.11.2017.
 */

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Toolbar myToolbar = findViewById(R.id.profile_collapsingStaticToolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsingToolbar);
        final AppBarLayout appBar = (AppBarLayout)findViewById(R.id.profile_collapsingToolbarLayout);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                    myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                } else {
                    myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
