package com.matchfinder.jan.t9_mobileapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ListView;

import com.matchfinder.jan.t9_mobileapp.activities.Profile;
import com.matchfinder.jan.t9_mobileapp.util.CustomRosterList;
import com.matchfinder.jan.t9_mobileapp.R;

/*
 * Created by Christopher on 20.11.2017.
 */

public class TeamTab1Roster extends Fragment{
    /*
        Hardcoded names, should get names dynamicly from the Database and and them before building the layout
     */
    private final String[] playernames = {
            "Jan Mehr",
            "Christopher Huntscha",
            "Simon Mertens",
            "Maximilian Storr",
            "Taras Zaika",
            "Tarek Al Ashi"};
    /*
         Hardcoded picture paths, should get names from the Database before building the layout
    */
    private final Integer[] imageId = {
            R.drawable.ic_person_black_72dp,
            R.drawable.ic_person_black_72dp,
            R.drawable.ic_person_black_72dp,
            R.drawable.ic_person_black_72dp,
            R.drawable.ic_person_black_72dp,
            R.drawable.ic_person_black_72dp};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teamtab1roster, container, false);

        CustomRosterList adapter = new CustomRosterList(getActivity(), playernames, imageId);
        ListView list = rootView.findViewById(R.id.teamtab1rosterlist);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Test notification on click , can be commented out after debugging.
                Toast.makeText(getActivity(), "You Clicked at " +playernames[+ position], Toast.LENGTH_SHORT).show();
                //links to the player profile
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        return rootView;
    }
}