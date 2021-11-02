package com.example.ptvimproved24.datastructures;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ptvimproved24.commonDataRequest;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PatternRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    private Time time;

    public PatternRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;

        time = Time.getInstance();
    }

//    private String UTCToAEST(String utc) {
//        String result = Instant.parse ( utc )
//                .atZone ( ZoneId.of ( "Australia/Sydney" ) )
//                .format (
//                        DateTimeFormatter.ofLocalizedDateTime ( FormatStyle.SHORT )
//                                .withLocale ( Locale.UK ));
//        return result;
//    }

    private ArrayList<Departure> getDeparturesOnPattern(JSONArray jsonArray) throws JSONException {
        ArrayList<Departure> departureArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject departure = jsonArray.getJSONObject(i);
            int stop_id = departure.getInt("stop_id");
            int route_id = departure.getInt("route_id");
            int run_id = departure.getInt("run_id");
            String run_ref = departure.getString("run_ref");
            int direction_id = departure.getInt("direction_id");
//            Skipping disruptions ids
            String scheduled_departure_utc = time.UTCToAEST(departure.getString("scheduled_departure_utc"));
            String estimated_departure_utc = departure.getString("estimated_departure_utc");
            boolean at_platform = departure.getBoolean("at_platform");
            String platform_number = departure.getString("platform_number");
            String flags = departure.getString("flags");
            int departure_sequence = departure.getInt("departure_sequence");

            Departure d = new Departure(stop_id,route_id,run_id,run_ref,direction_id,scheduled_departure_utc,estimated_departure_utc,at_platform,platform_number,flags,departure_sequence);
            departureArrayList.add(d);
        }
        return departureArrayList;
    }

    private ArrayList<Stop> getStopsOnPattern(JSONArray jsonArray) throws JSONException{
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

    public void getPatternRequest(int route_type, String run_ref, ArrayAdapter adapter, MapElementLayer mPinLayer, MapView mapView) throws Exception {
        String url = commonDataRequest.showPatternonRoute(run_ref, route_type);
        System.out.println("PatternURL:"+url);
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
                        JSONArray disruptions = jsonObj.getJSONArray("disruptions");
                        JSONArray departures = jsonObj.getJSONArray("departures");
                        //JSON Stops are saving as Objects
                        JSONObject stops = jsonObj.getJSONObject("stops");
                        JSONObject routes = jsonObj.getJSONObject("routes");
                        JSONObject runs = jsonObj.getJSONObject("runs");
                        JSONObject directions = jsonObj.getJSONObject("directions");

                        ArrayList<Departure> departureArrayList = getDeparturesOnPattern(departures);

                        ArrayList<Stop> patterns = new ArrayList<>();
                        for (int i = 0; i < departureArrayList.size(); i++) {
                            Departure departure = departureArrayList.get(i);
                            JSONObject stop=jsonObj.getJSONObject("stops").getJSONObject(Integer.toString(departure.getStop_id()));
                            patterns.add(new Stop(stop.getString("stop_suburb"),stop.getString("stop_name"),stop.getInt("stop_id"),stop.getInt("route_type"),stop.getDouble("stop_latitude"),stop.getDouble("stop_longitude"),departure.getScheduled_departure_utc()));
                        }
                        System.out.println("patternlen"+patterns.size());
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPinLayer.getElements().clear();
                                double avglat=0,avglng=0;
                                for (int i = 0; i < patterns.size(); i++) {
                                    MapIcon pushpin = new MapIcon();
                                    pushpin.setLocation(new Geopoint(patterns.get(i).getStop_latitude(),patterns.get(i).getStop_longitude()));
                                    pushpin.setTitle(patterns.get(i).getStop_name());
                                    avglng+=patterns.get(i).getStop_longitude();
                                    avglat+=patterns.get(i).getStop_latitude();
                                    mPinLayer.getElements().add(pushpin);
                                    MapFlyout mapFlyout = new MapFlyout();
                                    mapFlyout.setTitle(patterns.get(i).getStop_name());
                                    mapFlyout.setDescription("Suburb:"+patterns.get(i).getStop_suburb()+"\nStop Id:"+patterns.get(i).getStop_id());
                                    pushpin.setFlyout(mapFlyout);
                                }
                                avglat = avglat/patterns.size();
                                avglng = avglng/patterns.size();
                                if (route_type == 0){
                                    mapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(avglat,avglng), 11), MapAnimationKind.DEFAULT);
                                } else if(route_type == 1 || route_type ==2 || route_type ==4){
                                    mapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(avglat,avglng), 14), MapAnimationKind.DEFAULT);
                                } else {
                                    mapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(avglat,avglng), 7), MapAnimationKind.DEFAULT);
                                }

                                adapter.clear();
                                adapter.addAll(patterns);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
