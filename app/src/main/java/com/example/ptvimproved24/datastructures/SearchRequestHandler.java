package com.example.ptvimproved24.datastructures;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ptvimproved24.commonDataRequest;
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

public class SearchRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public SearchRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    private ArrayList<Stop> getStopsFromSearch(JSONArray jsonArray) throws JSONException {
        ArrayList<Stop> stopsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int stop_distance = jsonObject.getInt("stop_distance");
            String stop_suburb = jsonObject.getString("stop_suburb");
            String stop_name = jsonObject.getString("stop_name");
            int stop_id = jsonObject.getInt("stop_id");
            int route_type = jsonObject.getInt("route_type");
            double stop_latitude = jsonObject.getDouble("stop_latitude");
            double stop_longitude = jsonObject.getDouble("stop_longitude");
            String stop_landmark = jsonObject.getString("stop_landmark");
            int stop_sequence = jsonObject.getInt("stop_sequence");
            Stop s = new Stop(stop_distance,stop_suburb,stop_name,stop_id,route_type,stop_latitude,stop_longitude,stop_landmark,stop_sequence);
            stopsList.add(s);
        }
        return stopsList;
    }

    private ArrayList<Route> getRoutesFromSearch(JSONArray jsonArray) throws JSONException{
        ArrayList<Route> result = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int route_type = jsonObject.getInt("route_type");
            int route_id = jsonObject.getInt("route_id");
            String route_name = jsonObject.getString("route_name");
            String route_number = jsonObject.getString("route_number");
            String route_gtfs_id = jsonObject.getString("route_gtfs_id");

            Route r = new Route(route_type,route_id,route_name,route_number,route_gtfs_id);
            result.add(r);
        }
        return result;
    }

    public void getSearchResults(String string, ArrayAdapter adapter) throws Exception {
        String url = commonDataRequest.showSearchResults(string);
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
                            JSONArray routesArray =  jsonObj.getJSONArray("routes");
                            JSONArray outletsArray =  jsonObj.getJSONArray("outlets");

                            ArrayList<Stop> stopsList = getStopsFromSearch(stopsArray);
                            ArrayList<Route> routesList = getRoutesFromSearch(routesArray);
                            ArrayList<SearchResults> searchResults = new ArrayList<>();
                            for (int i = 0; i < stopsList.size(); i++) {
                                searchResults.add(new SearchResults(stopsList.get(i).getStop_id(),stopsList.get(i).getRouteType(),stopsList.get(i).getStop_name(),0,stopsList.get(i).getStop_suburb()));
                            }
                            for (int i = 0; i < routesList.size(); i++) {
                                searchResults.add(new SearchResults(routesList.get(i).getRoute_id(),routesList.get(i).getRoute_type(),routesList.get(i).getRoute_gtfs_id().substring(2),1,routesList.get(i).getRoute_name()));
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.clear();
                                    adapter.addAll(searchResults);
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
