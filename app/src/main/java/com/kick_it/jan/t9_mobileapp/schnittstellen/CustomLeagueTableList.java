package com.kick_it.jan.t9_mobileapp.schnittstellen;

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
import android.widget.TextView;

import com.kick_it.jan.t9_mobileapp.R;

public class CustomLeagueTableList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] teamname;
    private final int[] teamrank;
    private final int[] teampoints;
    private final int[] teamgamesplayed;
    private final int[] teamgoaldifference;


    public CustomLeagueTableList(Activity context, int[] teamrank, String[] teamname, int[] teampoints, int[] teamgamesplayed, int[] teamgoaldifference) {
        super(context, R.layout.item_league_tables, teamname);
        this.context = context;
        this.teamrank = teamrank;
        this.teamname = teamname;
        this.teampoints = teampoints;
        this.teamgamesplayed = teamgamesplayed;
        this.teamgoaldifference = teamgoaldifference;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams") View rowView= inflater.inflate(R.layout.item_league_tables, null, true);
        TextView txtRank = rowView.findViewById(R.id.league_tables_tableRank);
        TextView txtName = rowView.findViewById(R.id.league_tables_teamName);
        TextView txtGames = rowView.findViewById(R.id.league_tables_gamesPlayed);
        TextView txtGoalDiff = rowView.findViewById(R.id.league_tables_goalDifference);
        TextView txtPoints = rowView.findViewById(R.id.league_tables_points);

        txtRank.setText(Integer.toString(teamrank[position]));
        txtName.setText(teamname[position]);
        txtGames.setText(Integer.toString(teamgamesplayed[position]));
        txtGoalDiff.setText(Integer.toString(teamgoaldifference[position]));
        txtPoints.setText(Integer.toString(teampoints[position]));

        return rowView;
    }
}
