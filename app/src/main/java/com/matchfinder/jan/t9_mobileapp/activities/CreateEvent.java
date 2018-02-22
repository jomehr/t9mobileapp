package com.matchfinder.jan.t9_mobileapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.matchfinder.jan.t9_mobileapp.util.PickerEditor;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Created by Taras on 20.11.2017.
 */

public class CreateEvent extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private int PLACE_PICKER_REQUEST = 1;
    private int REQUEST = 1;

    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private long eventDateAndTimeFinal = -1;
    private LatLng eventPlace;
    private int eventMaxPlayersNumber = -1;
    private String eventDescription = "";

    Button creatEventButton;

    TextView eventOrtText;
    TextView eventDateAndTimeText;
    TextView eventMaxPlayersNumberText;
    TextView eventDescriptionText;

    LinearLayout dateAndTimeLayout;
    LinearLayout maxPlayersLayout;
    LinearLayout descriptionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.create));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        creatEventButton = findViewById(R.id.createEvent_createButton);
        creatEventButton.setEnabled(false);

        eventOrtText =  findViewById(R.id.createEvent_eventOrtText);
        eventDateAndTimeText = findViewById(R.id.createEvent_eventDateAndTimeText);
        eventMaxPlayersNumberText =  findViewById(R.id.createEvent_eventMaxPlayersText);
        eventDescriptionText =  findViewById(R.id.createEvent_eventDescriptionText);

        dateAndTimeLayout =  findViewById(R.id.createEvent_eventDateAndTime);
        maxPlayersLayout =  findViewById(R.id.createEvent_eventMaxPlayers);
        descriptionLayout =  findViewById(R.id.createEvent_eventDescription);

        dateAndTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePickerDialog();
            }
        });

        maxPlayersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPickerDialog();
            }
        });

        descriptionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textPickerDialog();
            }
        });

        creatEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });

    }

    /**
     * Create an event and store eventData in ParseServer
     */
    private void createEvent() {

        ParseServer ps = ParseServer.getInstance(this);

        //Default eventPlace
        eventPlace = eventPlace == null? new LatLng(0,0) : eventPlace;

        ps.saveEventData(eventPlace.latitude, eventPlace.longitude, eventDateAndTimeFinal, eventMaxPlayersNumber, eventDescription);

        Toast.makeText(this, R.string.event_saved, Toast.LENGTH_SHORT).show();
    }

    //Place Picker
    public void pickAPlace(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        //TODO catch-Block implementieren
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                //Layout anpassen
                ViewGroup.LayoutParams params = eventOrtText.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                eventOrtText.setLayoutParams(params);

                //Button Color anpassen nur wenn zumindest ort, datum und uhrzeit gesetz sind
                if(eventDateAndTimeText.getText() != getResources().getString(R.string.setDateAndTime)) {
                    creatEventButton.setEnabled(true);
                    creatEventButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
                eventPlace = place.getLatLng();
                eventOrtText.setText(place.getAddress());
            }
        }
    }

    //Date/Time Picker
    //Zuerst wird datePickerDialog gestartet.
    //Wenn datum gesetzt ist wird sofort auch timePickerDialog gestartet.
    //Wenn auch Zeit gesetzt ist werden die Daten in TextView geändert.
    private void dateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this,CreateEvent.this,
                year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = day;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, CreateEvent.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        hourFinal = hour;
        minuteFinal = minute;

        //Layout anpassen
        ViewGroup.LayoutParams params = eventDateAndTimeText.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        eventDateAndTimeText.setLayoutParams(params);

        //Button Color anpassen nur wenn zumindest ort, datum und uhrzeit gesetz sind
        if(eventOrtText.getText() != getResources().getString(R.string.pick_place)) {
            creatEventButton.setEnabled(true);
            creatEventButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
        }

        //TextView füllen
        String dateAndTimeText = getResources().getString(R.string.date) + " " + String.format("%02d", dayFinal) + "." + String.format("%02d",monthFinal) + "." + yearFinal
                + "\n" + getResources().getString(R.string.time) + " " + String.format("%02d", hourFinal) + ":" + String.format("%02d", minuteFinal);
        eventDateAndTimeText.setText(dateAndTimeText);

        //set eventDateAndTimeFinal (long) number in milliseconds
        eventDateAndTimeFinal = convertDateToLong();

    }

    //Number Picker
    private void numberPickerDialog() {

        final NumberPicker myNumberPicker = new NumberPicker(this);
        PickerEditor.setNumberPickerTextColor(myNumberPicker, getColor(R.color.colorBlack));
        myNumberPicker.setMaxValue(22);
        myNumberPicker.setMinValue(2);


        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
        builder.setTitle(R.string.max_player_amount);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventMaxPlayersNumber = myNumberPicker.getValue();
                eventMaxPlayersNumberText.setText(String.format("%01d", eventMaxPlayersNumber));
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
        builder.setTitle(R.string.description);

        // Set up the input
        final EditText input = new EditText(this);
        input.setSingleLine(false);
        input.setMinLines(1);

        //Set margins
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(params);

        container.addView(input);

        if (!eventDescriptionText.getText().equals(getResources().getString(R.string.description)))
            input.setText(eventDescriptionText.getText());

        //View zuweisen
        builder.setView(container);

        //Tastatur öfnnen
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Tastatur schlissen
                //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                //Set TextViewLayout to WRAP_CONTENT
                ViewGroup.LayoutParams paramsDescriptionText = eventDescriptionText.getLayoutParams();
                paramsDescriptionText.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                eventDescriptionText.setLayoutParams(paramsDescriptionText);

                //Set eventDescriptionLayout (Linear Layout) paddingBottom to 10dp
                descriptionLayout.setPadding(0,0,0,10);

                eventDescription = input.getText().toString();
                eventDescriptionText.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                //Tastatur schlissen
                //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                dialog.cancel();
            }
        });

        builder.show();

        //Tastatur automatisch öfnnen
        input.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        /*
        TODO Beim Click auf Homebutton vor dem Click auf OK oder CANCEL schliesst sich die Tastatur nicht.
        Es muss eingestellt sein, dass die Tastatur sich immer beim App-Schliessen schliesst.
        */
    }

    /**
     * Method to convert Date and Time - String to long. number of milliseconds;
     * @return Date and Time as a number of milliseconds.
     */
    private long convertDateToLong() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String dateString = String.format("%02d", dayFinal) + "."+ String.format("%02d", monthFinal) + "." + yearFinal + " "
                + String.format("%02d", hourFinal) + ":" + String.format("%02d", minuteFinal) + ":00";
        return dateFormat.parse(dateString, new ParsePosition(0)).getTime();
    }

    //Menü
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
