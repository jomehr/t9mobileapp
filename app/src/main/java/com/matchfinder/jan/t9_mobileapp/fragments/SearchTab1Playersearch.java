package com.matchfinder.jan.t9_mobileapp.fragments;

/*
 * Created by Christopher on 20.11.2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.matchfinder.jan.t9_mobileapp.activities.Search;
import com.matchfinder.jan.t9_mobileapp.interfaces.IFragmentListener;
import com.matchfinder.jan.t9_mobileapp.interfaces.ISearch;
import com.matchfinder.jan.t9_mobileapp.R;

import com.matchfinder.jan.t9_mobileapp.util.CustomSearchPlayerList;

import java.util.ArrayList;

public class SearchTab1Playersearch extends Fragment implements ISearch {

    private static final String ARG_SEARCHTERM = "search_term";
    private String mSearchTerm = null;

    private IFragmentListener mIFragmentListener = null;
    private ArrayAdapter<String> arrayAdapter = null;
    //private CustomSearchPlayerList arrayAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.fragment_searchtab1playersearch, container, false);



        // CREATE THE ARRAYLIST
        ArrayList<String> playerNameList = new ArrayList<>();
        // FILL THE ARRAYLIST
            playerNameList.add("Jan Mehr");
            playerNameList.add("Christopher Huntscha");
            playerNameList.add("Simon Mertens");
            playerNameList.add("Maximilian Storr");
            playerNameList.add("Taras Zaika");
            playerNameList.add("Tarek Al Ashi");

        ArrayList<Integer> playerProfilePicture = new ArrayList<>();
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);
            playerProfilePicture.add(R.drawable.ic_person_black_72dp);


        /*

        ListView playersearchlist = view.findViewById(R.id.listview1);
        playersearchlist.setAdapter(firstLeagueAdapter);
        */

        //CustomSearchPlayerList firstLeagueAdapter = new CustomSearchPlayerList(getActivity(), playerNameList, playerProfilePicture);
        //standard item layout
        //arrayAdapter = new CustomSearchPlayerList(getActivity(), playerNameList, playerProfilePicture);
            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, playerNameList);
        ListView listView = view.findViewById(R.id.listview1);
        listView.setAdapter(arrayAdapter);



        Search searchActivity = (Search) getActivity();
        searchActivity.getDataFromFragment_one(playerNameList);
        if (getArguments() != null) {
            mSearchTerm = (String) getArguments().get(ARG_SEARCHTERM);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mSearchTerm) {
            onTextQuery(mSearchTerm);
        }
    }

    public SearchTab1Playersearch() {
    }

    public static SearchTab1Playersearch newInstance(String searchTerm) {
        SearchTab1Playersearch fragment = new SearchTab1Playersearch();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCHTERM, searchTerm);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onTextQuery(String text) {
        arrayAdapter.getFilter().filter(text);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIFragmentListener = (IFragmentListener) context;
        mIFragmentListener.addiSearch(SearchTab1Playersearch.this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mIFragmentListener)
            mIFragmentListener.removeISearch(SearchTab1Playersearch.this);
    }
}

