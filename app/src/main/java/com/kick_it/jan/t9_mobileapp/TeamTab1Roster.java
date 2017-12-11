package com.kick_it.jan.t9_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ListView;

/**
 * Created by Christopher on 20.11.2017.
 */

public class TeamTab1Roster extends Fragment{
    ListView list;
    /*
    Hardcoded names, should get names from the Database and and them before building the layout
     */
    String[] playernames = {
            "Jan Mehr",
            "Christopher Huntscha",
            "Simon Mertens",
            "Maximilian Storr",
            "Taras Zaika",
            "Tarek Al Ashi",};

    Integer[] imageId = {
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

        CustomList adapter = new
                CustomList(getActivity(), playernames, imageId);
        list=(ListView)rootView.findViewById(R.id.listView1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Testnotification on click
                Toast.makeText(getActivity(), "You Clicked at " +playernames[+ position], Toast.LENGTH_SHORT).show();
                //links to the playerprofile
                startActivity(new Intent(getActivity(), Profile.class));

            }
        });

        // Für die statische Seite, kann später gelöscht werden.
        /*
        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile1 = (LinearLayout) rootView.findViewById(R.id.spielercontainer);
        toProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });

        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile2 = (LinearLayout) rootView.findViewById(R.id.spielercontainer2);
        toProfile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile3 = (LinearLayout) rootView.findViewById(R.id.spielercontainer3);
        toProfile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile4 = (LinearLayout) rootView.findViewById(R.id.spielercontainer4);
        toProfile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile5 = (LinearLayout) rootView.findViewById(R.id.spielercontainer5);
        toProfile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        */
        return rootView;
    }
}

