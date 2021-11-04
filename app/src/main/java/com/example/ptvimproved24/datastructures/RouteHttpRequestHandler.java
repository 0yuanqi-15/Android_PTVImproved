package com.example.ptvimproved24.datastructures;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ptvimproved24.commonDataRequest;
import com.example.ptvimproved24.datastructures.Route;
import com.example.ptvimproved24.datastructures.RouteGeopath;
import com.example.ptvimproved24.datastructures.RouteGeopathRequestHandler;
import com.example.ptvimproved24.datastructures.Stop;
import com.microsoft.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteHttpRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public RouteHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    public void getRoutePathById(int routeid, ArrayAdapter adapter, MapView map) throws Exception {
        String url = commonDataRequest.showRouteInfoWithPath(routeid);
        Log.d("","Request:"+url);
        try {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try{
                            JSONObject jsonObj = new JSONObject(responseBody);
                            JSONArray stopsArray =  jsonObj.getJSONArray("stops");
                            ArrayList<Stop> stopslist = getStoppingsList(stopsArray);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Adding pins on map

                                    // Inflate adapter showing stops

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRoutePathByIdwoGeoPath(int routeid, View view) throws Exception {
        String url = commonDataRequest.showRouteInfoWithPath(routeid);
        Log.d("","Request:"+url);
        try {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try{
                            JSONObject jsonObj = new JSONObject(responseBody);
                            JSONArray stopsArray =  jsonObj.getJSONArray("stops");
                            ArrayList<Route> routesPath = getRoute(stopsArray);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    view.findViewById(R.id.);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getRoutePathByIdwGeoPath(int routeid, MapView map) throws Exception {
        String url = commonDataRequest.showRouteInfoWithPath(routeid);
        Log.d("","Request:"+url);
        try {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        try{
                            JSONObject jsonObj = new JSONObject(responseBody);
                            JSONArray stopsArray =  jsonObj.getJSONArray("stops");
                            ArrayList<Route> routesPath = getRoutePath(stopsArray);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Adding pins on map

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Route> getRoutePath(JSONArray jsonArray) throws JSONException{
        RouteGeopathRequestHandler routeGeopathRequestHandler = new RouteGeopathRequestHandler();
        ArrayList<Route> result = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int route_type = jsonObject.getInt("route_type");
            int route_id = jsonObject.getInt("route_id");
            String route_name = jsonObject.getString("route_name");
            String route_number = jsonObject.getString("route_number");
            String route_gtfs_id = jsonObject.getString("route_gtfs_id");
            JSONArray geopath = jsonObject.getJSONArray("geopath");
            ArrayList<RouteGeopath> arrGeoPath = routeGeopathRequestHandler.getRouteGeoPath(geopath);

            Route r = new Route(route_type,route_id,route_name,route_number,route_gtfs_id,arrGeoPath);
            result.add(r);
        }
        return result;
    }

    private ArrayList<Route> getRoute(JSONArray jsonArray) throws JSONException{
        RouteGeopathRequestHandler routeGeopathRequestHandler = new RouteGeopathRequestHandler();
        ArrayList<Route> result = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int route_type = jsonObject.getInt("route_type");
            int route_id = jsonObject.getInt("route_id");
            String route_name = jsonObject.getString("route_name");
            String route_number = jsonObject.getString("route_number");
            String route_gtfs_id = jsonObject.getString("route_gtfs_id");

            Route r = new Route(route_type,route_id,route_name,route_number,route_gtfs_id);
            result.add(r);
        }
        return result;
    }

    private ArrayList<Stop> getStoppingsList(JSONArray jsonArray) throws JSONException {
        ArrayList<Stop> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int stop_id = jsonObject.getInt("stop_id");
            int route_type = jsonObject.getInt("route_type");
            String stop_suburb = jsonObject.getString("stop_suburb");
            String stop_name = jsonObject.getString("stop_name");
            float stop_latitude = (float) jsonObject.getDouble("stop_latitude");
            float stop_longitude = (float) jsonObject.getDouble("stop_longitude");
            Stop s = new Stop(stop_suburb,route_type,stop_latitude,stop_longitude,stop_id,stop_name);
            result.add(s);
        }
        return result;
    }
}
