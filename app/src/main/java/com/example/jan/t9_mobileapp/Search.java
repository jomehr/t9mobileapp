package com.example.jan.t9_mobileapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tarek on 17.11.2017.
 */

public class Search extends AppCompatActivity{

    private ListView lv;
    EditText editInput;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ListView Data Array Adapter

        String playerName[] = {"Tarek", "Jan", "Taras", "Max", "Simon", "Chris", "Jonas", "Hannes", "Omar", "Omas", "Omag"};
        int playerImages[] = {R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp, R.drawable.ic_person_black_72dp};

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        //Adding players to listview

        adapter = new ArrayAdapter<String>(this, R.layout.activity_list_player, R.id.player_name, playerName);
                lv.setAdapter(adapter);

        // ** Showing the keyboard when pressing on "Search Players" ** //

        editInput = (EditText) findViewById(R.id.inputSearch);
        //InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //mgr.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);

        editInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editInput, InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });

        // ** Adding the name and image of the players in the listview **  TEST//

        //START

        
        /*class ViewHolder
        {
            TextView tvw1;
            ImageView imw1;
            ViewHolder (View v)
            {
               tvw1 = (TextView) v.findViewById(R.id.player_name);
               imw1 = (ImageView) v.findViewById(R.id.playerImage);
            }
        }*/

        // ** END ** //

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

            // ** Showing the keyboard when pressing on "Search Players" ** //

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}