package com.example.ptvimproved24;


import android.content.Intent;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.example.ptvimproved24.datastructures.DepartureHttpRequestHandler;
import com.example.ptvimproved24.datastructures.Stop;
import com.example.ptvimproved24.datastructures.StopHttpRequestHandler;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fragment_Home extends Fragment implements ShakeDetector.Listener {

    private FragmentHomeBinding binding;
    private StopHttpRequestHandler stopHttpRequestHandler;
    private LocationManager locationManager;
    private ListView nearStopListView;
    private ListView savedStopListView;
    private ListView savedRouteListView;
    private NearStopListAdapter nearStopListAdapter;
    private SavedStopListAdapter savedStopListAdapter;
    private SavedRouteListAdapter savedRouteListAdapter;
    private final float defaultLatitude = -37.818078f;
    private final float defaultLongitude = 144.96681f;
    private Float latitude;
    private Float longitude;

    private Boolean isNearStopShowing;

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
        nearStopListView = (ListView) view.findViewById(R.id.nearStops_view);
        savedStopListView = (ListView) view.findViewById(R.id.SavedStop_view);
        savedRouteListView = (ListView) view.findViewById(R.id.savedRoutes_view);
        view.findViewById(R.id.nearbystopLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNearStopShowing) {
                    nearStopListView.setAdapter(new NearStopListAdapter(view.getContext(),R.layout.nearstop_view, new ArrayList<>()));
                    isNearStopShowing = false;
                } else {
                    nearStopListView.setAdapter(nearStopListAdapter);
                    isNearStopShowing = true;
                }
            }
        });
        nearStopListAdapter = new NearStopListAdapter(view.getContext(),R.layout.nearstop_view, new ArrayList<>());
        nearStopListView.setAdapter(nearStopListAdapter);
        stopHttpRequestHandler = new StopHttpRequestHandler(getActivity());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        getGeoLocation();
        generateSavedStopList(view);
        generateSavedRouteList(view);

        //add listener here
        nearStopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), stops.class);
                Stop clickedStop = nearStopListAdapter.getItem(i);


                intent.putExtra("index", clickedStop.getStop_id());
                intent.putExtra("type", clickedStop.getRouteType());
                intent.putExtra("name", clickedStop.getStop_name());
                intent.putExtra("suburb", clickedStop.getStop_suburb());
                startActivity(intent);

            }
        });

        //add saved stop listener
        savedStopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), stops.class);
                SavedStop clickedStop = savedStopListAdapter.getItem(i);

                intent.putExtra("index", Integer.parseInt(clickedStop.getStopid()));
                intent.putExtra("type", Integer.parseInt(clickedStop.getRouteType()));
                intent.putExtra("name", clickedStop.getStopname());
                intent.putExtra("suburb", clickedStop.getSuburb());

                System.out.println(clickedStop.getRouteType());
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
                    if(latitude != (float) location.getLatitude() || longitude != (float) location.getLongitude()) {
                        latitude = (float) location.getLatitude();
                        longitude = (float) location.getLongitude();
                        generateNearStopList();
                    }
                }
            });
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            System.out.println("locationfraghome:"+location);
//            latitude = (float) location.getLatitude();
//            longitude = (float) location.getLongitude();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            generateNearStopList();
            e.printStackTrace();
        }
    }

    public void generateNearStopList(){

//        ArrayList<String> stop1route = new ArrayList<>();
//        stop1route.add("606");stop1route.add("606");stop1route.add("606");
//        ArrayList<String> stop1time = new ArrayList<>();
//        stop1time.add("Now");stop1time.add("17:15");stop1time.add("17:30");
//        Stop stop1=new Stop("Port melbourne","Cruikshank St/Liardet St",293,stop1route,stop1time);
//
//        ArrayList<String> stop2route = new ArrayList<>();
//        stop2route.add("606");stop2route.add("606");stop2route.add("606");
//        ArrayList<String> stop2time = new ArrayList<>();
//        stop2time.add("Now");stop2time.add("17:14");stop2time.add("17:29");
//        Stop stop2=new Stop("Albert Park","Bridport St / Richardson St",276,stop2route,stop2time);
//
//        ArrayList<String> stop3route = new ArrayList<>();
//        stop3route.add("236");stop3route.add("236");stop3route.add("236");
//        ArrayList<String> stop3time = new ArrayList<>();
//        stop3time.add("Now");stop3time.add("52 mins");stop3time.add("06:29 PM");
//        Stop stop3=new Stop("Albert Park","Graham St/ Pickles St",162,stop3route,stop3time);

//        ArrayList<NearStop> nearStopList = new ArrayList<>();
//        nearStopList.add(stop1);
//        nearStopList.add(stop2);
//        nearStopList.add(stop3);

        stopHttpRequestHandler.getStopsFromLocation(nearStopListAdapter, latitude, longitude);

    }

    public void generateSavedStopList(View v){


//        ArrayList<String> stop1route = new ArrayList<>();
//        stop1route.add("703");stop1route.add("737");stop1route.add("862");
//        ArrayList<String> stop1time = new ArrayList<>();
//        stop1time.add("Now");stop1time.add("Now");stop1time.add("Now");
//        SavedStop stop1=new SavedStop("Clayton","Monash University",stop1route,stop1time);
//
//        ArrayList<String> stop2route = new ArrayList<>();
//        stop2route.add("862");stop2route.add("802");stop2route.add("900");
//        ArrayList<String> stop2time = new ArrayList<>();
//        stop2time.add("Now");stop2time.add("Now");stop2time.add("Now");
//        SavedStop stop2=new SavedStop("Chadstone","Chadstone Shopping Centre / Eastern Access Rd",stop2route,stop2time);
//
//        ArrayList<String> stop3route = new ArrayList<>();
//        stop3route.add("631");stop3route.add("631");stop3route.add("631");
//        ArrayList<String> stop3time = new ArrayList<>();
//        stop3time.add("Now");stop3time.add("6 mins");stop3time.add("7 mins");
//        SavedStop stop3=new SavedStop("Clayton","Princes Hwy / Blackburn Rd",stop3route,stop3time);

        ArrayList<SavedStop> savedStopList = new ArrayList<>();

        String PREFERENCE_NAME = "SavedStops";
        SharedPreferences pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        try {
            if (pref != null) {
                Map<String, ?> allEntries = pref.getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    String stopid = entry.getKey();
                    String stopinfo = pref.getString(stopid, (new JSONObject()).toString());

                    Map<String,String> stopinfoMap = new HashMap<>();
                    if (stopinfo != null) {
                        JSONObject jsonObject = new JSONObject(stopinfo);
                        Iterator<String> keysItr = jsonObject.keys();
                        while (keysItr.hasNext()) {
                            String key = keysItr.next();
                            String value = jsonObject.getString(key);
                            stopinfoMap.put(key, value);
                        }
                    }

                    SavedStop savedStop = new SavedStop(stopid, stopinfoMap.get("suburb"), stopinfoMap.get("route_type"), stopinfoMap.get("stop_name"));
                    savedStopList.add(savedStop);
                    Log.d("values", "saved stop loaded success");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        savedStopListAdapter  = new SavedStopListAdapter(v.getContext(),R.layout.savedstops_view, savedStopList);
        savedStopListView.setAdapter(savedStopListAdapter);

        for (SavedStop eachSaveStop: savedStopList){
            Log.d("values", ("id of " + eachSaveStop.getStopname() + "is" + eachSaveStop.getStopid()));
            new DepartureHttpRequestHandler(getActivity()).getNextDepartureBySavedStop(eachSaveStop, savedStopListAdapter);

        }

        savedStopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), stops.class);
                SavedStop clickedStop = savedStopListAdapter.getItem(i);

                intent.putExtra("index", Integer.valueOf(clickedStop.getStopid()));
                intent.putExtra("type", Integer.valueOf(clickedStop.getRouteType()));
                intent.putExtra("name", clickedStop.getStopname());
                intent.putExtra("suburb", clickedStop.getSuburb());

                startActivity(intent);
            }
        });


    }

    public void generateSavedRouteList(View v){
        ArrayList<SavedRoute> savedRouteArrayList = new ArrayList<>();
//        SavedRoute route1=new SavedRoute("703",13270,"Oakleigh - Box Hill via Clayton",2);
//        SavedRoute route2=new SavedRoute("3-3a",761,"Melbourne University - East Malvern",1);
//        savedRouteArrayList.add(route1);
//        savedRouteArrayList.add(route2);


        SharedPreferences pref = getContext().getSharedPreferences("saved_routes", Context.MODE_PRIVATE);

        try {
            if (pref != null) {
                Map<String, ?> allEntries = pref.getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    String routeid = entry.getKey();
                    String stopinfo = pref.getString(routeid, (new JSONObject()).toString());

                    Map<String,String> routeinfoMap = new HashMap<>();
                    if (stopinfo != null) {
                        JSONObject jsonObject = new JSONObject(stopinfo);
                        Iterator<String> keysItr = jsonObject.keys();
                        while (keysItr.hasNext()) {
                            String key = keysItr.next();
                            String value = jsonObject.getString(key);
                            routeinfoMap.put(key, value);
                        }
                    }
                    System.out.println("gtfs is " + routeinfoMap.get("gtfs_id"));
                    System.out.println("route name is " + routeinfoMap.get("route_name"));
                    System.out.println("id is " + routeid);
                    SavedRoute route=new SavedRoute(routeinfoMap.get("gtfs_id"), Integer.parseInt(routeid), routeinfoMap.get("route_name"), Integer.parseInt(routeinfoMap.get("route_type")));
                    savedRouteArrayList.add(route);
                    Log.d("values", "saved route loaded success");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        savedRouteListAdapter = new SavedRouteListAdapter(v.getContext(),R.layout.savedroutes_view,savedRouteArrayList);
        savedRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), RouteDirections.class);
                intent.putExtra("route_id", savedRouteListAdapter.getItem(position).getSavedRouteid());
                intent.putExtra("route_type", savedRouteListAdapter.getItem(position).getSavedRouteType());
                intent.putExtra("route_name", savedRouteListAdapter.getItem(position).getSavedRouteName());
                intent.putExtra("route_gtfs_id", savedRouteListAdapter.getItem(position).getSavedRouteGtfsId());
                startActivity(intent);
            }
        });
        savedRouteListView.setAdapter(savedRouteListAdapter);
    }

    @Override
    public void hearShake() {
        getGeoLocation();
        generateNearStopList();
        Toast.makeText(getContext(), "Reloading", Toast.LENGTH_SHORT).show();
    }
}
