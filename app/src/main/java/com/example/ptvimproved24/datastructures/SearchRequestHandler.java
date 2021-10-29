package com.example.ptvimproved24.datastructures;

import androidx.fragment.app.FragmentActivity;

import okhttp3.OkHttpClient;

public class SearchRequestHandler {
    private OkHttpClient client;
    private FragmentActivity activity;

    public SearchRequestHandler(FragmentActivity act) {
        client = new OkHttpClient();
        activity = act;
    }


}
