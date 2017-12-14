package com.kick_it.jan.t9_mobileapp.fragmenten;

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

import com.kick_it.jan.t9_mobileapp.aktivitaeten.Search;
import com.kick_it.jan.t9_mobileapp.schnittstellen.IFragmentListener;
import com.kick_it.jan.t9_mobileapp.schnittstellen.ISearch;
import com.kick_it.jan.t9_mobileapp.R;

import java.util.ArrayList;


public class SearchTab2Teamsearch extends Fragment implements ISearch {
    private static final String ARG_SEARCHTERM = "search_term";
    private String mSearchTerm = null;

    ArrayList<String> strings = null;
    private IFragmentListener mIFragmentListener = null;
    ArrayAdapter<String> arrayAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.fragment_searchtab2teamsearch, container, false);
        ListView listView = view.findViewById(R.id.listview1);


        // CREATE AND FILL THE ARRAYLIST
        strings = new ArrayList<>();
        strings.add("Krampfadergeschwader");
        strings.add("Kebab Connection");
        strings.add("Chronisch Überhopft");
        strings.add("Gardoffelsalaad");
        strings.add("Die glorreichen 70iger");
        strings.add("3sporty5us");
        strings.add("Die Streetfighters");
        strings.add("Babos ohne Namen");
        strings.add("6 richtige plus Superzahl");
        strings.add("Kenias allerletzte Reserve");

        //standard item layout
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);
        //Own item Layout
        //arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_teamsearch, strings);

        listView.setAdapter(arrayAdapter);
        Search mainActivity = (Search) getActivity();
        mainActivity.getDataFromFragment_one(strings);
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

    public SearchTab2Teamsearch() {
    }

    public static SearchTab2Teamsearch newInstance(String searchTerm) {
        SearchTab2Teamsearch fragment = new SearchTab2Teamsearch();
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
        mIFragmentListener.addiSearch(SearchTab2Teamsearch.this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mIFragmentListener)
            mIFragmentListener.removeISearch(SearchTab2Teamsearch.this);
    }
}

