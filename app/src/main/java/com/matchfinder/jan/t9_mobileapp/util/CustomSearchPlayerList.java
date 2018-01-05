package com.matchfinder.jan.t9_mobileapp.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.matchfinder.jan.t9_mobileapp.R;

import java.util.ArrayList;

/**
 * Created by Christopher on 04.01.2018.
 */

public class CustomSearchPlayerList extends ArrayAdapter<String> implements Filterable, ThemedSpinnerAdapter {

    private final Activity context;
    private final ArrayList<String> playernames;
    private final ArrayList<Integer> playerpictures;

    public CustomSearchPlayerList(Activity context,
                                  ArrayList<String> playernames, ArrayList<Integer> playerpictures) {
        super(context, R.layout.item_roster, playernames);
        this.context = context;
        this.playernames = playernames;
        this.playerpictures = playerpictures;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.item_playersearch, null, true);

        TextView txtTitle = rowView.findViewById(R.id.text1);
        ImageView imageView = rowView.findViewById(R.id.search_player_picture);


        txtTitle.setText(playernames.get(position));
        imageView.setImageResource(playerpictures.get(position));

        return rowView;
    }
}