package com.example.ptvimproved24;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_StopSelect extends Fragment {
    private double lat,lng;
    LatLng userLatlng;
    LocationListener locationListener;
    LocationManager locationManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng FlinderSt = new LatLng(-37.818078, 144.96681);
            googleMap.addMarker(new MarkerOptions().position(FlinderSt).title("Flinder St Station"));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(FlinderSt));
            if (getLocation()){
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
            }
            GoogleMap mMap = googleMap;
            locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    userLatlng = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLatlng).title("Your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatlng));
                }
            };
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

    public boolean getLocation() {
        String serviceString = getActivity().LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(serviceString);
        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        Log.i("location",location.toString());
        if(location!=null){
            userLatlng=new LatLng(location.getLatitude(),location.getLongitude());
        }
        locationManager.requestLocationUpdates(provider,5000,50,locationListener);
        return true;
    }



}