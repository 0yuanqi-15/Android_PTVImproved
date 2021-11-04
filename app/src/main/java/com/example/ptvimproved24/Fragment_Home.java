package com.example.ptvimproved24;


import android.content.Intent;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ptvimproved24.databinding.FragmentHomeBinding;
import com.example.ptvimproved24.datastructures.DepartureHttpRequestHandler;
import com.example.ptvimproved24.datastructures.SavedRoute;
import com.example.ptvimproved24.datastructures.SavedRouteListAdapter;
import com.example.ptvimproved24.datastructures.SavedStop;
import com.example.ptvimproved24.datastructures.SavedStopListAdapter;
import com.example.ptvimproved24.datastructures.Stop;
import com.example.ptvimproved24.datastructures.StopHttpRequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Fragment_Home extends Fragment {

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        latitude = defaultLatitude;
        longitude = defaultLongitude;

        super.onViewCreated(view, savedInstanceState);
        nearStopListView = (ListView) view.findViewById(R.id.nearStops_view);
        savedStopListView = (ListView) view.findViewById(R.id.SavedStop_view);
        savedRouteListView = (ListView) view.findViewById(R.id.savedRoutes_view);
        nearStopListAdapter = new NearStopListAdapter(view.getContext(),R.layout.nearstop_view, new ArrayList<>());
        nearStopListView.setAdapter(nearStopListAdapter);
        stopHttpRequestHandler = new StopHttpRequestHandler(getActivity());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        getGeoLocation();
        generateSavedStopList();
        generateSavedRouteList();

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getGeoLocation() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (location != null) {
                        if (latitude != (float) location.getLatitude() || longitude != (float) location.getLongitude()) {
                            latitude = (float) location.getLatitude();
                            longitude = (float) location.getLongitude();
                            generateNearStopList();
                        }
                    }
                }
            });
        } catch (SecurityException e) {
            Toast.makeText(getActivity(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            generateNearStopList();
            e.printStackTrace();
        }
    }

    private void resetListViewHeight (ListView listView, ArrayAdapter adapter, int frag) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int maxHeight = displayMetrics.heightPixels / frag;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = Math.min(maxHeight, totalHeight);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void generateNearStopList(){
        stopHttpRequestHandler.getStopsFromLocation(nearStopListAdapter, latitude, longitude);
    }

    public void generateSavedStopList(){
        ArrayList<SavedStop> savedStopList = new ArrayList<>();

        String PREFERENCE_NAME = "SavedStops";
        SharedPreferences pref = getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int count = 0;
        try {
            if (pref != null) {
                Map<String, ?> allEntries = pref.getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    count++;
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
                if(count == 0){
                    SavedStop stop1=new SavedStop("-1","","","Nothing saved yet");
                    savedStopList.add(stop1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        savedStopListAdapter  = new SavedStopListAdapter(getContext(),R.layout.savedstops_view, savedStopList);
        savedStopListView.setAdapter(savedStopListAdapter);

        resetListViewHeight(savedStopListView, savedStopListAdapter, 5);

        for (SavedStop eachSaveStop: savedStopList){
            if (eachSaveStop.getStopid().equals("-1")){
                continue;
            }
            Log.d("values", ("id of " + eachSaveStop.getStopname() + "is" + eachSaveStop.getStopid()));
            new DepartureHttpRequestHandler(getActivity()).getNextDepartureBySavedStop(eachSaveStop, savedStopListAdapter);

        }

        if (count != 0){
            savedStopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SavedStop clickedStop = savedStopListAdapter.getItem(i);

                    Intent intent = new Intent(getActivity(), stops.class);
                    intent.putExtra("index", Integer.valueOf(clickedStop.getStopid()));
                    intent.putExtra("type", Integer.valueOf(clickedStop.getRouteType()));
                    intent.putExtra("name", clickedStop.getStopname());
                    intent.putExtra("suburb", clickedStop.getSuburb());

                    startActivity(intent);
                }
            });
        }


    }

    public void generateSavedRouteList(){
        ArrayList<SavedRoute> savedRouteArrayList = new ArrayList<>();

        SharedPreferences pref = getContext().getSharedPreferences("saved_routes", Context.MODE_PRIVATE);

        try {
            if (pref != null) {
                Map<String, ?> allEntries = pref.getAll();
                int count = 0;
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    count++;
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
                    Log.d("","gtfs is " + routeinfoMap.get("gtfs_id"));
                    Log.d("","route name is " + routeinfoMap.get("route_name"));
                    Log.d("","id is " + routeid);
                    SavedRoute route=new SavedRoute(routeinfoMap.get("gtfs_id"), Integer.parseInt(routeid), routeinfoMap.get("route_name"), Integer.parseInt(routeinfoMap.get("route_type")));
                    savedRouteArrayList.add(route);
                    Log.d("values", "saved route loaded success");

                }
                if (count == 0){
                    SavedRoute route2=new SavedRoute("",-1,"Nothing saved yet",1);
                    savedRouteArrayList.add(route2);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        savedRouteListAdapter = new SavedRouteListAdapter(getContext(),R.layout.savedroutes_view,savedRouteArrayList);
        savedRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (savedRouteListAdapter.getItem(position).getSavedRouteid() ==-1){
                    return;
                }
                Intent intent = new Intent(getActivity(), RouteDirections.class);
                intent.putExtra("route_id", savedRouteListAdapter.getItem(position).getSavedRouteid());
                intent.putExtra("route_type", savedRouteListAdapter.getItem(position).getSavedRouteType());
                intent.putExtra("route_name", savedRouteListAdapter.getItem(position).getSavedRouteName());
                intent.putExtra("route_gtfs_id", savedRouteListAdapter.getItem(position).getSavedRouteGtfsId());
                startActivity(intent);
            }
        });
        savedRouteListView.setAdapter(savedRouteListAdapter);

        resetListViewHeight(savedRouteListView, savedRouteListAdapter, 10);
    }

    @Override
    public void onResume() {
        super.onResume();
        generateSavedRouteList();
        generateSavedStopList();
    }
}
