package com.example.ptvimproved24;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ptvimproved24.NearStop;
import com.example.ptvimproved24.NearStopListAdapter;
import com.example.ptvimproved24.R;
import com.example.ptvimproved24.SavedRoute;
import com.example.ptvimproved24.SavedRouteListAdapter;
import com.example.ptvimproved24.SavedStop;
import com.example.ptvimproved24.SavedStopListAdapter;
import com.example.ptvimproved24.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateNearStopList(view);
        generateSavedStopList(view);
        generateSavedRouteList(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void generateNearStopList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.nearStops_view);

        ArrayList<String> stop1route = new ArrayList<>();
        stop1route.add("606");stop1route.add("606");stop1route.add("606");
        ArrayList<String> stop1time = new ArrayList<>();
        stop1time.add("Now");stop1time.add("17:15");stop1time.add("17:30");
        NearStop stop1=new NearStop("Port melbourne","Cruikshank St/Liardet St",293,stop1route,stop1time);

        ArrayList<String> stop2route = new ArrayList<>();
        stop2route.add("606");stop2route.add("606");stop2route.add("606");
        ArrayList<String> stop2time = new ArrayList<>();
        stop2time.add("Now");stop2time.add("17:14");stop2time.add("17:29");
        NearStop stop2=new NearStop("Albert Park","Bridport St / Richardson St",276,stop2route,stop2time);

        ArrayList<String> stop3route = new ArrayList<>();
        stop3route.add("236");stop3route.add("236");stop3route.add("236");
        ArrayList<String> stop3time = new ArrayList<>();
        stop3time.add("Now");stop3time.add("52 mins");stop3time.add("06:29 PM");
        NearStop stop3=new NearStop("Albert Park","Graham St/ Pickles St",162,stop3route,stop3time);

        ArrayList<NearStop> nearStopList = new ArrayList<>();
        nearStopList.add(stop1);
        nearStopList.add(stop2);
        nearStopList.add(stop3);

        NearStopListAdapter adapter = new NearStopListAdapter(v.getContext(),R.layout.nearstop_view, nearStopList);
        mListView.setAdapter(adapter);
    }

    public void generateSavedStopList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.SavedStop_view);

        ArrayList<String> stop1route = new ArrayList<>();
        stop1route.add("703");stop1route.add("737");stop1route.add("862");
        ArrayList<String> stop1time = new ArrayList<>();
        stop1time.add("Now");stop1time.add("Now");stop1time.add("Now");
        SavedStop stop1=new SavedStop("Clayton","Monash University",stop1route,stop1time);

        ArrayList<String> stop2route = new ArrayList<>();
        stop2route.add("862");stop2route.add("802");stop2route.add("900");
        ArrayList<String> stop2time = new ArrayList<>();
        stop2time.add("Now");stop2time.add("Now");stop2time.add("Now");
        SavedStop stop2=new SavedStop("Chadstone","Chadstone Shopping Centre / Eastern Access Rd",stop2route,stop2time);

        ArrayList<String> stop3route = new ArrayList<>();
        stop3route.add("631");stop3route.add("631");stop3route.add("631");
        ArrayList<String> stop3time = new ArrayList<>();
        stop3time.add("Now");stop3time.add("6 mins");stop3time.add("7 mins");
        SavedStop stop3=new SavedStop("Clayton","Princes Hwy / Blackburn Rd",stop3route,stop3time);

        ArrayList<SavedStop> savedStopList = new ArrayList<>();
        savedStopList.add(stop1);
        savedStopList.add(stop2);
        savedStopList.add(stop3);

        SavedStopListAdapter adapter = new SavedStopListAdapter(v.getContext(),R.layout.savedstops_view, savedStopList);
        mListView.setAdapter(adapter);
    }

    public void generateSavedRouteList(View v){
        ListView mListView = (ListView) v.findViewById(R.id.savedRoutes_view);

        SavedRoute route1=new SavedRoute("703","Oakleigh - Box Hill via Clayton");
        SavedRoute route2=new SavedRoute("3-3a","Melbourne University - East Malvern");

        ArrayList<SavedRoute> SavedRouteList = new ArrayList<>();
        SavedRouteList.add(route1);
        SavedRouteList.add(route2);

        SavedRouteListAdapter adapter = new SavedRouteListAdapter(v.getContext(),R.layout.savedroutes_view, SavedRouteList);
        mListView.setAdapter(adapter);
    }
}