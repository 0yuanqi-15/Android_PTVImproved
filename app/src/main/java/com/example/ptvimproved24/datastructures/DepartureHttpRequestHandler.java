package com.example.ptvimproved24.datastructures;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ptvimproved24.Direction;
import com.example.ptvimproved24.commonDataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DepartureHttpRequestHandler {

    private final OkHttpClient client;
    private final FragmentActivity activity;
    private HashMap<Integer, ArrayList<String>> routeMap;
    private HashMap<Integer, String> routeNameMap;

    private HashMap<String, String> runDestinationMap;

    private HashMap<Integer, ArrayList<String>> routeRunMap;

    private JSONObject runs;
    //private HashMap<Integer, String> routeDirectionMap;

    public DepartureHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
        routeMap = new HashMap<>();
        routeNameMap = new HashMap<>();

        runDestinationMap = new HashMap<>();
        routeRunMap = new HashMap<>();
        runs = new JSONObject();

       // routeDirectionMap = new HashMap<>();
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
            if (Time.getInstance().timeGap(Time.getInstance().UTCToAEST(schedule_depart)) >= 0) {
                if (routeMap.get(route_id) == null) {
                    ArrayList<String> newArray = new ArrayList<>();
                    newArray.add(Time.getInstance().UTCToAEST(schedule_depart));
                    routeMap.put(route_id, newArray);
                } else {
                    ArrayList<String> currentArray = routeMap.get(route_id);
                    currentArray.add(Time.getInstance().UTCToAEST(schedule_depart));
                }
                Departure d = new Departure(stop_id,route_id,run_id,run_ref,direction_id,schedule_depart,estimated_depart_utc,at_platform,platform_number,flags,departure_sequence);
                result.add(d);
            }
        }
        return result;
    }

    //change this to additionally get destination name of the departure
    private ArrayList<Departure> getDepartureArrayListFromJSONArray(JSONArray jsonArray) throws JSONException {
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
            if (Time.getInstance().timeGap(Time.getInstance().UTCToAEST(schedule_depart)) >= 0) {
                if (routeMap.get(route_id) == null) {
                    ArrayList<String> newArray = new ArrayList<>();
                    newArray.add(Time.getInstance().UTCToAEST(schedule_depart));
                    routeMap.put(route_id, newArray);

                    ArrayList<String> newRunArray = new ArrayList<>();
                    //System.out.println(runs);
                    JSONObject run = runs.getJSONObject(run_ref);
                    String runDestination = run.getString("destination_name");
                    newRunArray.add(run_ref);
                    routeRunMap.put(route_id, newRunArray);
                    runDestinationMap.put(run_ref, runDestination);

                } else {
                    ArrayList<String> currentArray = routeMap.get(route_id);
                    currentArray.add(Time.getInstance().UTCToAEST(schedule_depart));

                    ArrayList<String> currentRunArray = routeRunMap.get(route_id);
                    JSONObject run = runs.getJSONObject(run_ref);
                    String runDestination = run.getString("destination_name");
                    currentRunArray.add(run_ref);
                    runDestinationMap.put(run_ref, runDestination);

                }
                Departure d = new Departure(stop_id, route_id, run_id, run_ref, direction_id, schedule_depart, estimated_depart_utc, at_platform, platform_number, flags, departure_sequence);
                result.add(d);
            }
        }
        return result;
    }

    private ArrayList<Route> getRouteArrayFromJSONObject(JSONObject jsonObject) throws JSONException {
        ArrayList<Route> result = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<String>> e : routeMap.entrySet()) {
            JSONObject route = jsonObject.getJSONObject(Integer.toString(e.getKey()));
            int routeType = route.getInt("route_type");
            int routeId = route.getInt("route_id");
            String routeName = route.getString("route_name");
            String routeNumber = route.getString("route_number");
            String routeGtfsId = route.getString("route_gtfs_id");

            //String[] direction = routeName.split("-");
            //String[] routeDirection = direction[1].split("via");

            routeNameMap.put(routeId, routeGtfsId);
            //routeDirectionMap.put(routeId, routeDirection[0]);
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
        int stopid = stop.getStop_id();
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
                                    for(Map.Entry<Integer, ArrayList<String>> e : routeMap.entrySet()) {
                                        String name = routeNameMap.get(e.getKey());
                                        displayRoute.add(restructureGtfsId(name));
                                        displayTime.add(e.getValue().get(0));
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

    public void getDepartureForRouteOnStop(Direction direction, ArrayAdapter adapter) {
        try{
            String url = commonDataRequest.showRouteDepartureOnStop(direction.getRoute_type(),
                    direction.getNearestStop().getStop_id(), direction.getRoute_id(), direction.getDirection_id());
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
                            ArrayList<Departure> departuresArray = getDepartureListFromJSONArray(departures);
                            for (Departure d : departuresArray) {
                                d.setScheduled_departure_utc(Time.getInstance().UTCToAEST(d.getScheduled_departure_utc()));
                            }
                            direction.setDeparturesForNearestStop(departuresArray);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

                            //get runs information
                            runs = jsonObj.getJSONObject("runs");

                            ArrayList<Departure> departureArrayList = getDepartureArrayListFromJSONArray(departures);
                            ArrayList<Route> routeArray = getRouteArrayFromJSONObject(routes);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<Route> resultArray = new ArrayList<>();
                                    for(Route r : routeArray) {
                                        ArrayList<String> times = routeMap.get(r.getRoute_id());

                                        //get runRefs
                                        ArrayList<String> runRefs = routeRunMap.get(r.getRoute_id());

                                        int fetchedIndex = Math.min(10, times.size());
                                        for (int i = 0; i < fetchedIndex; i ++) {
                                            Route newRoute = new Route(r);
                                            newRoute.setScheduleDepart(times.get(i));
                                            String gtfsId = restructureGtfsId(newRoute.getRoute_gtfs_id());
                                            newRoute.setRoute_gtfs_id(gtfsId);

                                            //record destination for the route
                                            newRoute.setRun_ref(runRefs.get(i));
                                            newRoute.setDestination_name(runDestinationMap.get(runRefs.get(i)));
                                            resultArray.add(newRoute);
                                        }
                                    }
                                    adapter.clear();
                                    adapter.addAll(resultArray);
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

    public void getNextDepartureBySavedStop(SavedStop stop, ArrayAdapter adapter){
        int stopid = Integer.parseInt(stop.getStopid());
        int route_type = Integer.parseInt(stop.getRouteType());
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

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<String> displayRoute = new ArrayList<>();
                                    ArrayList<String> displayTime = new ArrayList<>();
                                    for(Map.Entry<Integer, ArrayList<String>> e : routeMap.entrySet()) {
                                        String name = routeNameMap.get(e.getKey());
                                        displayRoute.add(restructureGtfsId(name));
                                        displayTime.add(e.getValue().get(0));
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
}
