package com.example.ptvimproved24;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

import java.util.List;

public class RouteDetails extends AppCompatActivity {
    // Used for saved routes, showing both direction
    private static final String TAG = "RouteDetails";
    private MapView mMapView;
    private float latitude=-37.818078f;
    private float longitude=144.96681f;
    private static final Geopoint FlinderSt = new Geopoint(-37.818078, 144.96681);
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    int REQUEST_LOCATION =99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserLocation();
        int routeid = getIntent().getIntExtra("routeid",1); // Get Route details to display

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setContentView(R.layout.activity_routedetails);
        mMapView = new MapView(this, MapRenderMode.RASTER);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout) findViewById(R.id.map_view)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);


        if (ActivityCompat.checkSelfPermission(RouteDetails.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Location();
        }else{
            ActivityCompat.requestPermissions(RouteDetails.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
    }



    private void getUserLocation() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(RouteDetails.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(RouteDetails.this, "Location Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject Location permission,some service may not available\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    private void Location(){
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(10000);
        request.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // Get near stop
            } catch (ApiException e) {
                switch (e.getStatusCode()){
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(RouteDetails.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException sendIntentException){
                            sendIntentException.printStackTrace();
                        }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (ActivityCompat.checkSelfPermission(RouteDetails.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Location();
        }else{
            ActivityCompat.requestPermissions(RouteDetails.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        if (ActivityCompat.checkSelfPermission(RouteDetails.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            locationManager.removeUpdates((LocationListener)this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getGeoLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                }
            });
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("locationfragStopselect:"+location);
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } catch (SecurityException e) {
            Toast.makeText(this, "GPS Permission has been disabled", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
