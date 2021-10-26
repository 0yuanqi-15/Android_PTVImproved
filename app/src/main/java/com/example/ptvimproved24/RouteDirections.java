package com.example.ptvimproved24;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapElement;
import com.microsoft.maps.MapElementCollection;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapImage;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;
import com.microsoft.maps.OnMapElementTappedListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

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
        routeDirectionAdapter = new RouteDirectionAdapter(this, R.layout.routedetails_view, new ArrayList<>());
        mListView.setAdapter(routeDirectionAdapter);
        routeDirectionHandler = new RouteDirectionsRequestsHandler(this);
        routeDirectionHandler.getRouteDirectionById(route_id, routeDirectionAdapter, latitude, longitude);
        mMapView.setTransitFeaturesVisible(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RouteDirections.this, stops.class);
                Direction clickedDirection = routeDirectionAdapter.getItem(i);
                intent.putExtra("index", clickedDirection.getStops().get(0).getStop_id());
                intent.putExtra("type", clickedDirection.getStops().get(0).getRouteType());
                intent.putExtra("name", clickedDirection.getStops().get(0).getStop_name());
                intent.putExtra("suburb", clickedDirection.getStops().get(0).getStop_suburb());
                startActivity(intent);
            }
        });

        mMapView = new MapView(this, MapRenderMode.RASTER);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout)findViewById(R.id.map_view)).addView(mMapView);
        mPinLayer = new MapElementLayer();
        mMapView.getLayers().add(mPinLayer);
        try {
            stopHttpRequestHandler.getStopsOnRouteToBingmap(route_id,route_type,mPinLayer,mMapView); // Pop stops in
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.save_route) {
            route_id = getIntent().getIntExtra("route_id", 1); // Get Route details to display
            route_type = getIntent().getIntExtra("route_type", 0);
            SharedPreferences.Editor editor = getSharedPreferences("saved_routes", Context.MODE_PRIVATE).edit();
            editor.putInt(Integer.toString(route_id), route_type);
            editor.apply();
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
