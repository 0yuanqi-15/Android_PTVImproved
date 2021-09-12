package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_disruptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDisruptionInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private ArrayList<Disruptions> getArrayListFromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Disruptions> result = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("disruption_id");
            String title = jsonObject.getString("title");
            String details = jsonObject.getString("description");
            String status = jsonObject.getString("disruption_status");
            String dateTime = jsonObject.getString("from_date");
            String reflink = jsonObject.getString("url");
            String type = jsonObject.getString("disruption_type");
            //TODO: create mapping between type String and int
            Disruptions d = new Disruptions(id, title, dateTime, 1);
            d.setDetails(details);
            d.setStatus(status);
            d.setReflink(reflink);
            result.add(d);
        }
        return result;
    }

    public void getDisruptionInfo(){
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
                        } catch (JSONException e) {
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