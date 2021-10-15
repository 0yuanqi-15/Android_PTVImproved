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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

import java.util.ArrayList;
import java.util.List;

public class RouteDirections extends AppCompatActivity {
    // Used for saved routes, showing both direction
    private static final String TAG = "RouteDirections";
    private MapView mMapView;
    private static final Geopoint FlinderSt = new Geopoint(-37.818078, 144.96681);
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    String provider;

    private static final int REQUEST_LOCATION = 99;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_routedirections);
        mMapView = new MapView(this, MapRenderMode.VECTOR);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout) findViewById(R.id.map_view)).addView(mMapView);

        mMapView.onCreate(savedInstanceState);

        int routeid = getIntent().getIntExtra("routeid",1); // Get Route details to display
//        getRoutePathById(routeid);
        // looking up route route, nearest's stop to user, then lookup the stop's next departure


//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        provider = LocationManager.GPS_PROVIDER;
//        provider = locationManager.getBestProvider(new Criteria(), false);
//        if(checkLocationPermission()){
//            ActivityCompat.requestPermissions(RouteDirections.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 999);
//            Location location = locationManager.getLastKnownLocation(provider);
//
//            double lat = location.getLatitude();
//            double lng = location.getLongitude();
//
//            locationManager.requestLocationUpdates(provider, 10000, 25,locationListener);
//            System.out.println("Location Info:"+lat+"/"+lng);
//            if (location != null) {
//
//            }
//            mMapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(lat, lng), 15), MapAnimationKind.NONE);
//        } else {
//            Toast.makeText(RouteDirections.this, "Location permission not granted", Toast.LENGTH_SHORT).show();
//            System.out.println("Location Info:"+"No location :(");
//            mMapView.setScene(MapScene.createFromLocationAndZoomLevel(FlinderSt, 15), MapAnimationKind.NONE);
//        }

        getUserLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private void getUserLocation() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(RouteDirections.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(RouteDirections.this, "Location Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject Location permission,some service may not available\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    private void Location() {
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
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(RouteDirections.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
                e.printStackTrace();
            }
        });
        result.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(RouteDirections.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(this)
//                        .setTitle("Location access required")
//                        .setMessage("To find your nearest stops, your location is important attribute to calculate that.")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(RouteDirections.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 999);
//                            }
//                        })
//                        .create()
//                        .show();
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 999);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 999: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        locationManager.requestLocationUpdates(provider, 10000, 1, (LocationListener) this);
////                        Update and show user's location on bingmap
//                    }
//
//                } else {
//                    // Define user's location is 0,0 and remove user's location
//                }
//                return;
//            }
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

    };

    //ZhentaoHe Test
    public void generateRouteDirectionList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.route_directionList);

        Route route1 = new Route(1038);
        Direction direction1 = new Direction(1038);


        ArrayList<Route> SearchRouteList = new ArrayList<>();
        ArrayList<Direction> SearchDirectionList = new ArrayList<>();

        SearchRouteList.add(route1);
        SearchDirectionList.add(direction1);


        RouteDirectionAdapter adapter = new RouteDirectionAdapter(v.getContext(),R.layout.routedetails_view, SearchDirectionList);
        mListView.setAdapter(adapter);
    }

    //ZhentaoHe Test



}
