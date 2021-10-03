package com.example.ptvimproved24;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StopDetailActivity extends AppCompatActivity {

    private ListView detail;
    private TextView name;
    private Button saveStop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopdetail_view);
        detail = (ListView) findViewById(R.id.stopDetail_view);
        name = (TextView) findViewById(R.id.stopDetailName_view);
        saveStop = (Button) findViewById(R.id.saveStop_button);



        String stopRouteAndTime;

        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount", 0);
        int stopId = intent.getIntExtra("index", 0);
        int routeType = intent.getIntExtra("type", 0);
        String stopName = intent.getStringExtra("name");

        name.setText(stopName);
        name.setTextSize(48);



        ArrayList<String> stopDetail = new ArrayList<>();


        for(int x=0; x<amount; x++) {
            stopRouteAndTime = intent.getStringExtra(String.valueOf(x));
            stopDetail.add(stopRouteAndTime);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stopDetail);

        detail.setAdapter(adapter);


        //this button listener is used to save the stop
        saveStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }




}
