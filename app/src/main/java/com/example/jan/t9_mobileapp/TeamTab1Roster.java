package com.example.jan.t9_mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Christopher on 20.11.2017.
 */

public class TeamTab1Roster extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teamtab1roster, container, false);

        //Listener für "click" auf den ersten Spieler von oben in der Teamaufstellung
        LinearLayout toProfile1 = (LinearLayout) rootView.findViewById(R.id.spielercontainer1);
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
        return rootView;
    }
}

