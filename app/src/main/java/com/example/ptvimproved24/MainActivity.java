package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ptvimproved24.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    String provider;

    protected static final int REQUEST_LOCATION = 99;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserLocation();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        checkLocationPermission();
//
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });

        Log.d(TAG, "onCreate: Started");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_journey_planner, R.id.navigation_mapselect, R.id.navigation_distuptions)
                .build();


        navView.setSelectedItemId(R.id.navigation_home);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        int fragmentToDisplay = getIntent().getIntExtra("fragmentToDisplay", 0);
        if (fragmentToDisplay == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_Home()).addToBackStack(null).commit();
        }
        if (fragmentToDisplay == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_StopSelect()).addToBackStack(null).commit();
        }
        if (fragmentToDisplay == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_JourneyPlanner()).addToBackStack(null).commit();
        }
        if (fragmentToDisplay == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_Disruptions()).addToBackStack(null).commit();
        }
    }

    private void getUserLocation() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Location Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject Location permission,some service may not available\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activities_menu, menu); // popping up test window
        // return true so that the menu pop up is opened

        MenuItem menuItem = menu.findItem(R.id.search);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                Toast.makeText(MainActivity.this,"Search View showed",Toast.LENGTH_SHORT);
//                onSearchRequested();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type route, stops or location to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                if (newText.length() >=3){
                    
                }
                return false;
            }
        });

        return true;
    }

    public static final String FRAGMENTA = "Fragment_Home";
    public static final String FRAGMENTB = "Fragment_StopSelect";
    public static final String FRAGMENTC = "Fragment_JourneyPlanner";
    public static final String FRAGMENTD = "Fragment_Disruptions";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);

        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menuact_disruption:
                intent = new Intent(MainActivity.this, Disruptions.class);
                startActivity(intent);
                break;
            case R.id.menuact_stops:
                intent = new Intent(MainActivity.this, stops.class);
                intent.putExtra("stop_id", 1000);
                startActivity(intent);
                break;
            case R.id.menuact_routedirections:
                intent = new Intent(MainActivity.this, RouteDirections.class);
                intent.putExtra("route_id", 1);
                intent.putExtra("route_type",0);
                startActivity(intent);
                break;
            case R.id.menuact_routedetails:
                intent = new Intent(MainActivity.this, RouteDetails.class);
                intent.putExtra("route_id", 2);
                intent.putExtra("route_type",0);
                startActivity(intent);
                break;
        }
        return true;
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
                            resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
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
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}