package com.example.ptvimproved24;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DepartureHttpRequestHandler {

    private final OkHttpClient client;
    private final FragmentActivity activity;
    private HashMap<Integer, String> routeMap;
    private HashMap<Integer, String> routeNameMap;
    private HashMap<Integer, String> routeDirectionMap;

    public DepartureHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
        routeMap = new HashMap<>();
        routeNameMap = new HashMap<>();
        routeDirectionMap = new HashMap<>();
    }

    private String UTCToAEST(String utc) {
        String result = Instant.parse ( utc )
                        .atZone ( ZoneId.of ( "Australia/Sydney" ) )
                        .format (
                        DateTimeFormatter.ofLocalizedDateTime ( FormatStyle.SHORT )
                                .withLocale ( Locale.UK ));
        return result;
    }

    private ArrayList<Departure> getDepartureListFromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Departure> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int stop_id = jsonObject.getInt("stop_id");
            int route_id = jsonObject.getInt("route_id");
            int run_id = jsonObject.getInt("run_id");
            String run_ref = jsonObject.getString("run_ref");
            int direction_id = jsonObject.getInt("direction_id");
            JSONArray disruption_ids = jsonObject.getJSONArray("disruption_ids");
            String schedule_depart = jsonObject.getString("scheduled_departure_utc");
            String estimated_depart_utc = jsonObject.getString("estimated_departure_utc");
            boolean at_platform = jsonObject.getBoolean("at_platform");
            String platform_number = jsonObject.getString("platform_number");
            String flags = jsonObject.getString("flags");
            int departure_sequence = jsonObject.getInt("departure_sequence");
            if (routeMap.get(route_id) == null) {
                routeMap.put(route_id, UTCToAEST(schedule_depart));
            }
            Departure d = new Departure(stop_id,route_id,run_id,run_ref,direction_id,new ArrayList<>(),schedule_depart,estimated_depart_utc,at_platform,platform_number,flags,departure_sequence);
            result.add(d);
        }
        return result;
    }

    private ArrayList<Route> getRouteArrayFromJSONObject(JSONObject jsonObject) throws JSONException {
        ArrayList<Route> result = new ArrayList<>();
        for (Map.Entry<Integer, String> e : routeMap.entrySet()) {
            JSONObject route = jsonObject.getJSONObject(Integer.toString(e.getKey()));
            int routeType = route.getInt("route_type");
            int routeId = route.getInt("route_id");
            String routeName = route.getString("route_name");
            String routeNumber = route.getString("route_number");
            String routeGtfsId = route.getString("route_gtfs_id");

            String[] direction = routeName.split("-");
            String[] routeDirection = direction[1].split("via");

            routeNameMap.put(routeId, routeGtfsId);
            routeDirectionMap.put(routeId, routeDirection[0]);
            Route r = new Route(routeType, routeId, routeName, routeNumber, routeGtfsId, "");
            result.add(r);
        }
        return result;
    }

    private String restructureGtfsId(String gtfsId) {
        String result = gtfsId.substring(2);
        while (result.charAt(0) == '0') {
            result = result.substring(1);
        }
        return result;
    }

    public void getNextDepartureByStopid(Stop stop, ArrayAdapter adapter){
        int stopid = stop.getStopid();
        int route_type = stop.getRouteType();
        try{
            String url = commonDataRequest.nextDeparture(route_type, stopid);
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
                            JSONArray departures = jsonObj.getJSONArray("departures");
                            JSONObject routes = jsonObj.getJSONObject("routes");
                            ArrayList<Departure> fetchedArray = getDepartureListFromJSONArray(departures);
                            ArrayList<Route> routeArray = getRouteArrayFromJSONObject(routes);
                            stop.setDeparturesObj(fetchedArray);
                            stop.setRoutesObj(routeArray);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<String> displayRoute = new ArrayList<>();
                                    ArrayList<String> displayTime = new ArrayList<>();
                                    for(Map.Entry<Integer, String> e : routeMap.entrySet()) {
                                        String name = routeNameMap.get(e.getKey());
                                        displayRoute.add(restructureGtfsId(name));
                                        displayTime.add(e.getValue());
                                    }
                                    stop.setRoutes(displayRoute);
                                    stop.setTimes(displayTime);
                                    adapter.notifyDataSetChanged();
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


    //add this method to show stop detail information
    public void getNextDepartureByStopId(int stopId, int routeType, ArrayAdapter adapter){
        try{
            String url = commonDataRequest.nextDeparture(routeType, stopId);
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
                            JSONArray departures = jsonObj.getJSONArray("departures");
                            JSONObject routes = jsonObj.getJSONObject("routes");
                            ArrayList<Departure> fetchedArray = getDepartureListFromJSONArray(departures);
                            ArrayList<Route> routeArray = getRouteArrayFromJSONObject(routes);



                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<String> displayRoute = new ArrayList<>();
                                    ArrayList<String> displayTime = new ArrayList<>();
                                    ArrayList<String> displayDetail = new ArrayList<>();

                                    for(Map.Entry<Integer, String> e : routeMap.entrySet()) {
                                        String name = routeNameMap.get(e.getKey());

                                        String direction = routeDirectionMap.get(e.getKey());
                                        //try {
                                        //    direction = commonDataRequest.showDirectionsOnRoute(e.getKey());
                                        //   System.out.println(direction);
                                        //} catch (Exception exception) {
                                        //    exception.printStackTrace();
                                        //}
                                        displayRoute.add(name);
                                        displayTime.add(e.getValue());
                                        displayDetail.add(name+"@"+e.getValue()+"@"+direction);
                                    }
                                    adapter.clear();
                                    adapter.addAll(displayDetail.subList(0, displayDetail.size()));
                                    adapter.notifyDataSetChanged();
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

}
