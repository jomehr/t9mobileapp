package com.kick_it.jan.t9_mobileapp.aktivitaeten;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kick_it.jan.t9_mobileapp.InputFilterMinMax;
import com.kick_it.jan.t9_mobileapp.R;


/**
 * Created by Jan on 18.12.2017.
 */

public class ProfileEdit extends AppCompatActivity {


    private int mDay, mMonth, mYear;
    private EditText editDay, editMonth, editYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiledit);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profil bearbeiten");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton submitBtn = findViewById(R.id.profiledit_submitbtn);
        editDay = findViewById(R.id.profiledit_ageDay);
        editMonth = findViewById(R.id.profiledit_ageMonth);
        editYear = findViewById(R.id.profiledit_ageYear);

        editDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 31)});
        editMonth.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 12)});
        editYear.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 2018)});

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ProfileEdit.this, "hi", Toast.LENGTH_SHORT).show();
                if(!validate()) {
                    onValidationFailed();
                    return;
                }
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validate() {
        boolean valid = true;

        mDay = Integer.parseInt(editDay.getText().toString());
        mMonth = Integer.parseInt(editMonth.getText().toString());
        mYear = Integer.parseInt(editYear.getText().toString());

        if (mYear < 1930 || mYear > 2018) {
            editDay.setError("invalides Jahr!");
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

    public void onValidationFailed() {
        Toast.makeText(getApplicationContext(), "Validierung fehlgeschlagen",Toast.LENGTH_SHORT).show();
    }

}
