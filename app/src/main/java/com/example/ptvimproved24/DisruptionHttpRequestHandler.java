package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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

public class DisruptionHttpRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public DisruptionHttpRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }

    private ArrayList<Disruption> getArrayListFromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<Disruption> result = new ArrayList<>();
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
            Disruption d = new Disruption(id, title, dateTime, 1);
            d.setDetails(details);
            d.setStatus(status);
            d.setReflink(reflink);
            result.add(d);
        }
        return result;
    }

    public void getAllDisruptions(DisruptionsListAdapter adapter) {
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

                            ArrayList<Disruption> generalArray = getArrayListFromJsonArray(general);
                            ArrayList<Disruption> metroTrainArray = getArrayListFromJsonArray(metroTrain);
                            ArrayList<Disruption> metroTramArray = getArrayListFromJsonArray(metroTram);
                            ArrayList<Disruption> metroBusArray = getArrayListFromJsonArray(metroBus);
                            ArrayList<Disruption> regionalTrainArray = getArrayListFromJsonArray(regionalTrain);
                            ArrayList<Disruption> regionalCoachArray = getArrayListFromJsonArray(regionalCoach);
                            ArrayList<Disruption> regionalBusArray = getArrayListFromJsonArray(regionalBus);
                            ArrayList<Disruption> schoolBusArray = getArrayListFromJsonArray(schoolBus);
                            ArrayList<Disruption> telebusArray = getArrayListFromJsonArray(telebus);
                            ArrayList<Disruption> nightBusArray = getArrayListFromJsonArray(nightBus);
                            ArrayList<Disruption> ferryArray = getArrayListFromJsonArray(ferry);
                            ArrayList<Disruption> interstateTrainArray = getArrayListFromJsonArray(interstateTrain);
                            ArrayList<Disruption> skybusArray = getArrayListFromJsonArray(skybus);
                            ArrayList<Disruption> taxiArray = getArrayListFromJsonArray(taxi);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addAll(metroBusArray);
                                    adapter.notifyDataSetChanged();
                                }
                            });
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
