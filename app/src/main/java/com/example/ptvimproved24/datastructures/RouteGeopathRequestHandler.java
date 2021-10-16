package com.example.ptvimproved24.datastructures;

import com.example.ptvimproved24.Disruption;
import com.example.ptvimproved24.datastructures.RouteGeopath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class RouteGeopathRequestHandler {

    public ArrayList<RouteGeopath> getRouteGeoPath(JSONArray jsonArray) throws JSONException{
        // Get Geopath
        ArrayList<RouteGeopath> geopath = new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int direction_id = jsonObject.getInt("direction_id");
            String valid_from = jsonObject.getString("valid_from");
            String valid_to = jsonObject.getString("valid_to");
            JSONArray paths = jsonObject.getJSONArray("paths");
            JSONArray path = jsonObject.getJSONArray("paths");
            String[] arr = new String[path.length()];
            ArrayList<Float> routepath = new ArrayList<Float>();
            for(int j = 0; j < path.length(); j++) {
                arr[j] = path.getString(j).replace(", "," ");
                String[] y = arr[j].split("\\ ",0);
                for(int k = 0;j<y.length;k=k+2){
                    routepath.add(Float.parseFloat(y[k]));
                    routepath.add(Float.parseFloat(y[k+1]));
                }
            }
            RouteGeopath r=new RouteGeopath(direction_id,valid_from,valid_to,routepath);
        }
        return geopath;
    }

}
