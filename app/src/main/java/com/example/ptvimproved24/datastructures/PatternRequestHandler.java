package com.example.ptvimproved24.datastructures;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatternRequestHandler {
    public ArrayList<Pattern> getPatterns(JSONArray jsonArray) throws Exception{
        ArrayList<Pattern> patterns = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int run_id = jsonObject.getInt("run_id");
            String run_ref = jsonObject.getString("run_ref");
            int route_id = jsonObject.getInt("route_id");
            int route_type = jsonObject.getInt("route_type");
            int final_stop_id = jsonObject.getInt("final_stop_id");
            String destination_name = jsonObject.getString("destination_name");
            String status = jsonObject.getString("status");
            int direction_id = jsonObject.getInt("direction_id");
            int run_sequence = jsonObject.getInt("run_sequence");
            int express_stop_count = jsonObject.getInt("express_stop_count");
            Pattern p = new Pattern(run_ref,route_id,route_type,final_stop_id,destination_name,status,direction_id,run_sequence,express_stop_count);
            patterns.add(p);
        }

        return patterns;
    }
}
