package com.matchfinder.jan.t9_mobileapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_data_privacy;
import com.matchfinder.jan.t9_mobileapp.menu.menu_developer;
import com.matchfinder.jan.t9_mobileapp.menu.menu_faq;
import com.matchfinder.jan.t9_mobileapp.menu.menu_settings;
import com.matchfinder.jan.t9_mobileapp.util.PermissionUtils;

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

    private LocationManager myLocationManager;
    private String bestLocationProvider;
    private LocationProvider myLocationProvider;
    /// TEST
    private Location currentLocation;
    ///

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Listener für "click" auf EventRadar_Button
        ImageButton btn_Add = findViewById(R.id.eventRadar_btnAdd);
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
        enableMyLocation();

        ///TEST

        ParseServer ps = ParseServer.getInstance(getApplicationContext());
        ps.loadEventData(this);
        LatLng kosivSchool = new LatLng(ParseServer.event.getPlaceLatitude(), ParseServer.event.getPlaceLongitude());
        myGoogleMap.addMarker(new MarkerOptions().position(kosivSchool).title(ParseServer.event.getObjectId() + ": " + ParseServer.event.getDescription()));

        //myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kosivSchool, 17.0f));

        ///

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

            //Move to current Position
            if (myLocationManager == null || myLocationProvider == null) {
                myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(true);
                criteria.setSpeedRequired(false);

                bestLocationProvider = myLocationManager.getBestProvider(criteria, true);
                myLocationProvider = myLocationManager.getProvider(bestLocationProvider);
            }
            Location loc = myLocationManager.getLastKnownLocation(bestLocationProvider);
            myGoogleMap.getMyLocation();
            loc = loc == null? myLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER): loc;
            loc = loc == null? myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER): loc;
            loc = loc == null? myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER): loc;
            if (loc == null) {
                /// TEST

                // Fetch a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
                // Identify a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {

                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        currentLocation = location;
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };

                // Register the listener with the Location Manager to receive location updates

                ///DEBUG

                if (locationManager.isProviderEnabled(bestLocationProvider)){
                    locationManager.setTestProviderLocation(bestLocationProvider, new Location(bestLocationProvider));
                    //locationManager.sendExtraCommand()
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(bestLocationProvider, 2 * 60 * 1000, 10, locationListener);
                    Log.d("Location", "bestLocationProvider");
                    if (currentLocation == null) {
                        Log.d("Location", "bestLocationProvider hat null zurückgegeben");
                        System.out.println("bestLocationProvider hat null zurückgegeben");
                    }
                    else {
                        Log.d("Location", "bestLocationProvider hat LOCATION zurückgegeben");
                        System.out.println("bestLocationProvider hat LOCATION zurückgegeben");
                    }
                }
                else {
                    Log.d("Location", "bestLocationProvider is not enabled.");
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2 * 60 * 1000 , 10, locationListener);
                    Log.d("Location", "GPS_PROVIDER");
                    if (currentLocation == null) {
                        Log.d("Location", "GPS_PROVIDER hat null zurückgegeben");
                        System.out.println("GPS_PROVIDER hat null zurückgegeben");
                    }
                    else {
                        Log.d("Location", "GPS_PROVIDER hat LOCATION zurückgegeben");
                        System.out.println("GPS_PROVIDER hat LOCATION zurückgegeben");
                    }
                }
                else {
                    Log.d("Location", "GPS is not enabled.");
                }

                if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2 * 60 * 1000, 10, locationListener);
                    Log.d("Location", "PASSIVE_PROVIDER");
                    if (currentLocation == null) {
                        Log.d("Location", "PASSIVE_PROVIDER hat null zurückgegeben");
                        System.out.println("PASSIVE_Provider hat null zurückgegeben");
                    }
                    else {
                        Log.d("Location", "PASSIVE_PROVIDER hat LOCATION zurückgegeben");
                        System.out.println("Passive_Provider hat LOCATION zurückgegeben");
                    }
                }
                else {
                    Log.d("Location", "PASSIVE_PROVIDER is not enabled.");
                }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2 * 60 * 1000, 10, locationListener);
                    Log.d("Location", "NETWORK_PROVIDER");
                    if (currentLocation == null) {
                        Log.d("Location", "NETWORK_PROVIDER hat null zurückgegeben");
                        System.out.println("Network_Provider hat null zurückgegeben");
                    }
                    else {
                        Log.d("Location", "NETWORK_PROVIDER hat LOCATION zurückgegeben");
                        System.out.println("Network_Provider hat LOCATION zurückgegeben");
                    }
                }
                else {
                    Log.d("Location", "Newtwork_Provider is not enabled.");
                }

                loc = currentLocation;
                ///
            }
            /*
            new AlertDialog.Builder(EventRadar.this)
                    .setTitle("Please activate location")
                    .setMessage("Click ok to goto settings else exit.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .show();
             */
            double latitude = loc.getLatitude();
            double longitude = loc.getLongitude();
            LatLng currentLatLng = new LatLng(latitude,longitude);
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
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
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
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
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
