package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment_StopSelect extends Fragment {
    private final float defaultLatitude = -37.818078f;
    private final float defaultLongitude = 144.96681f;
    private Float latitude = defaultLatitude;
    private Float longitude = defaultLongitude;
    private LocationManager locationManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng FlinderSt = new LatLng(-37.818078, 144.96681);
            googleMap.addMarker(new MarkerOptions().position(FlinderSt).title("Flinder St Station"));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            getGeoLocation();
            googleMap.getUiSettings().setMapToolbarEnabled(true);
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

    };

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
            mapFragment.getMapAsync(callback);
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
            System.out.println("locationfragStopselect:"+location);
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void getStopLists(GoogleMap googleMap) throws Exception {
        float zoomlevel = googleMap.getCameraPosition().zoom;
        if (zoomlevel <=15.5f){
            //Require more Zoomi n
            Toast.makeText(getContext(),"Please Zoomin the map to see stops",2);
        } else {
            // Require via RestAPI
            float targetLng = (float) googleMap.getCameraPosition().target.longitude;
            float targetLat = (float) googleMap.getCameraPosition().target.latitude;

            StopHttpRequestHandler stopHttpRequestHandler = new StopHttpRequestHandler(getActivity());
            stopHttpRequestHandler.getStopsFromLocation(googleMap,targetLat,targetLng);
        }
    }
}