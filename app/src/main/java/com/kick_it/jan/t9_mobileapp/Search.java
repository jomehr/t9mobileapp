package com.kick_it.jan.t9_mobileapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Tarek on 17.11.2017.
 */

public class Search extends AppCompatActivity{

   // private sectionsPageAdapter tSectionsPagerAdapter;
    //private ViewPager tViewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Suche");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*tSectionsPagerAdapter = new sectionsPageAdapter(getSupportFragmentManager());
        tViewPager = (ViewPager) findViewById(R.id.searchContainer);
        tViewPager.setAdapter(tSectionsPagerAdapter);

        tViewPager.setCurrentItem(1);*/


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Player Suche"));
        tabLayout.addTab(tabLayout.newTab().setText("Team Suche"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        //final SearchAdapter adapter = new SearchAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

/*    public class sectionsPageAdapter extends FragmentPagerAdapter {
        private sectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    playerSearchTab1 x = new playerSearchTab1();
                    //return x;
                case 1:
                    teamSearchTab2 y = new teamSearchTab2();
                    //return y;
                default:
                    return null;
            }
        }

        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitlE(int position) {
            switch (position) {
                case 0:
                    return "playersuche";
                case 1:
                    return "teamsuche";
            }
            return null;
        }
    }*/
    public void onFragmentInteraction(Uri uri) {

    }
}