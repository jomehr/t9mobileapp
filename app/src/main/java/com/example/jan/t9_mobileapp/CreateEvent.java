package com.example.jan.t9_mobileapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

/**
 * Created by Taras on 20.11.2017.
 */

public class CreateEvent extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private int PLACE_PICKER_REQUEST = 1;

    private int day, month, year, hour, minute;
    private int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    TextView eventOrt;
    TextView eventDateAndTime;
    TextView eventMaxPlayersNumber;
    LinearLayout dateAndTimeLayout;
    LinearLayout maxPlayersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Event erstellen");

        eventOrt = (TextView) findViewById(R.id.eventOrtText);
        eventDateAndTime = (TextView) findViewById(R.id.eventDateAndTimeText);
        eventMaxPlayersNumber = (TextView) findViewById(R.id.eventMaxPlayersText);

        dateAndTimeLayout = (LinearLayout) findViewById(R.id.eventDateAndTime);
        maxPlayersLayout = (LinearLayout) findViewById(R.id.eventMaxPlayers);


        dateAndTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this,CreateEvent.this,
                        year,month,day);
                datePickerDialog.show();

            }
        });

        maxPlayersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPickerDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void pickAPlace(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;

        try {
            intent = builder.build(CreateEvent.this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(CreateEvent.this, data);
                eventOrt.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, CreateEvent.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hourFinal = i;
        minuteFinal = i1;

        ViewGroup.LayoutParams params = eventDateAndTime.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        eventDateAndTime.setLayoutParams(params);

        eventDateAndTime.setText("Datum: "+ dayFinal + "."+ monthFinal + "." + yearFinal
                + "\nUhrzeit: " + String.format("%02d", hourFinal) + ":" + String.format("%02d", minuteFinal));


        //Toast.makeText(getApplicationContext(),
        //        "Datum: "+ dayFinal + "."+ minuteFinal + "." + yearFinal + ", Uhrzeit: " + hourFinal + ":" + minuteFinal,
        //        Toast.LENGTH_SHORT).show();

    }

    private void numberPickerDialog() {

        final NumberPicker myNumberPicker = new NumberPicker(getApplicationContext());
        myNumberPicker.setMaxValue(22);
        myNumberPicker.setMinValue(1);

        /* Unnötig

        NumberPicker.OnValueChangeListener myValueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                eventMaxPlayersNumber.setText(" " + newValue + " ");
            }
        };

        myNumberPicker.setOnValueChangedListener(myValueChangeListener);

        */

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
        builder.setTitle(R.string.maxAnzahl);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventMaxPlayersNumber.setText(" " + myNumberPicker.getValue() + " ");
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventMaxPlayersNumber.setText(R.string.maxAnzahl);
            }
        });

        builder.show();
    }
}
