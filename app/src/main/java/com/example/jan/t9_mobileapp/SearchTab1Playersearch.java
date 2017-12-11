package com.example.jan.t9_mobileapp;

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

public class SearchTab1Playersearch extends Fragment{
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
        View rootView = inflater.inflate(R.layout.fragment_searchtab1playersearch, container, false);

        CustomList adapter = new
                CustomList(getActivity(), playernames, imageId);
        list = rootView.findViewById(R.id.listView1PlayerSearch);
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
        return rootView;
    }
}

