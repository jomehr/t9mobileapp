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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Created by Jan on 13.11.2017.
 */

public class Profile extends AppCompatActivity {

    private static final String PREFER_NAME_PROFILDATA = "ProfilData";
    private SharedPreferences sharedPreferencesProf;

    private final static int RESULT_LOAD_IMAGE = 1;
    private final static int PERMISSION_REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView realName = findViewById(R.id.profile_realName);
        TextView birthdayText = findViewById(R.id.profile_ageValue);
        TextView descriptionText = findViewById(R.id.profile_descriptionText);
        TextView experienceText = findViewById(R.id.profile_experienceValue);
        TextView favouriteTeamText = findViewById(R.id.profile_favouriteTeamValue);
        TextView profileInitText = findViewById(R.id.profile_notInit);
        LinearLayout profileData = findViewById(R.id.profile_profileData);
        final Toolbar myToolbar = findViewById(R.id.profile_collapsingStaticToolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.profile_collapsingToolbar);
        AppBarLayout appBar = findViewById(R.id.profile_collapsingToolbarLayout);
        FloatingActionButton editBtn = findViewById(R.id.profile_editBtn);
        CircleImageView profilePicture = findViewById(R.id.profile_picture);
        ProgressBar progressBar = findViewById(R.id.profile_progressBar);

        //get data from shared pref
        sharedPreferencesProf = getSharedPreferences(PREFER_NAME_PROFILDATA, Context.MODE_PRIVATE);
        String profileName = sharedPreferencesProf.getString("EchterName", "Profil");

        //get data from server if userdata exist and sharedpred empty
        //TODO save profile data in sharedpref if sharedpref is empty but data exists on server
        ParseServer ps = ParseServer.getInstance(this);
        if (profileName.equals("Profil")) {
            progressBar.setVisibility(View.GONE);
            boolean profileInit = ParseUser.getCurrentUser().getBoolean("profileInit");
            if (profileInit) {
                ps.loadProfileData(this, profileInitText, profileData, realName, birthdayText, descriptionText, experienceText, favouriteTeamText);
            }
        } else if (!profileName.equals("Profil")) {
            progressBar.setVisibility(View.GONE);

            String profileBirthday = sharedPreferencesProf.getString("Geburtstag", null);
            String profileDescription = sharedPreferencesProf.getString("ProfilBeschreibung", null);
            String profileExperience = sharedPreferencesProf.getString("Erfahrung", null);
            String profileFavouriteTeam = sharedPreferencesProf.getString("Lieblingsteam", null);
            String picturePath = sharedPreferencesProf.getString("Profilbild", null);

            birthdayText.setText(profileBirthday);
            descriptionText.setText(profileDescription);
            experienceText.setText(profileExperience);
            favouriteTeamText.setText(profileFavouriteTeam);
            profilePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            profileInitText.setVisibility(View.GONE);
            profileData.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            profileInitText.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle(ParseUser.getCurrentUser().getUsername());
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
                        myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                        myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        editBtn.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, CreateProfile.class));
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfilePicture();
            }
        });

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }
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
            SharedPreferences.Editor editor = sharedPreferencesProf.edit();
            editor.putString("Profilbild", picturePath).apply();
            cursor.close();

            ImageView new_profilPicture = findViewById(R.id.profile_picture);
            new_profilPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
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
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            case R.id.action_sign_out:
                ParseServer ps = ParseServer.getInstance(this);
                if (ps.logOut()) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                } else {
                    Toast.makeText(this, "Fehler beim Logout", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
