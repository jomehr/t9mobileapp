package com.matchfinder.jan.t9_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;

public class Homescreen extends AppCompatActivity {

    private static final int REQUEST = 1;
    private static final String TAG = "HOMESCREEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setTitle(R.string.app_name);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //Listener für "click" auf Banner1
        RelativeLayout banner1 = findViewById(R.id.homescreen_findGameContainer);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    startActivityIfNeeded(new Intent(Homescreen.this, EventRadar.class), REQUEST);
                }
                else {
                    Toast.makeText(Homescreen.this, "Internetverbindung fehlt. Es ist notwendig für diese Funktion", Toast.LENGTH_LONG).show();
                }

            }
        });

        RelativeLayout banner2 = findViewById(R.id.homescreen_createEventContainer);
        banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, CreateEvent.class));
            }
        });

        RelativeLayout banner3 = findViewById(R.id.homescreen_leagueContainer);
        banner3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, LeagueOverview.class));
            }
        });

        /*
        //TeamButton
        RelativeLayout banner4 = findViewById(R.id.banner_btn4);
        banner4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homescreen.this, Team.class));
            }
        });
        */
    }

    /**
     * Check if Network is Available becaouse it is very important to soma features.
     * @return boolean true if Network is available and false if not.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
                //ParseServer ps =ParseServer.getInstance(this);
                /*if (ps.logOut()) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                  }else {
                    Toast.makeText(this, "Fehler beim Logout",Toast.LENGTH_SHORT).show();
                }*/
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    Log.d(TAG, "signOut:success");
                    startActivity(new Intent(this, Login.class));
                    finish();
                } else {
                    Log.w(TAG, "signOut:failure");
                    Toast.makeText(Homescreen.this, "Abmeldung fehlgeschlagen", Toast.LENGTH_SHORT).show();
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
