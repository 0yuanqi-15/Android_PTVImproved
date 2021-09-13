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

import java.util.ArrayList;


public class Fragment_Disruptions extends Fragment {

    private FragmentHomeBinding binding;
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
        getDisruptionInfo();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void getDisruptionInfo(){
        disruptionsListAdapter.getAllDisruptions(adapter);
    }
}