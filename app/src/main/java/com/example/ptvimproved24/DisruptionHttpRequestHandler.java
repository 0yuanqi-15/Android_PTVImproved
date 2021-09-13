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

public class DisruptionHttpRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public DisruptionHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    private ArrayList<Routes> getRoutesListFromJSONArray(JSONArray routesArray) throws JSONException{
        ArrayList<Routes> routes = new ArrayList<>();

        for (int j=0;j<routesArray.length();j++){
            JSONObject jsonobject = routesArray.getJSONObject(j);
            int route_type = jsonobject.getInt("route_type");
            int route_id = jsonobject.getInt("route_id");
            String route_name = jsonobject.getString("route_name");
            String route_number = jsonobject.getString("route_number");
            String route_gtfs_id = jsonobject.getString("route_gtfs_id");
            String direction = jsonobject.getString("direction");
            Routes r = new Routes(route_type,route_id,route_name,route_number,route_gtfs_id,direction);
            routes.add(r);
        }

        return routes;
    }

    private ArrayList<Stoppings> getStopsListFromJSArray(JSONArray stopsArray) throws JSONException{
        ArrayList<Stoppings> stopsList = new ArrayList<>();
        for (int j=0; j<stopsArray.length();j++){
            JSONObject jsonObject = stopsArray.getJSONObject(j);
            int stops_id = jsonObject.getInt("stops_id");
            String stops_name = jsonObject.getString("stops_name");
            Stoppings s = new Stoppings(stops_id,stops_name);
            stopsList.add(s);
        }
        return stopsList;
    }

    private ArrayList<Disruption> getArrayListFromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Disruption> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("disruption_id");
            String title = jsonObject.getString("title");
            String reflink = jsonObject.getString("url");
            String description = jsonObject.getString("description");
            String disruption_status = jsonObject.getString("disruption_status");
            String published_on = jsonObject.getString("published_on");
            String from_date = jsonObject.getString("from_date");
            String to_date = jsonObject.getString("to_date");
            String disruption_type = jsonObject.getString("disruption_type");
            ArrayList<Routes> routesinfo = getRoutesListFromJSONArray(jsonObject.getJSONArray("routes"));
            ArrayList<Stoppings> stopsList = getStopsListFromJSArray(jsonObject.getJSONArray("stops"));
            boolean display_on_board = jsonObject.getBoolean("display_on_board");
            boolean display_status = jsonObject.getBoolean("display_status");

            //TODO: create mapping between type String and int
            Disruption d = new Disruption(id,title,reflink,description,disruption_status,published_on,routesinfo,stopsList,display_status);
            d.setDescription(description);
            d.setDisruption_status(disruption_status);
            d.setReflink(reflink);
            result.add(d);
        }
        return result;
    }

    public void getAllDisruptions(DisruptionListAdapter adapter) {
        try {
            System.out.println(commonDataRequest.disruptions());
            String url = commonDataRequest.disruptions();
            Request request = new Request.Builder().url(url).build();
            StringBuilder builder = new StringBuilder();
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
                            JSONObject allDisruption = jsonObj.getJSONObject("disruptions");
                            JSONArray general = allDisruption.getJSONArray("general");
                            JSONArray metroTrain = allDisruption.getJSONArray("metro_train");
                            JSONArray metroTram = allDisruption.getJSONArray("metro_tram");
                            JSONArray metroBus = allDisruption.getJSONArray("metro_bus");
                            JSONArray regionalTrain = allDisruption.getJSONArray("regional_train");
                            JSONArray regionalCoach = allDisruption.getJSONArray("regional_coach");
                            JSONArray regionalBus = allDisruption.getJSONArray("regional_bus");
                            JSONArray schoolBus = allDisruption.getJSONArray("school_bus");
                            JSONArray telebus = allDisruption.getJSONArray("telebus");
                            JSONArray nightBus = allDisruption.getJSONArray("night_bus");
                            JSONArray ferry = allDisruption.getJSONArray("ferry");
                            JSONArray interstateTrain = allDisruption.getJSONArray("interstate_train");
                            JSONArray skybus = allDisruption.getJSONArray("skybus");
                            JSONArray taxi = allDisruption.getJSONArray("taxi");

                            ArrayList<Disruption> generalArray = getArrayListFromJsonArray(general);
                            ArrayList<Disruption> metroTrainArray = getArrayListFromJsonArray(metroTrain);
                            ArrayList<Disruption> metroTramArray = getArrayListFromJsonArray(metroTram);
                            ArrayList<Disruption> metroBusArray = getArrayListFromJsonArray(metroBus);
                            ArrayList<Disruption> regionalTrainArray = getArrayListFromJsonArray(regionalTrain);
                            ArrayList<Disruption> regionalCoachArray = getArrayListFromJsonArray(regionalCoach);
                            ArrayList<Disruption> regionalBusArray = getArrayListFromJsonArray(regionalBus);
                            ArrayList<Disruption> schoolBusArray = getArrayListFromJsonArray(schoolBus);
                            ArrayList<Disruption> telebusArray = getArrayListFromJsonArray(telebus);
                            ArrayList<Disruption> nightBusArray = getArrayListFromJsonArray(nightBus);
                            ArrayList<Disruption> ferryArray = getArrayListFromJsonArray(ferry);
                            ArrayList<Disruption> interstateTrainArray = getArrayListFromJsonArray(interstateTrain);
                            ArrayList<Disruption> skybusArray = getArrayListFromJsonArray(skybus);
                            ArrayList<Disruption> taxiArray = getArrayListFromJsonArray(taxi);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addAll(metroBusArray);
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
