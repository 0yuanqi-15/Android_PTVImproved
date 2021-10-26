package com.example.ptvimproved24;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//
//import org.mozilla.geckoview.GeckoRuntime;
//import org.mozilla.geckoview.GeckoSession;
//import org.mozilla.geckoview.GeckoView;

public class Fragment_JourneyPlanner extends Fragment {
    private static final String URL = "https://www.ptv.vic.gov.au/journey";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_journey_planner, container, false);
        WebView mWebView = (WebView) v.findViewById(R.id.frag_journey_webview);
        mWebView.getSettings().setUserAgentString("");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        mWebView.loadUrl(URL);
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }


//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        return super.onCreateView(inflater, container, savedInstanceState);
//        View v= inflater.inflate(R.layout.fragment_journey_planner, container, false);
//
//
//        return v;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}