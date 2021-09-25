package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.microsoft.maps.MapView;

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

public class RouteHttpRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public RouteHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    public void getRoutePathById(int routeid, stopsListAdapter adapter, MapView map) throws Exception {
        String url = commonDataRequest.showRouteInfoWithPath(routeid);
        System.out.println("Request:"+url);
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

    private ArrayList<Stop> getStoppingsList(JSONArray jsonArray) throws JSONException {
        ArrayList<Stop> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int stop_id = jsonObject.getInt("stop_id");
            int route_type = jsonObject.getInt("route_type");
            String stop_suburb = jsonObject.getString("stop_suburb");
            String stop_name = jsonObject.getString("stop_name");
            double stop_latitude = jsonObject.getDouble("stop_latitude");
            double stop_longitude = jsonObject.getDouble("stop_longitude");
            Stop s = new Stop(stop_suburb,stop_name,stop_id,route_type,stop_latitude,stop_longitude);
            result.add(s);
        }
        return result;
    }
}
