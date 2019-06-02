package com.esdrasmorais.inspetoronline.ui;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.MySingleton;
import com.esdrasmorais.inspetoronline.data.SecurityPreferences;
import com.esdrasmorais.inspetoronline.ui.listeners.OpenInspectionListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class TaskManagerActivity extends AppCompatActivity implements OnMapReadyCallback {
    Boolean mLocationPermissionGranted = false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    GoogleMap mMap = null;
    //    GeoDataClient mGeoDataClient = null;
//    PlaceDetectionClient mPlaceDetectionClient = null;
    FusedLocationProviderClient fusedLocationProviderClient = null;
    Task<Location> locationResult = null;
    Location mLastKnownLocation = null;
    private static final String TAG = "TaskManagerActivity";
    private static final Integer DEFAULT_ZOOM = 20; //Buildings
    LatLng mDefaultLocation;
    private static final Integer REQUEST_CHECK_SETTINGS = 1;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final Integer UPDATE_INTERVAL = 7 * 1000;
    private static final Integer FASTEST_INTERVAL = 2;

    public TaskManagerActivity() {
        this.mDefaultLocation = new LatLng(-23.4862562, -46.7285661);
    }

    private void setUpMap() {
        // Construct a GeoDataClient.
//        mGeoDataClient = Places.getGeoDataClient(this, null);L
//
//        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(
//                this, null
//        );

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpMap();

        FloatingActionButton fabAddInspection = findViewById(R.id.fab_add_inspection);
        fabAddInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OpenInspectionListener(view);
            }
        });

        createLocationCallback();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(
                this,
                new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults
    ) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocation(Location location) {
        if (mMap != null) {
            storeLocation(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(),
                        location.getLongitude()), DEFAULT_ZOOM));
            mMap.addMarker(new MarkerOptions()
                .title("Voce esta")
                .position(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .snippet("aqui"));
        }
    }

    private void createLocationCallback() {
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocation(location);
                }
            }
        };
    }

    private void startLocationUpdates() {
        try {
            if (fusedLocationProviderClient != null && mLocationPermissionGranted &&
                ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, null
                );
            }
        } catch (Exception ex) {
            Log.e("startLocationUpdates()", ex.getMessage());
        }
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (locationSettingsResponse == null) return;
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(
                            TaskManagerActivity.this, REQUEST_CHECK_SETTINGS
                        );
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e("createLocationRequest()", sendEx.getMessage());
                    }
                }
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        getLocationPermission();
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void storeLocation(Location location) {
        SecurityPreferences securityPreferences = new SecurityPreferences(
            this
        );
        securityPreferences.storeString(
            "last_know_location", new Gson().toJson(location)
        );
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
//                this.locationResult = fusedLocationProviderClient.getLastLocation();
//                this.locationResult.addOnCompleteListener(this, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            storeLocation(mLastKnownLocation);
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                new LatLng(mLastKnownLocation.getLatitude(),
//                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                            mMap.addMarker(new MarkerOptions()
//                                .title("Voce esta")
//                                .position(new LatLng(mLastKnownLocation.getLatitude(),
//                                    mLastKnownLocation.getLongitude()))
//                                .snippet("aqui"));
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                mDefaultLocation, DEFAULT_ZOOM
//                            ));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
                fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                updateLocation(location);
                            }
                        }
                    }
                );
            }
        } catch(SecurityException e)  {
            Log.e("getDeviceLocation()", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        createLocationRequest();

        this.getDeviceLocation();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MySingleton.getInstance(this).stop(TAG);
        stopLocationUpdates();
    }
}