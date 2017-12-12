package com.kick_it.jan.t9_mobileapp;

/**
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

import java.util.ArrayList;


public class SearchTab1Playersearch extends Fragment implements ISearch {
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
        view = inflater.inflate(R.layout.fragment_searchtab1playersearch, container, false);
        ListView listView = view.findViewById(R.id.listview1);


        // CREATE THE ARRAYLIST
        strings = new ArrayList<>();
        // FILL THE ARRAYLIST
        for (int i = 0; i < 20; i++) {
            strings.add(String.valueOf(i));
        }
        strings.add("12");
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, strings);


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

