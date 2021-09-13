package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ptvimproved24.databinding.FragmentHomeBinding;

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


public class Fragment_Disruptions extends Fragment {

    private FragmentHomeBinding binding;
    OkHttpClient client = new OkHttpClient();
    ListView mListView;
    DisruptionsListAdapter adapter;
    DisruptionHttpRequestHandler disruptionsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_disruptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.fragments_view);
        adapter = new DisruptionsListAdapter(view.getContext(),R.layout.disruptions_view, new ArrayList<>());
        mListView.setAdapter(adapter);
        disruptionsListAdapter = new DisruptionHttpRequestHandler(getActivity());
        getDisruptionInfo(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // From Wenkai
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
    private ArrayList<Disruptions> getArrayListFromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Disruptions> result = new ArrayList<>();

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
            Disruptions d = new Disruptions(id,title,reflink,description,disruption_status,published_on,routesinfo,stopsList,display_status);
            d.setDescription(description);
            d.setDisplay_status(display_status);
            d.setReflink(reflink);
            result.add(d);
        }
        return result;
    }

    public void getDisruptionInfo(View v){
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

                            ArrayList<Disruptions> generalArray = getArrayListFromJsonArray(general);
                            ArrayList<Disruptions> metroTrainArray = getArrayListFromJsonArray(metroTrain);
                            ArrayList<Disruptions> metroTramArray = getArrayListFromJsonArray(metroTram);
                            ArrayList<Disruptions> metroBusArray = getArrayListFromJsonArray(metroBus);
                            ArrayList<Disruptions> regionalTrainArray = getArrayListFromJsonArray(regionalTrain);
                            ArrayList<Disruptions> regionalCoachArray = getArrayListFromJsonArray(regionalCoach);
                            ArrayList<Disruptions> regionalBusArray = getArrayListFromJsonArray(regionalBus);
                            ArrayList<Disruptions> schoolBusArray = getArrayListFromJsonArray(schoolBus);
                            ArrayList<Disruptions> telebusArray = getArrayListFromJsonArray(telebus);
                            ArrayList<Disruptions> nightBusArray = getArrayListFromJsonArray(nightBus);
                            ArrayList<Disruptions> ferryArray = getArrayListFromJsonArray(ferry);
                            ArrayList<Disruptions> interstateTrainArray = getArrayListFromJsonArray(interstateTrain);
                            ArrayList<Disruptions> skybusArray = getArrayListFromJsonArray(skybus);
                            ArrayList<Disruptions> taxiArray = getArrayListFromJsonArray(taxi);

//                            ListView disruptionListview = v.findViewById(R.id.disruptions_listview);
//                            DisruptionsListAdapter adapter = new DisruptionsListAdapter(v.getContext(),R.layout.disruptions_view,selectedDisruptionList);

//                            System.out.println(generalArray.size());
//                            System.out.println(metroTrainArray.size());
//                            System.out.println(metroTramArray.size());
//                            System.out.println(metroBusArray.size());
//                            System.out.println(regionalTrainArray.size());
//                            System.out.println(regionalCoachArray.size());
//                            System.out.println(regionalBusArray.size());
//                            System.out.println(schoolBusArray.size());
//                            System.out.println(telebusArray.size());
//                            System.out.println(nightBusArray.size());
//                            System.out.println(ferryArray.size());
//                            System.out.println(interstateTrainArray.size());
//                            System.out.println(skybusArray.size());
//                            System.out.println(taxiArray.size());
                            //TODO: add the data to UI
                        } catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // From Mutian
    public void getDisruptionInfo(){
        disruptionsListAdapter.getAllDisruptions(adapter);

    }
}
