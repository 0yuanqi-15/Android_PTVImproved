package com.example.ptvimproved24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ptvimproved24.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            case R.id.menuact_home:
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("fragmentToDisplay", 1);
                startActivity(intent);
                break;
            case R.id.menuact_stopselect:
                intent = new Intent(MainActivity.this, MainActivity.class );
                intent.putExtra("fragmentToDisplay",2);
                startActivity(intent);
                break;
            case R.id.menuact_journeyplanner:
                intent = new Intent(MainActivity.this, MainActivity.class );
                intent.putExtra("fragmentToDisplay",3);
                startActivity(intent);
                break;
            case R.id.menuact_disruptions:
                intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("fragmentToDisplay", 4);
                startActivity(intent);
                break;
            case R.id.menuact_stops:
                break;
            case R.id.menuact_routes:
                intent = new Intent(MainActivity.this, route)
                break;
            case R.id.activity_routedetails:
                intent = new Intent(MainActivity.this,RouteDetails.class);
                startActivity(intent);
                break;

        }
        return true;
    }

}