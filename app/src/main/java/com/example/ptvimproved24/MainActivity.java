package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ptvimproved24.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    String provider;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPermission();

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
////               public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });

        Log.d(TAG, "onCreate: Started");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_journey_planner,R.id.navigation_mapselect, R.id.navigation_distuptions)
                .build();


        navView.setSelectedItemId(R.id.navigation_home);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        int fragmentToDisplay = getIntent().getIntExtra("fragmentToDisplay",0);
        if(fragmentToDisplay == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_Home()).addToBackStack(null).commit();
        }
        if(fragmentToDisplay == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_StopSelect()).addToBackStack(null).commit();
        }
        if(fragmentToDisplay == 3){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_JourneyPlanner()).addToBackStack(null).commit();
        }
        if(fragmentToDisplay == 4){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new Fragment_Disruptions()).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activities_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    public static final String FRAGMENTA = "Fragment_Home";
    public static final String FRAGMENTB = "Fragment_StopSelect";
    public static final String FRAGMENTC = "Fragment_JourneyPlanner";
    public static final String FRAGMENTD = "Fragment_Disruptions";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
//            case R.id.menuact_home:
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.putExtra("fragmentToDisplay", 1);
//                startActivity(intent);
//                break;
//            case R.id.menuact_stopselect:
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.putExtra("fragmentToDisplay",2);
//                startActivity(intent);
//                break;
//            case R.id.menuact_journeyplanner:
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.putExtra("fragmentToDisplay",3);
//                startActivity(intent);
//                break;
//            case R.id.menuact_disruptions:
//                intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.putExtra("fragmentToDisplay", 4);
//                startActivity(intent);
//                break;
            case R.id.menuact_stops:
                intent = new Intent(MainActivity.this, stops.class);
                intent.putExtra("stopid",1000);
                startActivity(intent);
                break;
            case R.id.menuact_routes:
//                intent = new Intent(MainActivity.this, Route)
                break;
            case R.id.menuact_routedetails:
                intent = new Intent(MainActivity.this,RouteDetails.class);
                intent.putExtra("routeid",1);
                startActivity(intent);
                break;
        }
        return true;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Request your location to get nearest stops")
                        .setMessage("This app will using your location to determine the nearest stops around you")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        999);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        999);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 999: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 10000, 1, (LocationListener) this);
                    }
                } else {
                    // permission denied, using 0,0 as user coordinates, not showing nearest stop, not showing user's location on map
                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(provider, 10000, 1, (LocationListener) this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates((LocationListener) this);
        }
    }
}