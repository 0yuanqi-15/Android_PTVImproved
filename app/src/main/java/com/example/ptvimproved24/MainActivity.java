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

        String fragmentToDisplay = getIntent().getStringExtra("fragmentToDisplay");
        if(fragmentToDisplay == MainActivity.FRAGMENTA){

        }
        if(fragmentToDisplay == MainActivity.FRAGMENTB){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activities_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    public static final String FRAGMENTA = "Fragment_Disruptions";
    public static final String FRAGMENTB = "Fragment_StopSelect";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuact_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragmentToDisplay", FRAGMENTA);
                startActivity(intent);
                break;
            case R.id.menuact_stopselect:
                break;
            case R.id.menuact_stops:
                break;
            case R.id.menuact_disruptions:
                break;
            case R.id.menuact_routes:
                break;

        }
        return true;
    }

}