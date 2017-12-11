package com.example.jan.t9_mobileapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Christopher on 20.11.2017.
 */

public class TeamTab2Profile extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teamtab2profile, container, false);

        return rootView;
    }
}