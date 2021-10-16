package com.example.ptvimproved24;

import android.location.Location;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
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

public class StopHttpRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public StopHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    private ArrayList<String> getRoutesByStop(JSONArray routes) throws JSONException {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < routes.length(); i ++) {
            //String routeGtfsId = routes.getJSONObject(i).getString("route_gtfs_id");
            String routeGtfsId = "--";
            result.add(routeGtfsId);
        }
        return result;
    }

    private ArrayList<Stop> getNearStopsFromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Stop> stops = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentStop = jsonArray.getJSONObject(i);
            int distance = currentStop.getInt("stop_distance");
            String stopSuburb = currentStop.getString("stop_suburb");
            String stopName = currentStop.getString("stop_name");
            int stopId = currentStop.getInt("stop_id");
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
                timeArray.add("--:--"); // 再次异步请求Departure,根据Stop id,route type; 获得后续的班车信息
            }
            Stop stop = new Stop(stopSuburb, stopName, distance, routesArray, timeArray);
            stop.setStop_id(stopId);
            stop.setRouteType(routeType);
            stops.add(stop);
        }
        return stops;
    }

    private ArrayList<Stop> getStoppingList(JSONArray jsonArray) throws JSONException {
//        异步请求某线路停靠的全程站点
        ArrayList<Stop> stops = new ArrayList<>();
        for (int i=0; i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String stop_suburb = jsonObject.getString("stop_suburb");
            int route_type = jsonObject.getInt("route_type");
            float stop_latitude = (float) jsonObject.getDouble("stop_latitude");
            float stop_longitude = (float) jsonObject.getDouble("stop_longitude");
            int stop_id = jsonObject.getInt("stop_id");
            String stop_name = jsonObject.getString("stop_name");

            Stop s = new Stop(stop_suburb,route_type,stop_latitude,stop_longitude,stop_id,stop_name);
            stops.add(s);
        }
        return stops;
    }

    public void getStopsFromLocation(ArrayAdapter adapter, float latitude, float longitude) {
        try {
            String url = commonDataRequest.nearByStops(latitude, longitude);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
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
                                    ArrayList<Stop> stopsArray = getNearStopsFromJson(stops);
                                    ArrayList<Stop> dedupStopsArray = new ArrayList<>();

                                    for (int i=0;i<stopsArray.size();i++){
                                        boolean duplicate = false;
                                        for (int j=0;j<dedupStopsArray.size();j++){
                                    if (stopsArray.get(i).getStop_id()==dedupStopsArray.get(j).getStop_id()){
                                        duplicate= true;
                                        break;
                                    }
                                }
                                if (!duplicate){
                                    dedupStopsArray.add(stopsArray.get(i));
                                }
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int toIndex = dedupStopsArray.size() >= 3 ? 3 : dedupStopsArray.size();
                                    adapter.clear();
                                    adapter.addAll(dedupStopsArray.subList(0,toIndex));
                                    adapter.notifyDataSetChanged();
                                    for(int i = 0 ; i < toIndex; i ++) {
                                        DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(activity);
                                        departureHttpRequestHandler.getNextDepartureByStopid(dedupStopsArray.get(i), adapter);
                                    }
                                }
                            });
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

    public void getStopsFromLocation(GoogleMap googleMap, float latitude, float longitude) {
        try {
            String url = commonDataRequest.nearByStopsOnSelect(latitude, longitude);
            Request request = new Request.Builder().url(url).build();
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
                            ArrayList<Stop> stopsArray = getStoppingList(stops);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    googleMap.clear();
                                    for(int i = 0 ; i < stopsArray.size(); i ++) {

                                    }
                                }
                            });
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

    public void getStopsOnRouteToBingmap(int route_id, int route_type, MapView mapview) throws Exception {
        String url = commonDataRequest.showRoutesStop(route_id, route_type);
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObj = new JSONObject(responseBody);
                        JSONArray stops = jsonObj.getJSONArray("stops");
                        ArrayList<Stop> stopsArray = getStoppingList(stops);
                        for(int i = 0 ; i < stopsArray.size(); i ++) {
                            System.out.println("Bingmap attempt append:"+ stopsArray.get(i).getStop_latitude()+" "+stopsArray.get(i).getStop_longitude());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0 ; i < stopsArray.size(); i ++) {
                                    System.out.println("Bingmap attempt append:"+ stopsArray.get(i).getStop_latitude()+" "+stopsArray.get(i).getStop_longitude());
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private StopLocation parseStopLocationFromJson(JSONObject jsonObject) {
        StopLocation stopLocation = null;
        try {
            int postCode = jsonObject.getInt("postcode");
            String municipality = jsonObject.getString("municipality");
            int municipalityId = jsonObject.getInt("municipality_id");
            String primaryName = jsonObject.getString("primary_stop_name");
            String primaryType = jsonObject.getString("road_type_primary");
            String secondName = jsonObject.getString("second_stop_name");
            String secondType = jsonObject.getString("road_type_second");
            String suburb = jsonObject.getString("suburb");
            JSONObject GPS = jsonObject.getJSONObject("gps");
            float latitude = (float) GPS.getDouble("latitude");
            float longitude = (float) GPS.getDouble("longitude");
            stopLocation = new StopLocation(postCode, municipality, municipalityId, primaryName, primaryType, secondName, secondType, suburb, latitude, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stopLocation;
    }

    private ArrayList<Route> parseRoutesFromJson(JSONArray jsonArray) {
        ArrayList<Route> routes = new ArrayList<>();
        try {
            for (int  i = 0; i < jsonArray.length(); i++) {
                JSONObject r = jsonArray.getJSONObject(i);
                int routeType = r.getInt("route_type");
                int routeId = r.getInt("route_id");
                String routeName = r.getString("route_name");
                String routeNumber = r.getString("route_number");
                String routeGtfsId = r.getString("route_gtfs_id");
                Route route = new Route(routeType, routeId, routeName, routeNumber, routeGtfsId);
                routes.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    private StopDetail parseStopDetailFromJson(JSONObject jsonObject) {
        StopDetail stopDetail = null;
        try {
            int stopID = jsonObject.getInt("stop_id");
            String stopName = jsonObject.getString("stop_name");
            String opTime = jsonObject.getString("operating_hours");
            String flexTime = jsonObject.getString("flexible_stop_opening_hours");
            ArrayList<Integer> disruptionIds = new ArrayList<>();
            JSONArray disruptionsJsonArray = jsonObject.getJSONArray("disruption_ids");
            for (int i = 0; i < disruptionsJsonArray.length(); i++) {
                int disId = disruptionsJsonArray.getInt(i);
                disruptionIds.add(disId);
            }
            String stationDescription = jsonObject.getString("station_description");
            StopLocation stopLocation = parseStopLocationFromJson(jsonObject.getJSONObject("stop_location"));
            ArrayList<Route> routes = parseRoutesFromJson(jsonObject.getJSONArray("routes"));
            int routeType = jsonObject.getInt("route_type");
            stopDetail = new StopDetail(stopID, stopName, opTime, flexTime, disruptionIds, stationDescription, routeType, stopLocation, routes);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stopDetail;
    }

    public void getStopFromId(int stopId, int routeType) {
        try {
            String url = commonDataRequest.showStopsInfo(stopId, routeType);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
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
                            StopDetail stopDetail = parseStopDetailFromJson(jsonObj.getJSONObject("stop"));
                            System.out.println(stopDetail.toString());

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
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

    public ArrayList<Stop> getStopsFromJSONArray(JSONArray array) throws JSONException{
        ArrayList<Stop> stops = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            JSONObject stopObject = array.getJSONObject(i);
            String stopName = stopObject.getString("stop_name");
            String stopSuburb = stopObject.getString("stop_suburb");
            int stopId = stopObject.getInt("stop_id");
            int stopSequence = stopObject.getInt("stop_sequence");
            double stopLatitude = stopObject.getDouble("stop_latitude");
            double stopLongitude = stopObject.getDouble("stop_longitude");
            Stop stop = new Stop(stopId, stopName);
            stop.setStop_suburb(stopSuburb);
            stop.setStop_latitude(stopLatitude);
            stop.setStop_longitude(stopLongitude);
            stops.add(stop);
        }
        return stops;
    }

    public void getStopsForDirection(Direction direction, ArrayAdapter adapter, double latitude, double longitude) {
        try {
            int routeId = direction.getRoute_id();
            int routeType = direction.getRoute_type();
            int directionId = direction.getDirection_id();
            String url = commonDataRequest.showRoutesStopByDirectionId(routeId, routeType, directionId);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
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
                            JSONArray stopArray = jsonObj.getJSONArray("stops");
                            ArrayList<Stop> stops = getStopsFromJSONArray(stopArray);
                            direction.setStops(stops);
                            float smallestDistance = Float.MAX_VALUE;
                            Stop nearestStop = null;
                            for(Stop s : stops) {
                                float[] distance = new float[1];
                                Location.distanceBetween(latitude, longitude, s.getStop_latitude(), s.getStop_longitude(), distance);
                                if (smallestDistance > distance[0]) {
                                    smallestDistance = distance[0];
                                    nearestStop = s;
                                }
                            }
                            direction.setNearestStop(nearestStop);
                            DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(activity);
                            departureHttpRequestHandler.getDepartureForRouteOnStop(direction, adapter);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
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
}
