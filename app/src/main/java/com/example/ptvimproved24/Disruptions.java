package com.example.ptvimproved24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.ptvimproved24.databinding.DisruptionsViewBinding;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class Disruptions extends AppCompatActivity {

    private DisruptionsViewBinding binding;
    OkHttpClient client = new OkHttpClient();
    ListView mListView;
    DisruptionListAdapter adapter;
    DisruptionHttpRequestHandler disruptionHttpRequestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disruptions);

        mListView = (ListView) this.findViewById(R.id.disruptions_listview);
        adapter = new DisruptionListAdapter(this,R.layout.disruptions_view, new ArrayList<>());
        mListView.setAdapter(adapter);
        disruptionHttpRequestHandler = new DisruptionHttpRequestHandler(this);
        getDisruptionInfo();
    }
    public void getDisruptionInfo(){
        disruptionHttpRequestHandler.getAllDisruptions(adapter);
    }

}