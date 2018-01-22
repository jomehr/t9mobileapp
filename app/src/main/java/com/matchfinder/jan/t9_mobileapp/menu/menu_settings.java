package com.matchfinder.jan.t9_mobileapp.menu;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.activities.Homescreen;
import com.matchfinder.jan.t9_mobileapp.activities.Login;
import com.matchfinder.jan.t9_mobileapp.activities.Profile;
import com.matchfinder.jan.t9_mobileapp.activities.Search;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;


/*
 * Created by Christopher on 31.12.2017.
 */

public class menu_settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_settings);

        Button defaultButton = findViewById(R.id.defaultButton);
        Button redButton = findViewById(R.id.redButton);
        Button yellowButton = findViewById(R.id.yellowButton);

        final EditText to = findViewById(R.id.sendTo);
        final EditText subject =  findViewById(R.id.subject);
        final EditText message =  findViewById(R.id.EmailText);
        Button sendE = findViewById(R.id.sendEmail);

        defaultButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        redButton.setBackgroundColor(getResources().getColor(R.color.colorRed));
        yellowButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        final Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle(R.string.settings);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorRed)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorRed));
                }
            }
        });

        /*redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myToolbar.setBackgroundColor(Color.GRAY);
            }
        });*/


        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Homescreen.class));
            }
        });

        sendE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toS = to.getText().toString();
                String subS = subject.getText().toString();
                String mesS = message.getText().toString();

                Intent email = new Intent (Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, toS);
                //email.putExtra(Intent.EXTRA_EMAIL, new String[], new String[]{toS});
                email.putExtra(Intent.EXTRA_SUBJECT, subS);
                email.putExtra(Intent.EXTRA_TEXT, mesS);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose app to send your feedback"));

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

