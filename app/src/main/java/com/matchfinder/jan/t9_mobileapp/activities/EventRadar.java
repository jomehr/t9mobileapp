package com.matchfinder.jan.t9_mobileapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.matchfinder.jan.t9_mobileapp.util.PermissionUtils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Updated by taraszaika on 20.11.17.
 *
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class EventRadar extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    /**
     * Request code for location permission request.
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap myGoogleMap;
    private Marker myMarker;

    private LocationManager myLocationManager;
    private String bestLocationProvider;
    private LocationProvider myLocationProvider;

    private Location currentLocation;
    private SharedPreferences sharedPreferencesLastLocation;
    private LocationListener gpsLocationListener;
    private LocationListener networkLocationListener;

    private final int REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_radar);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setTitle(R.string.home_eventradar);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferencesLastLocation = getSharedPreferences("Location", Context.MODE_PRIVATE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Listener für "click" auf EventRadar_Button
        FloatingActionButton btn_Add = findViewById(R.id.eventRadar_btnAdd);
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityIfNeeded(new Intent(EventRadar.this, CreateEvent.class), REQUEST);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        myGoogleMap = googleMap;

        myGoogleMap.setOnMyLocationButtonClickListener(this);
        myGoogleMap.setOnMyLocationClickListener(this);
        myGoogleMap.setOnMarkerClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        else {
            enableMyLocation();
            initWithPermission();
        }

        /// TEST

        ParseServer ps = ParseServer.getInstance(this);
        ps.loadEventData(this, myGoogleMap);

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (myGoogleMap != null) {
            // Access to the location has been granted to the app.
            myGoogleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Initialisieren with permmission. First steps. But only after permission check.
     */
    private void initWithPermission() {
        if (!getMyLocation()) {
            if(!locationServicesCheck(myLocationManager)) {
                // location services are enabled
                LatLng germanyLatLng = new LatLng(50.980602, 10.314458);
                myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(germanyLatLng, 6));
                Toast.makeText(this,"Your Location-Services are enabled\n" +
                        "You can use some 'My Location' button.", Toast.LENGTH_LONG).show();

            }
            else {
                // location services are disabled
                LatLng germanyLatLng = new LatLng(50.980602, 10.314458);
                myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(germanyLatLng, 6));
                Toast.makeText(this,"Your Location-Services are probably disabled. You can not use some features.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            locationServicesCheck(myLocationManager);
            //if(locationServicesCheck(myLocationManager)){
            //   enableMyLocation();
            //}
        }

    }

    /**
     * Initialisieren without permmission. First steps. But only after permission check.
     */
    private void initWithoutPermission() {
        // location services are disabled
        LatLng germanyLatLng = new LatLng(50.980602, 10.314458);
        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(germanyLatLng, 6));
        Toast.makeText(this,"Your Location-Services are probably disabled\n" +
                "You can not use some features.", Toast.LENGTH_LONG).show();
    }

    /**
     * Tries to get {@link Location} of Device. First of all it checkes last known {@link Location}. If no one is available it requests for one.
     * @return true if location found, flase - if not.
     */
    private boolean getMyLocation() {

        // Permission Chek
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

        // Move to current Position
        if (myLocationManager == null || myLocationProvider == null) {
            myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(true);
            criteria.setSpeedRequired(false);

            bestLocationProvider = myLocationManager.getBestProvider(criteria, true);
            if (bestLocationProvider == null) {
                return false;
            }
            else {
                myLocationProvider = myLocationManager.getProvider(bestLocationProvider);
            }
        }

        // Find last location
        Location lastLocation = myLocationManager.getLastKnownLocation(bestLocationProvider);
        lastLocation = lastLocation == null? myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER): lastLocation;
        lastLocation = lastLocation == null? myLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER): lastLocation;
        lastLocation = lastLocation == null? currentLocation: lastLocation;

        currentLocation = lastLocation;

        double sharedLatitude = sharedPreferencesLastLocation.getString("Latitude", null) != null ?
                Double.parseDouble(sharedPreferencesLastLocation.getString("Latitude", null)) : 0;
        double sharedLongitude = sharedPreferencesLastLocation.getString("Longitude", null) != null ?
                Double.parseDouble(sharedPreferencesLastLocation.getString("Longitude", null)) : 0;

        if(currentLocation == null) {
            if (sharedLatitude == 0 && sharedLongitude == 0) {
                requestLocation();
                return false;
            }
            else {
                myLocationManager.removeUpdates(getGpsLocationListener());
                myLocationManager.removeUpdates(getNetworkLocationListener());
                LatLng currentLatLng = new LatLng(sharedLatitude, sharedLongitude);
                myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
                return true;
            }
        }

        else {
            // Move cammera to my Location
            myLocationManager.removeUpdates(getGpsLocationListener());
            myLocationManager.removeUpdates(getNetworkLocationListener());
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
            LatLng currentLatLng = new LatLng(latitude, longitude);
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
            return true;
        }

    }

    /**
     * Check if Location Services are enabled. If they are not, start dialog window and settings.
     * @param lm LocatinManager provide information about Location-Services.
     */
    private boolean locationServicesCheck(LocationManager lm) {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Log.d("Location","Probable GPS_PROVIDER is null");
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            Log.d("Location","Probable NETWORK_PROVIDER is null");
            ex.printStackTrace();
        }

        if(!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.please_activate_location_services);
            dialog.setMessage("Click ok to goto settings else exit.");
            dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    paramDialogInterface.cancel();
                }
            });
            dialog.show();
        }
        else {
            return true;
        }

        // Check if the user has enabled the location data.
        // !!!NEVER USED BECOUSE of MULTI-THREAD
        // TODO Implement in right way "Check if the user has enabled the location data."
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            Log.d("Location","Probable GPS_PROVIDER is null");
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            Log.d("Location","Probable NETWORK_PROVIDER is null");
            ex.printStackTrace();
        }
        return !gps_enabled && !network_enabled;
    }

    /**
     * Request for Location with GPS_PROVIDER and NETWORK_PROVIDER
     */
    private void requestLocation(){

        // Permission Chek
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

        LocationListener gpsListener = getGpsLocationListener();
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
        LocationListener networkListener = getNetworkLocationListener();
        myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
    }

    /**
     * Get GPS_PROVIDER {@link LocationListener}
     * @return LocationListener
     */
    private LocationListener getGpsLocationListener() {
        return gpsLocationListener != null ? gpsLocationListener: new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                SharedPreferences.Editor editor = sharedPreferencesLastLocation.edit();
                editor.putString("Latitude", Double.toString(location.getLatitude()));
                editor.putString("Longitude", Double.toString(location.getLongitude()));
                editor.apply();
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    /**
     * Get NETWORK_PROVIDER {@link LocationListener}
     * @return LocationListener
     */
    private LocationListener getNetworkLocationListener() {
        return networkLocationListener != null ? networkLocationListener: new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                SharedPreferences.Editor editor = sharedPreferencesLastLocation.edit();
                editor.putString("Latitude", Double.toString(location.getLatitude()));
                editor.putString("Longitude", Double.toString(location.getLongitude()));
                editor.apply();
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    /**
     * Tries to find current {@link Location}. If found one, animates the camera to the user's current position.
     * @return boolean
     */
    @Override
    public boolean onMyLocationButtonClick() {
        locationServicesCheck(myLocationManager);
        Toast.makeText(this, R.string.your_location_is_being_searched, Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Make toast on "my location point" Click.
     * @param location my current location point.
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final String id = marker.getTitle();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e==null) {
                    showAlertDialog(object);
                }
                else {
                    Toast.makeText(EventRadar.this, e.getMessage()+ " " +id , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;
    }

    /**
     * Method to convert Date and Time - milliseconds to String.
     * @return Date and Time as a String of format dd.MM.yyyy HH:mm:ss.
     */
    private String convertLongToDate(long eventDateAndTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date eventDate = new Date(eventDateAndTime);
        return  dateFormat.format(eventDate);
    }

    private void showAlertDialog(final ParseObject object) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_event_join, null);

        TextView eventDateAndTimeText = dialogLayout.findViewById(R.id.dialog_event_join_eventDateAndTimeText);
        TextView eventMaxPlayersText = dialogLayout.findViewById(R.id.dialog_event_join_eventMaxPlayersText);
        TextView eventDescriptionText = dialogLayout.findViewById(R.id.dialog_event_join_eventDescriptionText);

        AlertDialog.Builder alert = new AlertDialog.Builder(EventRadar.this);
        alert.setTitle(R.string.join_event);
        // this is set the view from XML inside AlertDialog
        alert.setView(dialogLayout);

        // Set up the buttons
        alert.setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseServer.getInstance(EventRadar.this).addParticipantsToEvent(EventRadar.this, object.getObjectId());
            }
        });

        alert.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

        // Set Date And Time Text
        String dateAndTimeString = convertLongToDate(object.getLong("dateAndTime"));
        String[] dateAndTime = dateAndTimeString.split(" ");

        dateAndTimeString = getResources().getString(R.string.date) + " " + dateAndTime[0] +
                "\n" + getResources().getString(R.string.time) + " " + dateAndTime[1];
        eventDateAndTimeText.setText(dateAndTimeString);

        // Set Max Players Number
        String maxPlayersString = getResources().getString(R.string.players_amount) + " " + Integer.toString(object.getInt("maxPlayersNumber"));
        eventMaxPlayersText.setText(maxPlayersString);

        // Set Description Text
        String descriptionString = object.getString("description");
        eventDescriptionText.setText(descriptionString);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
            initWithPermission();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            initWithoutPermission();
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            initWithoutPermission();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
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
                } else {
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
