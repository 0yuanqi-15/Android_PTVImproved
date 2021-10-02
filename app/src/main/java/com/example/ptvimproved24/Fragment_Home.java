package com.example.ptvimproved24;


import android.content.Intent;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ptvimproved24.databinding.FragmentHomeBinding;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;

public class Fragment_Home extends Fragment implements ShakeDetector.Listener {

    private FragmentHomeBinding binding;
    private StopHttpRequestHandler stopHttpRequestHandler;
    private LocationManager locationManager;
    private ListView mListView;
    private NearStopListAdapter adapter;
    private final float defaultLatitude = -37.818078f;
    private final float defaultLongitude = 144.96681f;
    private Float latitude;
    private Float longitude;

    private Boolean isNearStopShowing;

    //add this to get nearest stops
    ArrayList<Stop> nearestStop = new ArrayList<>();
    ArrayList<Stop> nearestStopDetail = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        latitude = defaultLatitude;
        longitude = defaultLongitude;
        isNearStopShowing = true;

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.nearStops_view);
        view.findViewById(R.id.nearbystopLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNearStopShowing) {
                    mListView.setAdapter(new NearStopListAdapter(view.getContext(),R.layout.nearstop_view, new ArrayList<>()));
                    isNearStopShowing = false;
                } else {
                    mListView.setAdapter(adapter);
                    isNearStopShowing = true;
                }
            }
        });
        adapter = new NearStopListAdapter(view.getContext(),R.layout.nearstop_view, new ArrayList<>());
        mListView.setAdapter(adapter);
        stopHttpRequestHandler = new StopHttpRequestHandler(getActivity());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        getGeoLocation();
        generateNearStopList();
        generateSavedStopList(view);
        generateSavedRouteList(view);



        //add listener here
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), StopDetailActivity.class);

                int amount = Math.min(nearestStopDetail.get(i).getRoutes().size(), nearestStopDetail.get(i).getTimes().size());
                intent.putExtra("amount", amount);

                for(int x=0; x<amount; x++) {
                    intent.putExtra(String.valueOf(x), nearestStopDetail.get(i).getRoutes().get(x)+" "+nearestStopDetail.get(i).getTimes().get(x));
                }

                intent.putExtra("index", nearestStop.get(i).getStopid());
                intent.putExtra("type", nearestStop.get(i).getRouteType());
                intent.putExtra("name", nearestStop.get(i).getStopname());
                startActivity(intent);

            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getGeoLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                }
            });
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } catch (SecurityException e) {
            Toast.makeText(getContext(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void generateNearStopList(){

        ArrayList<String> stop1route = new ArrayList<>();
        stop1route.add("606");stop1route.add("606");stop1route.add("606");
        ArrayList<String> stop1time = new ArrayList<>();
        stop1time.add("Now");stop1time.add("17:15");stop1time.add("17:30");
        Stop stop1=new Stop("Port melbourne","Cruikshank St/Liardet St",293,stop1route,stop1time);

        ArrayList<String> stop2route = new ArrayList<>();
        stop2route.add("606");stop2route.add("606");stop2route.add("606");
        ArrayList<String> stop2time = new ArrayList<>();
        stop2time.add("Now");stop2time.add("17:14");stop2time.add("17:29");
        Stop stop2=new Stop("Albert Park","Bridport St / Richardson St",276,stop2route,stop2time);

        ArrayList<String> stop3route = new ArrayList<>();
        stop3route.add("236");stop3route.add("236");stop3route.add("236");
        ArrayList<String> stop3time = new ArrayList<>();
        stop3time.add("Now");stop3time.add("52 mins");stop3time.add("06:29 PM");
        Stop stop3=new Stop("Albert Park","Graham St/ Pickles St",162,stop3route,stop3time);

//        ArrayList<NearStop> nearStopList = new ArrayList<>();
//        nearStopList.add(stop1);
//        nearStopList.add(stop2);
//        nearStopList.add(stop3);


        stopHttpRequestHandler.getStopsFromLocation(adapter, -37.818078f, 144.96681f);

        //add this to get nearest stops
        nearestStop = stopHttpRequestHandler.getRecord();
        nearestStopDetail = stopHttpRequestHandler.getRecordDetail();

        stopHttpRequestHandler.getStopsFromLocation(adapter, latitude, longitude);

    }

    public void generateSavedStopList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.SavedStop_view);

        ArrayList<String> stop1route = new ArrayList<>();
        stop1route.add("703");stop1route.add("737");stop1route.add("862");
        ArrayList<String> stop1time = new ArrayList<>();
        stop1time.add("Now");stop1time.add("Now");stop1time.add("Now");
        SavedStop stop1=new SavedStop("Clayton","Monash University",stop1route,stop1time);

        ArrayList<String> stop2route = new ArrayList<>();
        stop2route.add("862");stop2route.add("802");stop2route.add("900");
        ArrayList<String> stop2time = new ArrayList<>();
        stop2time.add("Now");stop2time.add("Now");stop2time.add("Now");
        SavedStop stop2=new SavedStop("Chadstone","Chadstone Shopping Centre / Eastern Access Rd",stop2route,stop2time);

        ArrayList<String> stop3route = new ArrayList<>();
        stop3route.add("631");stop3route.add("631");stop3route.add("631");
        ArrayList<String> stop3time = new ArrayList<>();
        stop3time.add("Now");stop3time.add("6 mins");stop3time.add("7 mins");
        SavedStop stop3=new SavedStop("Clayton","Princes Hwy / Blackburn Rd",stop3route,stop3time);

        ArrayList<SavedStop> savedStopList = new ArrayList<>();
        savedStopList.add(stop1);
        savedStopList.add(stop2);
        savedStopList.add(stop3);

        SavedStopListAdapter adapter = new SavedStopListAdapter(v.getContext(),R.layout.savedstops_view, savedStopList);
        mListView.setAdapter(adapter);
    }

    public void generateSavedRouteList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.savedRoutes_view);

        SavedRoute route1=new SavedRoute("703","Oakleigh - Box Hill via Clayton");
        SavedRoute route2=new SavedRoute("3-3a","Melbourne University - East Malvern");

        ArrayList<SavedRoute> SavedRouteList = new ArrayList<>();
        SavedRouteList.add(route1);
        SavedRouteList.add(route2);

        SavedRouteListAdapter adapter = new SavedRouteListAdapter(v.getContext(),R.layout.savedroutes_view, SavedRouteList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void hearShake() {
        getGeoLocation();
        generateNearStopList();
        Toast.makeText(getContext(), "reloading", Toast.LENGTH_SHORT).show();
    }
}
