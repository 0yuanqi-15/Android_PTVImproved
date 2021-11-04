package com.example.ptvimproved24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisruptionDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disruption_detail);

        WebView myWebView = (WebView) findViewById(R.id.webview_details);
        myWebView.setWebViewClient(new WebViewClient());

        Intent i = getIntent();
        int disruption_id = i.getIntExtra("disruptionid", 0);
        String reflink = i.getStringExtra("disruptionlink");

        if (reflink != null) {
            myWebView.loadUrl(reflink);
        }
    }
}