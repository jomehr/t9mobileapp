package com.matchfinder.jan.t9_mobileapp.util;

/*
 * Created by Christopher on 11.12.2017.
 * CustomList for the Team Roster Tab to display an image and the name in a Row.
 */

import android.annotation.SuppressLint;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matchfinder.jan.t9_mobileapp.R;

public class CustomRosterList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private Integer[] imageId = null;
    public CustomRosterList(Activity context,
                            String[] web, Integer[] imageId) {
        super(context, R.layout.item_roster, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams") View rowView= inflater.inflate(R.layout.item_roster, null, true);
        TextView txtTitle = rowView.findViewById(R.id.text);

        ImageView imageView = rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
