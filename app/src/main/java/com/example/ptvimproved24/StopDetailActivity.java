package com.example.ptvimproved24;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StopDetailActivity extends AppCompatActivity {

    private ListView detail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopdetail_view);
        detail = (ListView) findViewById(R.id.stopDetail_view);

        //adapter = new StopDetailAdapter(detail.getContext(),R.layout.stopdetail_view, new ArrayList<>());

        //detail.setAdapter(adapter);

        String stopRouteAndTime;

        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount", 0);
        int stopId = intent.getIntExtra("index", 0);
        int routeType = intent.getIntExtra("type", 0);
        String stopName = intent.getStringExtra("name");





        ArrayList<String> stopDetail = new ArrayList<>();
        ArrayList<String> stopRoutes = new ArrayList<>();
        ArrayList<String> stopTimes = new ArrayList<>();
        //stopDetail.add(String.valueOf(stopId));
        //stopDetail.add(String.valueOf(routeType));
        stopDetail.add(stopName);
        //stopDetail.add(String.valueOf(i));

        for(int x=0; x<amount; x++) {
            stopRouteAndTime = intent.getStringExtra(String.valueOf(x));
            stopDetail.add(stopRouteAndTime);
        }

        Stop stop = new Stop(stopId, stopName);

        //DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(this);
        //Stop detailedStop = departureHttpRequestHandler.getNextDepartureByStopId(stop, routeType);

        //departureHttpRequestHandler.getNextDepartureByStopId(stop, routeType, stopRoutes);
        //stopRoutes.addAll(departureHttpRequestHandler.getStopRoutes().subList(0, 5));
        //stopTimes = departureHttpRequestHandler.getStopTimes();

        //stopDetail.add(String.valueOf(stopRoutes.size()));

        //int maximum = 10;
        //int amount = Math.min(maximum, detailedStop.getRoutes().size());
        //amount = Math.min(amount, detailedStop.getTimes().size());

        //for(int i=0; i<5; i++) {
         //   stopDetail.add(stopRoutes.get(i)+" "+stopTimes.get(i));
        //}

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.stopdetail_view, android.R.id.text1, stopDetail);

        detail.setAdapter(adapter);


        //Stop stop = new Stop(stopId, stopName);

        //adapter.add(stop);
        //adapter.notifyDataSetChanged();
        //DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(this);
        //departureHttpRequestHandler.getNextDepartureByStopId(stop, routeType, adapter);















    }




}
