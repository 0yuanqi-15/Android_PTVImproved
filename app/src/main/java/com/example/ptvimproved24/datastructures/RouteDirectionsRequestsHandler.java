package com.example.ptvimproved24.datastructures;

import android.util.Log;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RouteDirectionsRequestsHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public RouteDirectionsRequestsHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }


//    public void getRouteDirectionById(Route route, ArrayAdapter adapter, MapView map, ArrayList<Direction> resultArrayListDirection) throws Exception {
    public void getRouteDirectionById(int route_id,  ArrayAdapter adapter, double latitude, double longitude) {
        try {
            String url = commonDataRequest.showDirectionsOnRoute(route_id);
            Log.d("","Request:"+url);
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
                            JSONArray routeDirectionArray =  jsonObj.getJSONArray("directions");

                            ArrayList<Direction> directionList = getDirectionList(routeDirectionArray);

                            for (Direction direction : directionList) {
                                StopHttpRequestHandler stopHttpRequestHandler = new StopHttpRequestHandler(activity);
                                stopHttpRequestHandler.getStopsForDirection(direction, adapter, latitude, longitude);
                            }

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.clear();
                                    adapter.addAll(directionList);
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

    private ArrayList<Direction> getDirectionList(JSONArray jsonArray) throws JSONException {
        ArrayList<Direction> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int route_type = jsonObject.getInt("route_type");
            int route_id = jsonObject.getInt("route_id");
            int direction_id = jsonObject.getInt("direction_id");
            String direction_name = jsonObject.getString("direction_name");
            String route_direction_description = jsonObject.getString("route_direction_description");

            Direction d = new Direction(route_type, route_id, direction_id,route_direction_description, direction_name);
            result.add(d);
        }
        return result;
    }
}
