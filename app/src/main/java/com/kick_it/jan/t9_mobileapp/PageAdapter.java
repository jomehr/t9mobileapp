package com.kick_it.jan.t9_mobileapp;

/**
 * Created by Christopher on 12.12.2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

    private String mSearchTerm;
    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public PageAdapter(FragmentManager fm, int tabCount, String searchTerm) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.mSearchTerm= searchTerm;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                SearchTab1Playersearch tab1 = SearchTab1Playersearch.newInstance(mSearchTerm);
                return tab1;
            case 1:
                SearchTab2Teamsearch tab2 = SearchTab2Teamsearch.newInstance(mSearchTerm);
                return tab2;
            default:
                return null;
        }
    }

    //Overwriten method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    public void setTextQueryChanged(String newText) {
        mSearchTerm = newText;
    }
}
