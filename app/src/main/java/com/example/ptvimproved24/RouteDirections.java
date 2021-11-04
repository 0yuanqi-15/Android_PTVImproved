package com.example.ptvimproved24;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;

import android.widget.AdapterView;

import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.ptvimproved24.datastructures.RouteDirectionAdapter;
import com.example.ptvimproved24.datastructures.RouteDirectionsRequestsHandler;
import com.example.ptvimproved24.datastructures.RouteHttpRequestHandler;

import com.example.ptvimproved24.datastructures.StopHttpRequestHandler;
import com.google.android.material.snackbar.Snackbar;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapView;
import com.microsoft.maps.OnMapElementTappedListener;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteDirections extends AppCompatActivity {
//public class RouteDirections extends AppCompatActivity implements OnMapReadyCallback {
    private Float latitude = -37.818078f;
    private Float longitude = 144.96681f;

    // Used for saved routes, showing both direction
    private static final String TAG = "RouteDirections";
    private MapView mMapView;
    private MapElementLayer mPinLayer;
    private MapFlyout mapFlyout;
    private MapIcon pushpin;
    private ListView mListView;
    private RouteDirectionAdapter routeDirectionAdapter;
    private double userLatitude;
    private double userLongitude;
    private LocationManager locationManager;
    private RouteDirectionsRequestsHandler routeDirectionHandler;
    private int lastSelectedStopId=-1;
    RouteHttpRequestHandler routeHttpRequestHandler = new RouteHttpRequestHandler(this);
    StopHttpRequestHandler stopHttpRequestHandler = new StopHttpRequestHandler(this);

    private static final int REQUEST_LOCATION = 99;

    private int route_id;
    private int route_type;
    private String route_name;
    private String route_gtfs_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routedirections);
        route_id = getIntent().getIntExtra("route_id", 1); // Get Route details to display
        route_type = getIntent().getIntExtra("route_type", 0);
        route_name = getIntent().getStringExtra("route_name");
        route_gtfs_id = getIntent().getStringExtra("route_gtfs_id");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getGeoLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        getUserLocation();
        getGeoLocation();

        //int routeid = getIntent().getIntExtra("routeid",1); // Get Route details to display
//        getRoutePathById(routeid);
        mListView = (ListView) findViewById(R.id.route_directionList);
        routeDirectionAdapter = new RouteDirectionAdapter(this, R.layout.routedirections_view, new ArrayList<>());
        mListView.setAdapter(routeDirectionAdapter);
        routeDirectionHandler = new RouteDirectionsRequestsHandler(this);
        routeDirectionHandler.getRouteDirectionById(route_id, routeDirectionAdapter, latitude, longitude);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RouteDirections.this, RouteDetails.class);
                Direction clickedDirection = routeDirectionAdapter.getItem(i);
                if (clickedDirection.getDeparturesForNearestStop().size() > 0) {
                    intent.putExtra("run_ref", clickedDirection.getDeparturesForNearestStop().get(0).getRun_ref());
                    intent.putExtra("route_type", clickedDirection.getRoute_type());
                    startActivity(intent);
                } else{
                    Snackbar.make(view, "No depature scheduled for this direction yet, \nplease check again later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        mMapView = new MapView(this, MapRenderMode.RASTER);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view)).addView(mMapView);
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        mMapView.setTransitFeaturesVisible(true);
        try {
            stopHttpRequestHandler.getRouteStopsOnRouteToBingmap(route_id,route_type,mPinLayer,mMapView); // Pop stops in
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.onCreate(savedInstanceState);

        mPinLayer.addOnMapElementTappedListener(new OnMapElementTappedListener() {
            @Override
            public boolean onMapElementTapped(MapElementTappedEventArgs e) {
                System.out.println("PinElement:"+e.mapElements);
                if(e.mapElements.size()>0){
                    pushpin = (MapIcon) e.mapElements.get(0);
//                    System.out.println("FlyoutT:"+pushpin.getFlyout().getTitle());
//                    System.out.println("FlyoutD:"+pushpin.getFlyout().getDescription().split("\\:"));
                    String[] stopdetails = pushpin.getFlyout().getDescription().split("\\:");
                    int stopid = Integer.parseInt(stopdetails[stopdetails.length-1]);
                    System.out.println("stopid:"+stopid);
                    if(stopid == lastSelectedStopId){
                        Intent i = new Intent(RouteDirections.this,stops.class);
                        i.putExtra("index",stopid);
                        i.putExtra("type",route_type);
                        i.putExtra("name",pushpin.getFlyout().getTitle());
                        i.putExtra("suburb",stopdetails[1].split("\\n")[0]);
                        startActivity(i);
                    }
                    System.out.println("Last click stopid:"+lastSelectedStopId);
                    lastSelectedStopId = stopid;
                    Toast.makeText(getApplicationContext(),"Click again to see next departures of "+pushpin.getFlyout().getTitle(),Toast.LENGTH_SHORT);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.route_menu, menu);
        String PREFERENCE_NAME = "saved_routes";
        SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Integer.toString(route_id))) {
            Drawable yourdrawable = menu.getItem(0).getIcon();
            yourdrawable.mutate();
            yourdrawable.setColorFilter(getResources().getColor(R.color.ptv_disruptions), PorterDuff.Mode.SRC_IN);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.save_route) {

            String PREFERENCE_NAME = "saved_routes";
            SharedPreferences pref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            if (pref.contains(Integer.toString(route_id))){

                View pop_view = getLayoutInflater().inflate(R.layout.popup_window_route_direction, null, false);
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
                        editor.remove(Integer.toString(route_id));
                        editor.apply();

                        Snackbar.make(mMapView, "This stop has been removed from you save-list", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        popWindow.dismiss();

                        Drawable yourdrawable = item.getIcon();
                        yourdrawable.mutate();
                        yourdrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWindow.dismiss();
                    }
                });

                return true;
            }

            HashMap<String, Object> route_info = new HashMap<String, Object>();
            route_info.put("route_name", route_name);
            route_info.put("route_type", route_type);
            route_info.put("gtfs_id", route_gtfs_id);

            SharedPreferences.Editor editor = getSharedPreferences("saved_routes", Context.MODE_PRIVATE).edit();
            JSONObject jsonObject = new JSONObject(route_info);
            String jsonString = jsonObject.toString();
            editor.putString(Integer.toString(route_id), jsonString);
            editor.apply();

            Snackbar.make(mMapView, "Route saved successfully!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            Drawable yourdrawable = item.getIcon();
            yourdrawable.mutate();
            yourdrawable.setColorFilter(getResources().getColor(R.color.ptv_disruptions), PorterDuff.Mode.SRC_IN);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserLocation() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(RouteDirections.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(RouteDirections.this, "Location Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject Location permission,some service may not available\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getGeoLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                }
            });
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("locationfragStopselect:" + location);
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } catch (SecurityException e) {
            Toast.makeText(this,
                    "GPS Has been disabled to determine the nearest stops",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
