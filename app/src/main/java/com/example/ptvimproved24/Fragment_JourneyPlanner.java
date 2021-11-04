package com.example.ptvimproved24;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Fragment_JourneyPlanner extends Fragment {
    private static final String URL = "https://www.ptv.vic.gov.au/journey";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_journey_planner, container, false);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL));
        startActivity(i);


        Button button = (Button) v.findViewById( R.id.fragbutton_journeyplanner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URL));
                startActivity(i);
            }
        });

        return v;
    }
}