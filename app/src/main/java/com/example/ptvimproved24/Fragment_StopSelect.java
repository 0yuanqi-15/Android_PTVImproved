package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ptvimproved24.datastructures.Stop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_StopSelect extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener {
    private static final String TAG = "Stop Select Fragment:";

    private final float defaultLatitude = -37.818078f;
    private final float defaultLongitude = 144.96681f;
    private Float latitude = defaultLatitude;
    private Float longitude = defaultLongitude;
    private LocationManager locationManager;

    private GoogleMap map;
    private boolean isCanceled = false;
    private CompoundButton animateToggle;
    private CompoundButton customDurationToggle;
    private SeekBar customDurationBar;
    private PolylineOptions currPolylineOptions;
    MarkerOptions markerOptions = new MarkerOptions();
    ArrayList<Stop> stopsArray = new ArrayList<>();
    private Marker mLastSelectedMarker;

    int lastStopClicked;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        getGeoLocation();
        return inflater.inflate(R.layout.fragment_navigation_mapselect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
    }

    private void getGeoLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
            Toast.makeText(getActivity(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnCameraMoveListener(this::onCameraMove);
        map.setOnCameraIdleListener(this::onCameraIdle);
        map.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
        map.setOnCameraMoveCanceledListener(this::onCameraMoveCanceled);
        map.setOnInfoWindowClickListener(this);
        map.setOnInfoWindowCloseListener(this);
        map.setOnMarkerClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        map.getUiSettings().setMapToolbarEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (!isCanceled) {
            map.clear();
        }

        String reasonText = "UNKNOWN_REASON";
        currPolylineOptions = new PolylineOptions().width(0);   //Showing Polyline Gestures
        switch (reason) {
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                currPolylineOptions.color(Color.BLUE);
                reasonText = "GESTURE";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                currPolylineOptions.color(Color.RED);
                reasonText = "API_ANIMATION";
                break;
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                currPolylineOptions.color(Color.GREEN);
                reasonText = "DEVELOPER_ANIMATION";
                break;
        }
        Log.d(TAG, "onCameraMoveStarted(" + reasonText + ")");
        addCameraTargetToPath();
    }

    @Override
    public void onCameraMove() {
        // When the camera is moving, add its target to the current path we'll draw on the map.
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
        }
        Log.d(TAG, "onCameraMove");
    }

    @Override
    public void onCameraMoveCanceled() {
        // When the camera stops moving, add its target to the current path, and draw it on the map.
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            map.addPolyline(currPolylineOptions);
        }
        isCanceled = true;  // Set to clear the map when dragging starts again.
        currPolylineOptions = null;
        Log.d(TAG, "onCameraMoveCancelled");
//        getGeoLocation();
    }

    @Override
    public void onCameraIdle() {
        if (currPolylineOptions != null) {
            addCameraTargetToPath();
            map.addPolyline(currPolylineOptions);
        }
        currPolylineOptions = null;
        isCanceled = false;  // Set to *not* clear the map when dragging starts again.
        Log.d(TAG, "onCameraIdle");
//        System.out.println("Camera Centre:"+getCameraCentre().latitude+"\t"+getCameraCentre().longitude);
        getStopsFromLocation((float) map.getCameraPosition().target.latitude, (float) map.getCameraPosition().target.longitude);
//        getStopsFromLocation(-37.8060656f, 145.1571894f);
    }

    private void addCameraTargetToPath() {
        LatLng target = map.getCameraPosition().target;
        currPolylineOptions.add(target);
    }

    public void getStopsFromLocation(float latitude, float longitude) {
        OkHttpClient client = new OkHttpClient();
        try {
            String url = commonDataRequest.nearByStopsOnSelect(latitude, longitude);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        stopsArray.clear();
                        try {
                            JSONObject jsonObj = new JSONObject(responseBody);
                            JSONArray stops = jsonObj.getJSONArray("stops");
                            for (int i = 0; i < stops.length(); i++) {
                                JSONObject jsonObject = stops.getJSONObject(i);
                                String stop_suburb = jsonObject.getString("stop_suburb");
                                int route_type = jsonObject.getInt("route_type");
                                float stop_latitude = (float) jsonObject.getDouble("stop_latitude");
                                float stop_longitude = (float) jsonObject.getDouble("stop_longitude");
                                int stop_id = jsonObject.getInt("stop_id");
                                String stop_name = jsonObject.getString("stop_name");
                                Stop s = new Stop(stop_suburb, route_type, stop_latitude, stop_longitude, stop_id, stop_name);
                                stopsArray.add(s);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < stopsArray.size(); i++) {
                                        System.out.println(stopsArray.get(i));
                                        map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
                                                title(stopsArray.get(i).getStop_name()).snippet(stopsArray.get(i).getRouteType()+","+stopsArray.get(i).getStop_id()));
//                                        switch (stopsArray.get(i).getRouteType()){
//                                            case 0:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//                                                break;
//                                            case 1:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//                                                break;
//                                            case 2:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//                                                break;
//                                            case 3:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//                                                break;
//                                            case 4:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//                                                break;
//                                            default:
//                                                map.addMarker(new MarkerOptions().position(new LatLng(stopsArray.get(i).getStop_latitude(), stopsArray.get(i).getStop_longitude())).
//                                                        title(stopsArray.get(i).getStop_name()).snippet("stop id:" + stopsArray.get(i).getStop_id()));
//
//                                        }
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        // Markers have a z-index that is settable and gettable.
        float zIndex = marker.getZIndex() + 1.0f;
        marker.setZIndex(zIndex);
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
        int stop_id = Integer.parseInt(marker.getSnippet().substring(2));
        int route_type = Integer.parseInt(marker.getSnippet().substring(0,1));
        if(lastStopClicked == stop_id){
            Intent i = new Intent(getActivity(), stops.class);
            i.putExtra("index",stop_id);
            for (int ii = 0; ii < stopsArray.size(); ii++) {
                if(stopsArray.get(ii).getStop_id() == stop_id){
                    i.putExtra("type",stopsArray.get(ii).getRouteType());
                    i.putExtra("name",stopsArray.get(ii).getStop_name());
                    i.putExtra("suburb",stopsArray.get(ii).getStop_suburb());
                }
            }
            startActivity(i);
        }
        lastStopClicked = stop_id;
        mLastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }
}