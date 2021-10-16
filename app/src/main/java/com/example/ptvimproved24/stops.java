package com.example.ptvimproved24;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.ptvimproved24.databinding.ActivityStopsBinding;

import java.util.ArrayList;

public class stops extends AppCompatActivity {

    private ActivityStopsBinding binding;
    private ListView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStopsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());

        //show information in the layout
        detail = (ListView) findViewById(R.id.stops_nextservice);
        Intent intent = getIntent();
        int stopId = intent.getIntExtra("index", 0);
        int routeType = intent.getIntExtra("type", 0);
        String stopName = intent.getStringExtra("name");
        String stopSuburb = intent.getStringExtra("suburb");

        toolBarLayout.setTitle(stopName+"\n"+stopSuburb);

        ArrayList<String> stopDetail = new ArrayList<>();

        DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(this);

        StopDetailAdapter adapter = new StopDetailAdapter(this, R.layout.stopdetail, stopDetail);

        detail.setAdapter(adapter);

        departureHttpRequestHandler.getNextDepartureByStopId(stopId, routeType, adapter);

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}