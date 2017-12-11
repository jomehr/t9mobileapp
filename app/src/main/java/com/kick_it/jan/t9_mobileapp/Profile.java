package com.kick_it.jan.t9_mobileapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Jan on 13.11.2017.
 */

public class Profile extends AppCompatActivity {

    private static final String PREFER_NAME = "Registration";
    private SharedPreferences sharedPreferences;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 0;
    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(PREFER_NAME, 0);
        String profileName = sharedPreferences.getString("Name", null);

        coordinatorLayout =  (CoordinatorLayout) findViewById(R.id.profile_coordinatorLayout);
        final Toolbar myToolbar = findViewById(R.id.profile_collapsingStaticToolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsingToolbar);
        AppBarLayout appBar = (AppBarLayout)findViewById(R.id.profile_collapsingToolbarLayout);
        final View optionalData = (View) findViewById(R.id.profile_infoOptional);
        FloatingActionButton editBtn = (FloatingActionButton) findViewById(R.id.profile_editBtn);
        ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(profileName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        optionalData.setVisibility(View.GONE);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                    myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorPrimaryText), PorterDuff.Mode.SRC_ATOP);
                } else {
                    myToolbar.getOverflowIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                    myToolbar.getNavigationIcon().setColorFilter(getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        editBtn.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(coordinatorLayout,"Dieses Feature ist noch in Bearbeitung", Snackbar.LENGTH_SHORT).show();
                optionalData.setVisibility(View.VISIBLE);
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfilePicture();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView new_profilPicture = (ImageView) findViewById(R.id.profile_picture);
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
}
