package com.example.jan.t9_mobileapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jan on 17.11.2017.
 */

public class Search extends AppCompatActivity{

    private ListView lv;

    //ListView Adapter
    ArrayAdapter<String>adapter;

    //search Edit Test
    EditText inputSearch;

    //ArrayList for ListView

    ArrayList<HashMap<String, String >>productList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Suche");

        //ListView Data Array Adapter

        String playerName[] = {"Tarek", "Jan", "Taras", "Max", "Simon", "Chris"};

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        //Adding players to listview

        adapter = new ArrayAdapter<String>(this, R.layout.activity_list_player, R.id.player_name, playerName);
                lv.setAdapter(adapter);




        // ** Enabling Search **/

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Search.this.adapter.getFilter().filter(charSequence);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

}
