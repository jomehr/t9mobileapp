package com.kick_it.jan.t9_mobileapp.aktivitaeten;

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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.kick_it.jan.t9_mobileapp.R;

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

    //private String fileName = "eventDataFile.txt";
    //private String filePath = "myExternalFilePath";

    //File myExternalFile;
    //String myData = "";

    LatLng eventLatLng;

    Button creatEvent;

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

        creatEvent = (Button) findViewById(R.id.createEvent_createButton);

        eventOrtText = (TextView) findViewById(R.id.createEvent_eventOrtText);
        eventDateAndTimeText = (TextView) findViewById(R.id.createEvent_eventDateAndTimeText);
        eventMaxPlayersNumberText = (TextView) findViewById(R.id.createEvent_eventMaxPlayersText);
        eventDescriptionText = (TextView) findViewById(R.id.createEvent_eventDescriptionText);

        dateAndTimeLayout = (LinearLayout) findViewById(R.id.createEvent_eventDateAndTime);
        maxPlayersLayout = (LinearLayout) findViewById(R.id.createEvent_eventMaxPlayers);
        descriptionLayout = (LinearLayout) findViewById(R.id.createEvent_eventDescription);

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

        creatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creatEventData();
            }
        });

        /*
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            creatEvent.setEnabled(false);
        } else {
            myExternalFile = new File(getExternalFilesDir(filePath), fileName);
        }
        */
    }


    private void creatEventData() {
    /*    if (eventDateAndTimeText.getText() != getResources().getString(R.string.setDateAndTime)
                && eventOrtText.getText() != getResources().getString(R.string.ort_auswahl)
                && eventLatLng != null)
        {
            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                String data = eventLatLng.latitude + "::"
                        + eventLatLng.longitude + "::"
                        + eventOrtText + "::"
                        + dayFinal + "::"
                        + monthFinal + "::"
                        + yearFinal + "::"
                        + hourFinal + "::"
                        + minuteFinal + "::"
                        + eventMaxPlayersNumberText + "::"
                        + eventDescriptionText;
                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "SampleFile.txt saved to External Storage...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Bitte füllen Sie alle Daten", Toast.LENGTH_SHORT).show();
        }
        */
    }
 /*
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
*/


    //Menü
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }



    //Place Picker
    public void pickAPlace(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
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
                if(eventDateAndTimeText.getText() != getResources().getString(R.string.setDateAndTime))
                    creatEvent.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));

                eventLatLng = place.getLatLng();
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
        if(eventOrtText.getText() != getResources().getString(R.string.pick_place))
            creatEvent.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));

        //TextView füllen
        String dateAndTimeText = "Datum: "+ dayFinal + "."+ monthFinal + "." + yearFinal
                + "\nUhrzeit: " + String.format("%02d", hourFinal) + ":" + String.format("%02d", minuteFinal);
        eventDateAndTimeText.setText(dateAndTimeText);

    }



    //Number Picker
    private void numberPickerDialog() {

        final NumberPicker myNumberPicker = new NumberPicker(getApplicationContext());
        myNumberPicker.setMaxValue(22);
        myNumberPicker.setMinValue(1);

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
        builder.setTitle(R.string.max_amount);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eventMaxPlayersNumberText.setText(String.format("%01d", myNumberPicker.getValue()));
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
        TODO Beim Click auf Homebutton vor dem Click auf OK oder CANCEL schkiesst sich die Tastatur
        TODO Es muss eingestellt sein, dass die Tastatur sich immer beim App-Schliessen schliesst.
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivityIfNeeded(new Intent(this, Search.class), REQUEST);
                return true;
            case R.id.action_profile:
                startActivityIfNeeded(new Intent(this, Profile.class), REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
