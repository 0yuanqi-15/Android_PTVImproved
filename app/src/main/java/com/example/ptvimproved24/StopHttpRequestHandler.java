package com.example.ptvimproved24;

import androidx.annotation.NonNull;

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

public class StopHttpRequestHandler {
    private OkHttpClient client;
    private ArrayList<NearStop> nearStopArray;

    public StopHttpRequestHandler() {
        client = new OkHttpClient();
        nearStopArray = null;
    }

    public void clear() {
        this.nearStopArray = null;
    }

    private ArrayList<String> getRoutesByStop(JSONArray routes) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < routes.length(); i ++) {
            String routeGtfsId = routes.getJSONObject(i).getString("route_gtfs_id");
            result.add(routeGtfsId);
        }
        return result;
    }

    private ArrayList<NearStop> getNearStopsFromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<NearStop> nearStops = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentStop = jsonArray.getJSONObject(i);
            int distance = currentStop.getInt("stop_distance");
            String stopSuburb = currentStop.getString("stop_suburb");
            String stopName = currentStop.getString("stop_name");
            String stopId = currentStop.getString("stop_id");
            int routeType = currentStop.getInt("route_type");
            JSONArray routes = currentStop.getJSONArray("routes");
            ArrayList<String> routesArray = new ArrayList<>();
            try {
                routesArray = getRoutesByStop(routes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<String> timeArray = new ArrayList<>();
            for (int j = 0; j < routesArray.size(); j ++) {
                timeArray.add("15:00");
            }
            NearStop nearStop = new NearStop(stopSuburb, stopName, distance, routesArray, timeArray);
            nearStops.add(nearStop);
        }
        return nearStops;
    }

    public void getStopsFromLocation(float latitude, float longitude) {
        try {
            String url = commonDataRequest.nearByStops(latitude, longitude);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
            ArrayList<NearStop> stopArray;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String responseBody = response.body().string();
                        try {
                            JSONObject jsonObj = new JSONObject(responseBody);
                            JSONArray stops = jsonObj.getJSONArray("stops");
                            nearStopArray = getNearStopsFromJson(stops);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NearStop> getNearStopArray() {
        return nearStopArray;
    }

    public void setNearStopArray(ArrayList<NearStop> nearStopArray) {
        this.nearStopArray = nearStopArray;
    }
}
