package com.example.ptvimproved24;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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


public class RouteDirectionsHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public RouteDirectionsHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }


//    public void getRouteDirectionById(Route route, ArrayAdapter adapter, MapView map, ArrayList<Direction> resultArrayListDirection) throws Exception {
    public void getRouteDirectionById(Route route,  ArrayList<Direction> resultArrayListDirection) throws Exception {
        int route_id = route.getRoute_id();
        String url = commonDataRequest.showDirectionsOnRoute(route_id);
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
                            JSONArray routeDirectionArray =  jsonObj.getJSONArray("directions");

                            ArrayList<Direction> directionList = getDirectionList(routeDirectionArray);
                            resultArrayListDirection.clear();
                            resultArrayListDirection.addAll(directionList);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

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


//    private ArrayList<Direction> returnRouteDirectionById(Route route) throws Exception {
//
//        ArrayList<Direction> result1 = new ArrayList<>();
//
//        int route_id = route.getRoute_id();
//        String url = commonDataRequest.showDirectionsOnRoute(route_id);
//        System.out.println("Request:"+url);
//        try {
//            Request request = new Request.Builder().url(url).build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response, ArrayList<Direction> result1 ) throws IOException {
//                    if (response.isSuccessful()) {
//                        String responseBody = response.body().string();
//                        try{
//                            JSONObject jsonObj = new JSONObject(responseBody);
//                            JSONArray routeDirectionArray =  jsonObj.getJSONArray("directions");
//
//                            ArrayList<Direction> directionList = getDirectionList(routeDirectionArray);
//                            result1 = directionList;
//
//                            activity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result1;
//    }
}
