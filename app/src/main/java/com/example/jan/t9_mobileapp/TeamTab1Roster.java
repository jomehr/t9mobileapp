package com.example.jan.t9_mobileapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Christopher on 20.11.2017.
 */

public class TeamTab1Roster extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teamtab1roster, container, false);
        return rootView;
    }
}
