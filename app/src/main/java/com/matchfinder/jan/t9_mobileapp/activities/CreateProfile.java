package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.matchfinder.jan.t9_mobileapp.util.InputFilterMinMax;

import java.io.IOException;
import java.util.List;


/*
 * Created by Jan on 18.12.2017.
 */

public class CreateProfile extends AppCompatActivity {

    private static int PLACE_PICKER_REQUEST = 1;
    private final static String PREFER_NAME_PROFILDATA = "ProfilData";
    private SharedPreferences sharedPreferencesProf;

    private int mDay, mMonth, mYear;
    private String city;
    private EditText editFirstName, editLastName, editDay, editMonth, editYear, editExperience, editFavouriteTeam;
    private TextView descriptionText, residenceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setTitle(R.string.profiledit);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button saveBtn = findViewById(R.id.profiledit_saveBtn);
        editFirstName = findViewById(R.id.profiledit_firstName);
        editLastName = findViewById(R.id.profiledit_lastname);
        editDay = findViewById(R.id.profiledit_ageDay);
        editMonth = findViewById(R.id.profiledit_ageMonth);
        editYear = findViewById(R.id.profiledit_ageYear);
        editExperience = findViewById(R.id.profiledit_experienceText);
        editFavouriteTeam = findViewById(R.id.profiledit_favouriteTeamText);
        descriptionText = findViewById(R.id.profiledit_descriptionText);
        residenceText = findViewById(R.id.profiledit_residenceText);
        LinearLayout descriptionLayout = findViewById(R.id.profiledit_descriptionLayout);
        LinearLayout residenceLayout = findViewById(R.id.profiledit_residenceLayout);

        sharedPreferencesProf = getSharedPreferences(PREFER_NAME_PROFILDATA,Context.MODE_PRIVATE);
        if (sharedPreferencesProf.getString("Geburtstag", null)!= null) {
            getData();
        }

        editDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 31)});
        editMonth.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 12)});
        editYear.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 2018)});

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) {
                    onValidationFailed();
                } else {
                    saveData();
                    startActivity(new Intent(CreateProfile.this, Profile.class));
                }
            }
        });

        descriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textPickerDialog();
            }
        });

        residenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickAPlace();
            }
        });
    }


    private boolean validate() {
        boolean valid = true;

        mDay = Integer.parseInt(editDay.getText().toString());
        mMonth = Integer.parseInt(editMonth.getText().toString());
        mYear = Integer.parseInt(editYear.getText().toString());

        if (mYear < 1930 || mYear > 2018) {
            editYear.setError(getString(R.string.invalid_year));
            valid = false;
        }else {
            editDay.setError(null);
        }

        if (mDay > 30 && (mMonth == 2 || mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11)) {
            editDay.setError(getString(R.string.invalid_date));
            valid = false;
        }else {
            editDay.setError(null);
        }

        if(mDay == 29 && mMonth == 2 && (mYear%4 != 0 && (mYear%100 == 0 || mYear%400 != 0))) {
            editDay.setError(getString(R.string.invalid_date));
            valid = false;
        }else {
            editDay.setError(null);
        }

        return  valid;
    }

    private void onValidationFailed() {
        Toast.makeText(CreateProfile.this, R.string.validation_failed, Toast.LENGTH_SHORT).show();
    }

    //Text Picker
    private void textPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint(descriptionText.getHint());
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(128)});
        input.setGravity(Gravity.START | Gravity.TOP);
        input.setLines(2);
        input.setMaxLines(4);
        input.setSingleLine(false);
        builder.setTitle(getString(R.string.description)).setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                descriptionText.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

                //get place from Place Picker and reverse geocode it to get string of current city
                Place place = PlacePicker.getPlace(this, data);
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    city = addresses.get(0).getLocality();

                    residenceText.setText(city);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateProfile.this, "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveData() {
        String appendName = editFirstName.getText().toString() + " " +editLastName.getText().toString();
        String appendDate = mDay + "." + mMonth + "." +mYear;
        String tmp = "in Arbeit";

        //Store data locally in shared preference until app is deleted or cache cleared
        SharedPreferences.Editor editor = sharedPreferencesProf.edit();
        editor.putString("EchterName", appendName);
        editor.putString("Geburtstag", appendDate);
        editor.putString("Wohnort", city);
        editor.putString("ProfilBeschreibung", descriptionText.getText().toString());
        editor.putString("Erfahrung", editExperience.getText().toString());
        editor.putString("Lieblingsteam", editFavouriteTeam.getText().toString());
        editor.apply();

        //Store data on server permanently
        ParseServer ps = ParseServer.getInstance(this);
        ps.saveProfileData(appendName, appendDate, city, descriptionText.getText().toString(), tmp, editExperience.getText().toString(), editFavouriteTeam.getText().toString());
    }

    private void getData() {
        String birthday = sharedPreferencesProf.getString("Geburtstag", null);
        String realname = sharedPreferencesProf.getString("EchterName", null);
        assert birthday != null && realname != null;
        String [] birthtmp = birthday.split("\\.");
        String [] nametmp = realname.split(" ");
        editDay.setHint(birthtmp[0]);
        editMonth.setHint(birthtmp[1]);
        editYear.setHint(birthtmp[2]);
        editFirstName.setHint(nametmp[0]);
        editLastName.setHint(nametmp[1]);

        String residencetmp = sharedPreferencesProf.getString("Wohnort", null);
        residenceText.setHint(residencetmp);

        String descriptiontmp = sharedPreferencesProf.getString("ProfilBeschreibung", null);
        descriptionText.setHint(descriptiontmp);

        String experiencetmp = sharedPreferencesProf.getString("Erfahrung", null);
        editExperience.setHint(experiencetmp);

        String favouriteteamtmp = sharedPreferencesProf.getString("Lieblingsteam", null);
        editFavouriteTeam.setHint(favouriteteamtmp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
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
        }
        return super.onOptionsItemSelected(item);
    }

}
