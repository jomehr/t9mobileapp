package com.kick_it.jan.t9_mobileapp.aktivitaeten;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kick_it.jan.t9_mobileapp.menu.*;
import com.kick_it.jan.t9_mobileapp.schnittstellen.InputFilterMinMax;
import com.kick_it.jan.t9_mobileapp.R;


/*
 * Created by Jan on 18.12.2017.
 */

public class ProfileEdit extends AppCompatActivity {

    private int mDay, mMonth, mYear;
    private EditText editDay, editMonth, editYear;
    private LinearLayout descriptionLayout;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledit);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.profiledit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton submitBtn = findViewById(R.id.profiledit_submitbtn);
        editDay = findViewById(R.id.profiledit_ageDay);
        editMonth = findViewById(R.id.profiledit_ageMonth);
        editYear = findViewById(R.id.profiledit_ageYear);
        descriptionLayout = findViewById(R.id.profiledit_descriptionLayout);
        LinearLayout residenceLayout = findViewById(R.id.profiledit_residenceLayout);
        descriptionText = findViewById(R.id.profiledit_descriptionText);

        editDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 31)});
        editMonth.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 12)});
        editYear.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 2018)});

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) {
                    onValidationFailed();
                } else {
                    saveData();
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
                Toast.makeText(ProfileEdit.this, "Feature in Arbeit", Toast.LENGTH_SHORT).show();
            }
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
                startActivity(new Intent(this, Login.class));
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate() {
        boolean valid = true;

        mDay = Integer.parseInt(editDay.getText().toString());
        mMonth = Integer.parseInt(editMonth.getText().toString());
        mYear = Integer.parseInt(editYear.getText().toString());

        if (mYear < 1930 || mYear > 2018) {
            editYear.setError("invalides Jahr!");
            valid = false;
        }else {
            editDay.setError(null);
        }

        if (mDay > 30 && (mMonth == 2 || mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11)) {
            editDay.setError("invalides Datum!");
            valid = false;
        }else {
            editDay.setError(null);
        }

        if(mDay == 29 && mMonth == 2 && (mYear%4 != 0 && (mYear%100 == 0 || mYear%400 != 0))) {
            editDay.setError("invalides Datum!");
            valid = false;
        }else {
            editDay.setError(null);
        }

        return  valid;
    }

    private void onValidationFailed() {
        Toast.makeText(getApplicationContext(), "Validierung fehlgeschlagen",Toast.LENGTH_SHORT).show();
    }

    //Text Picker
    private void textPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //String descText = descriptionText.toString();
        builder.setTitle(R.string.description);

        // Set up the input
        final EditText input = new EditText(this);
        input.setSingleLine(false);
        input.setMinLines(1);
        input.setMaxLines(6);
        input.setHint(descriptionText.getHint());

        //Set margins
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(params);

        container.addView(input);

        input.setText(descriptionText.getText());

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
                ViewGroup.LayoutParams paramsDescriptionText = descriptionText.getLayoutParams();
                paramsDescriptionText.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                descriptionText.setLayoutParams(paramsDescriptionText);

                //Set eventDescriptionLayout (Linear Layout) paddingBottom to 10dp
                descriptionLayout.setPadding(0,0,0,10);

                descriptionText.setText(input.getText().toString());
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

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("ProfilData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String appendDate = mDay + "." + mMonth + "." +mYear;
        editor.putString("Geburtstag", appendDate);
        editor.putString("ProfilBeschreibung", descriptionText.getText().toString());
        editor.apply();
    }

}
