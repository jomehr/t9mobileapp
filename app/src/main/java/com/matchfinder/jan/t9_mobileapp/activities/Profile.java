package com.matchfinder.jan.t9_mobileapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.parse.ParseUser;

/*
 * Created by Jan on 13.11.2017.
 */

public class Profile extends AppCompatActivity {


    private static  final String PREFER_NAME_PROFILDATA = "ProfilData";
    private SharedPreferences sharedPreferencesProf;
    private SharedPreferences.Editor editor;


    private final static int RESULT_LOAD_IMAGE = 1;
    private final static int PERMISSION_REQUEST_STORAGE = 0;
    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get data from shared pref
        sharedPreferencesProf = getSharedPreferences(PREFER_NAME_PROFILDATA, Context.MODE_PRIVATE);
        editor = sharedPreferencesProf.edit();
        String profileName = sharedPreferencesProf.getString("Name", "Profil");
        String profileBirthday = sharedPreferencesProf.getString("Geburtstag", null);
        String profileDescription = sharedPreferencesProf.getString("ProfilBeschreibung", null);
        String profileExperience = sharedPreferencesProf.getString("Erfahrung", null);
        String profileFavouriteTeam = sharedPreferencesProf.getString("Lieblingsteam", null);
        String picturePath = sharedPreferencesProf.getString("Profilbild", null);

        //get data from server
        ParseServer.getInstance(this);
        //TODO load data from server if userdata exists and sharedpref is empty
        ParseServer.getInstance(this);
        if (profileName.equals("Profil")) {
            profileName = ParseUser.getCurrentUser().getUsername();
            editor.putString("Name", profileName);
        }

        coordinatorLayout =   findViewById(R.id.profile_coordinatorLayout);
        TextView birthdayText = findViewById(R.id.profile_ageValue);
        TextView descriptionText = findViewById(R.id.profile_descriptionText);
        TextView experienceText = findViewById(R.id.profile_experienceValue);
        TextView favouriteTeamText = findViewById(R.id.profile_favouriteTeamValue);
        final Toolbar myToolbar = findViewById(R.id.profile_collapsingStaticToolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.profile_collapsingToolbar);
        AppBarLayout appBar = findViewById(R.id.profile_collapsingToolbarLayout);
        FloatingActionButton editBtn = findViewById(R.id.profile_editBtn);
        ImageView profilePicture =  findViewById(R.id.profile_picture);

        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle(profileName);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    try {
                        myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    try {
                        myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    try {
                        myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (profileBirthday != null) {
            birthdayText.setText(profileBirthday);
        }
        if (profileDescription != null) {
            descriptionText.setText(profileDescription);
        }
        if (profileExperience != null) {
            experienceText.setText(profileExperience);
        }
        if (profileFavouriteTeam != null) {
            favouriteTeamText.setText(profileFavouriteTeam);
        }

        editBtn.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, ProfileEdit.class));
                finish();
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfilePicture();
            }
        });

        if(picturePath != null)
        {
            profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu2, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            try {
                cursor.moveToFirst();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            editor = sharedPreferencesProf.edit();
            editor.putString("Profilbild", picturePath).apply();
            cursor.close();

            ImageView new_profilPicture = findViewById(R.id.profile_picture);
            new_profilPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    private void loadProfilePicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(coordinatorLayout, "Speicherzugang wird benötigt um auf Bilder zugreifen zu können",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(Profile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            }).show();

        } else {
            Snackbar.make(coordinatorLayout,
                    "Speicherzugang nicht vorhanden. Zugang freigeben?",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }
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
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            case R.id.action_sign_out:
                startActivity(new Intent(this, Login.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
