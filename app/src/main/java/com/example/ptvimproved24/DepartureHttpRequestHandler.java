package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DepartureHttpRequestHandler {

    private final OkHttpClient client;
    private final FragmentActivity activity;

    public DepartureHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    public ArrayList<Departures> getDepartureListFromJSONArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Departures> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int stop_id = jsonObject.getInt("stop_id");
            int route_id = jsonObject.getInt("route_id");
            int run_id = jsonObject.getInt("run_id");
            String run_ref = jsonObject.getString("run_ref");
            int direction_id = jsonObject.getInt("direction_id");
            ArrayList<Integer> disruption_ids = (ArrayList<Integer>) jsonObject.get("disruption_ids");
            String schedule_depart = jsonObject.getString("scheduled_departure_utc");
            String estimated_depart_utc = jsonObject.getString("estimated_departure_utc");
            boolean at_platform = jsonObject.getBoolean("at_platform");
            String platform_number = jsonObject.getString("platform_number");
            String flags = jsonObject.getString("flags");
            int departure_sequence = jsonObject.getInt("departure_sequence");

            Departures d = new Departures(stop_id,route_id,run_id,run_ref,direction_id,disruption_ids,schedule_depart,estimated_depart_utc,at_platform,platform_number,flags,departure_sequence);
            result.add(d);
        }
        return result;
    }

    public void getNextDepartureByStopid(int stopid, int route_type){
        ArrayList<Departures> departuresArray = new ArrayList<>();
        try{
            String url = commonDataRequest.nextDeparture(stopid, route_type);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
            ArrayList<Departures> nextDeparturesList = new ArrayList<>();

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
                            ArrayList<Departures> fetchedArray = getDepartureListFromJSONArray(departures);
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
