package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ptvimproved24.databinding.FragmentHomeBinding;
import com.example.ptvimproved24.datastructures.Disruption;
import com.example.ptvimproved24.datastructures.DisruptionHttpRequestHandler;

import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class Fragment_Disruptions extends Fragment {

    private FragmentHomeBinding binding;
    OkHttpClient client = new OkHttpClient();
    ListView mListView;
    DisruptionListAdapter adapter;
    DisruptionHttpRequestHandler disruptionHttpRequestHandler;

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
        adapter = new DisruptionListAdapter(view.getContext(),R.layout.disruptions_view, new ArrayList<>());
        mListView.setAdapter(adapter);
        disruptionHttpRequestHandler = new DisruptionHttpRequestHandler(getActivity());
        getDisruptionInfo();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Disruption d = adapter.getItem(i);

                Intent intent = new Intent(getActivity(), DisruptionDetail.class);
                intent.putExtra("disruptionid", d.getDisruptionid());
                intent.putExtra("disruptionlink",d.getReflink());
                String excludelink = "http://ptv.vic.gov.au/live-travel-updates/";
                if(d.getReflink().equals(excludelink)){
                    Toast.makeText(getContext(),"This is a live disruption notice, PTV has not given out website. Please aware the cabin/station PA", Toast.LENGTH_LONG).show();
                }else {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void getDisruptionInfo(){
        disruptionHttpRequestHandler.getAllDisruptions(adapter);
    }
}
