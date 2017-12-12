package com.kick_it.jan.t9_mobileapp;

/*
 * Created by Christopher on 12.12.2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kick_it.jan.t9_mobileapp.fragments.SearchTab1Playersearch;
import com.kick_it.jan.t9_mobileapp.fragments.SearchTab2Teamsearch;

public class PageAdapter extends FragmentStatePagerAdapter {

    private String mSearchTerm;

    //integer to count number of tabs
    private int tabCount;

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
                return SearchTab1Playersearch.newInstance(mSearchTerm);
            case 1:
                return SearchTab2Teamsearch.newInstance(mSearchTerm);
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
