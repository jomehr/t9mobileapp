package com.matchfinder.jan.t9_mobileapp.activities;

/**
 * Created by Chris on 17.01.2018.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.matchfinder.jan.t9_mobileapp.util.PickerEditor;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public class CreateLeague extends AppCompatActivity {

    private static int PLACE_PICKER_REQUEST = 1;
    private int leagueMaxTeamNumber = -1;
    private String type, target, city;
    private TextView leagueLocation, leagueMaxTeams, leagueTarget, leagueDescription;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_league);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            getSupportActionBar().setTitle(R.string.create);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        leagueLocation = findViewById(R.id.createLeague_leagueOrtText);
        leagueMaxTeams = findViewById(R.id.createLeague_leagueMaxTeams);
        leagueTarget =  findViewById(R.id.createLeague_leagueTarget);
        leagueDescription = findViewById(R.id.createLeague_leagueDescription);
        saveBtn = findViewById(R.id.createEvent_createButton);


        final Spinner typeSpinner = findViewById(R.id.createLeague_typeSpinner);
        ArrayAdapter <CharSequence> s1adapter = ArrayAdapter.createFromResource(this,R.array.createLeague_typeSpinnerValues, R.layout.spinner_item);
        s1adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        typeSpinner.setAdapter(s1adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = "Liga";
            }
        });

        leagueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAPlace();
            }
        });

        leagueMaxTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberPickerDialog();
            }
        });

        final Spinner targetSpinner = findViewById(R.id.createLeague_targetSpinner);
        ArrayAdapter <CharSequence> s2adapter = ArrayAdapter.createFromResource(this,R.array.createLeague_targetSpinnerValues, R.layout.spinner_item);
        s2adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        targetSpinner.setAdapter(s2adapter);
        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target = targetSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                target = "offen";
            }
        });

        leagueDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textPickerDialog();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveBtn.isEnabled()) {
                    saveLeague();
                } else {
                    Toast.makeText(CreateLeague.this, "Gebe die benötigten Daten ein um die Liga zu speichern", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Place Picker
    public void pickAPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //get city from Place Picker
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                //Layout anpassen
                ViewGroup.LayoutParams params = leagueLocation.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                leagueLocation.setLayoutParams(params);

                //get place from Place Picker and reverse geocode it to get string of current city
                Place place = PlacePicker.getPlace(this, data);
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    city = addresses.get(0).getLocality();

                    leagueLocation.setText(city);

                    //enable savebutton
                    if (leagueMaxTeamNumber >= 4){
                        saveBtn.setEnabled(true);
                        saveBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateLeague.this, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Number Picker
    private void numberPickerDialog() {

        final NumberPicker myNumberPicker = new NumberPicker(getApplicationContext());
        PickerEditor.setNumberPickerTextColor(myNumberPicker, getColor(R.color.colorBlack));
        myNumberPicker.setMaxValue(18);
        myNumberPicker.setMinValue(4);

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
        builder.setTitle(R.string.max_team_amount);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                leagueMaxTeamNumber = myNumberPicker.getValue();
                leagueMaxTeams.setText(String.format("%01d", leagueMaxTeamNumber));

                if (leagueLocation.getText().toString() != "Ort Auswählen") {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Text Picker
    private void textPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint(leagueDescription.getText());
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(128)});
        input.setMaxLines(4);
        builder.setTitle("Beschreibung").setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leagueDescription.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Zurück", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //save data to parse server
    private void saveLeague() {
        ParseServer ps = ParseServer.getInstance(this);

        ps.saveLeagueData(this, type, city, leagueMaxTeamNumber, target, leagueDescription.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, Search.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, menu_settings.class));
                return true;
            case R.id.action_developer:
                startActivity(new Intent(this, menu_developer.class));
                return true;
            case R.id.action_faq:
                startActivity(new Intent(this, menu_faq.class));
                return true;
            case R.id.action_sign_out:
                ParseServer ps =ParseServer.getInstance(this);
                if (ps.logOut()) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }else {
                    Toast.makeText(this, "Fehler beim Logout",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}