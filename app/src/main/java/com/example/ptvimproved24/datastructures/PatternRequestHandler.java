package com.example.ptvimproved24.datastructures;

import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.ptvimproved24.commonDataRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PatternRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public ArrayList<Pattern> getPatterns(JSONArray jsonArray) throws Exception{
        ArrayList<Pattern> patterns = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

//            int run_id = jsonObject.getInt("run_id");
//            String run_ref = jsonObject.getString("run_ref");
//            int route_id = jsonObject.getInt("route_id");
//            int route_type = jsonObject.getInt("route_type");
//            int final_stop_id = jsonObject.getInt("final_stop_id");
//            String destination_name = jsonObject.getString("destination_name");
//            String status = jsonObject.getString("status");
//            int direction_id = jsonObject.getInt("direction_id");
//            int run_sequence = jsonObject.getInt("run_sequence");
//            int express_stop_count = jsonObject.getInt("express_stop_count");
//            Pattern p = new Pattern(run_ref,route_id,route_type,final_stop_id,destination_name,status,direction_id,run_sequence,express_stop_count);
//            patterns.add(p);
        }
        return patterns;
    }


    public void getPatternRequest(int route_type, String run_ref, ArrayAdapter adapter) throws Exception {
        String url = commonDataRequest.showPatternonRoute(run_ref, route_type);
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
                        JSONArray stops = jsonObj.getJSONArray("stops");
                        JSONArray runs = jsonObj.getJSONArray("runs");

                        ArrayList<Pattern> patternsArrayList = getPatterns(departures);
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
    }
}
