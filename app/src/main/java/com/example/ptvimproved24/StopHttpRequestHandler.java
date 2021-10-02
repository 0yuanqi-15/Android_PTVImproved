package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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
            stop.setStopid(stopId);
            stop.setRouteType(routeType);
            stops.add(stop);
        }
        return stops;
    }

    public void getStopsFromLocation(NearStopListAdapter adapter, float latitude, float longitude) {
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
                                    if (stopsArray.get(i).getStopid()==dedupStopsArray.get(j).getStopid()){
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
}
