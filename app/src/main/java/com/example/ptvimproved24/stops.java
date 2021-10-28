package com.example.ptvimproved24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.ptvimproved24.datastructures.DepartureHttpRequestHandler;
import com.example.ptvimproved24.datastructures.Route;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ptvimproved24.databinding.ActivityStopsBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class stops extends AppCompatActivity {

    private ActivityStopsBinding binding;
    private ListView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStopsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());

        //show information in the layout
        detail = (ListView) findViewById(R.id.stops_nextservice);
        detail.setNestedScrollingEnabled(true);
        Intent intent = getIntent();
        int stopId = intent.getIntExtra("index", 0);
        int routeType = intent.getIntExtra("type", 0);
        String stopName = intent.getStringExtra("name");
        String stopSuburb = intent.getStringExtra("suburb");

        toolBarLayout.setTitle(stopName);
        TextView stopsuburb = findViewById(R.id.text_stopsuburb);
        stopsuburb.setText(stopSuburb);

        ArrayList<Route> stopDetail = new ArrayList<>();

        DepartureHttpRequestHandler departureHttpRequestHandler = new DepartureHttpRequestHandler(this);

        StopDetailAdapter adapter = new StopDetailAdapter(this, R.layout.stopdetail, stopDetail);

        detail.setAdapter(adapter);

        departureHttpRequestHandler.getNextDepartureByStopId(stopId, routeType, adapter);

        detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), RouteDirections.class);
                Route route = adapter.getItem(i);

                intent.putExtra("route_id", route.getRoute_id());
                intent.putExtra("route_type", route.getRoute_type());
                intent.putExtra("route_name", route.getRoute_name());
                intent.putExtra("route_gtfs_id", route.getRoute_gtfs_id());

                startActivity(intent);

            }
        });

        String PREFERENCE_NAME = "SavedStops";
        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
                if (pref.contains(Integer.toString(stopId))){

                    View pop_view = getLayoutInflater().inflate(R.layout.popup_window_stop_detail, null, false);
                    final PopupWindow popWindow = new PopupWindow(pop_view,
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                    popWindow.setTouchable(true);
                    popWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));

                    popWindow.showAtLocation(pop_view, Gravity.CENTER, 0, 0);

                    Button btn_yes = (Button) pop_view.findViewById(R.id.btn_ok);
                    Button btn_cancel = (Button) pop_view.findViewById(R.id.btn_cancel);

                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                            editor.remove(Integer.toString(stopId));
                            editor.apply();

                            Snackbar.make(view, "This stop has been removed from you save-list", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            popWindow.dismiss();
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popWindow.dismiss();
                        }
                    });



//                    Snackbar.make(view, "You have already saved this stop!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    return;
                }

                HashMap<String, Object> stop_info = new HashMap<String, Object>();
                stop_info.put("stop_name", stopName);
                stop_info.put("route_type", routeType);
                stop_info.put("suburb", stopSuburb);

                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                JSONObject jsonObject = new JSONObject(stop_info);
                String jsonString = jsonObject.toString();
                editor.putString(Integer.toString(stopId), jsonString);

                editor.apply();

                String TAG = "MyActivity";
                Log.i(TAG, "write success");
                Snackbar.make(view, "Stop saved successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}