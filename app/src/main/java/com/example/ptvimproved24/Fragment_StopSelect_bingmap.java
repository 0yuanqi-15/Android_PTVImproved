package com.example.ptvimproved24;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ptvimproved24.datastructures.Stop;
import com.example.ptvimproved24.datastructures.StopHttpRequestHandler;
import com.microsoft.maps.Geopoint;
import com.microsoft.maps.MapAnimationKind;
import com.microsoft.maps.MapCamera;
import com.microsoft.maps.MapCameraChangedEventArgs;
import com.microsoft.maps.MapElementLayer;
import com.microsoft.maps.MapElementTappedEventArgs;
import com.microsoft.maps.MapFlyout;
import com.microsoft.maps.MapIcon;
import com.microsoft.maps.MapScene;
import com.microsoft.maps.MapView;
import com.microsoft.maps.MapRenderMode;
import com.microsoft.maps.OnMapCameraChangedListener;
import com.microsoft.maps.OnMapElementTappedListener;

public class Fragment_StopSelect_bingmap extends Fragment{
    private MapView mMapView;
    private MapCamera mapCamera;
    private MapElementLayer mPinLayer = new MapElementLayer();

    private Float cameraLat=-37.818078f;
    private Float cameraLng=144.96681f;
    private Float zoomLevel=14.5f;
    private LocationManager locationManager;
    StopHttpRequestHandler stopHttpRequestHandler = new StopHttpRequestHandler(this);

    private int lastSelectedStopId=-1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        View v = inflater.inflate(R.layout.fragment_mapselect_bing, container, false);
        getGeoLocation();

        mMapView = new MapView(getContext(), MapRenderMode.RASTER);  // or use MapRenderMode.RASTER for 2D map
        mMapView.setCredentialsKey(BuildConfig.CREDENTIALS_KEY);
        ((FrameLayout) v.findViewById(R.id.map_view_stopselect)).addView(mMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getLayers().add(mPinLayer);
        mMapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(cameraLat,cameraLng),zoomLevel), MapAnimationKind.DEFAULT);

        mPinLayer.addOnMapElementTappedListener(new OnMapElementTappedListener() {
            @Override
            public boolean onMapElementTapped(MapElementTappedEventArgs e) {
                System.out.println("PinElement:"+e.mapElements);
                if(e.mapElements.size()>0){
                    MapIcon pushpin = (MapIcon) e.mapElements.get(0);
                    String[] stopdetails = pushpin.getFlyout().getDescription().split("\\:");
                    int stopid = Integer.parseInt(stopdetails[stopdetails.length-1]);
                    System.out.println("stopid:"+stopid);
                    if(stopid == lastSelectedStopId){
                        Intent i = new Intent(getContext(),stops.class);
                        Stop s = (Stop) pushpin.getTag();
                        i.putExtra("index",s.getStop_id());
                        i.putExtra("type",s.getRouteType());
                        i.putExtra("name",s.getStop_name());
                        i.putExtra("suburb",s.getStop_suburb());
                        startActivity(i);
                    }
                    System.out.println("Last click stopid:"+lastSelectedStopId);
                    lastSelectedStopId = stopid;
                    Toast.makeText(getContext(),"Click again to see next departures of "+pushpin.getFlyout().getTitle(),Toast.LENGTH_SHORT);
                }
                return false;
            }
        });

        mMapView.addOnMapCameraChangedListener(new OnMapCameraChangedListener() {
            @Override
            public boolean onMapCameraChanged(MapCameraChangedEventArgs mapCameraChangedEventArgs) {
                cameraLat = (float) mapCameraChangedEventArgs.camera.getLocation().getPosition().getLatitude();
                cameraLng = (float) mapCameraChangedEventArgs.camera.getLocation().getPosition().getLongitude();
                float camAltitude = (float) mapCameraChangedEventArgs.camera.getLocation().getPosition().getAltitude();
                System.out.println("Camera:"+cameraLat+","+cameraLng+"\t"+camAltitude);
                try {
                    stopHttpRequestHandler.getStopsToBingmap(cameraLat, cameraLng, mPinLayer, mMapView, camAltitude);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (camAltitude > 8000) {
                    Toast.makeText(getContext(), "Zoom in to see more stops...", Toast.LENGTH_SHORT);
                }
//                Camera Changed, change loading stops, get camera range, and set the
                return false;
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getGeoLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    cameraLat = (float) location.getLatitude();
                    cameraLng = (float) location.getLongitude();
                    mMapView.setScene(MapScene.createFromLocationAndZoomLevel(new Geopoint(cameraLat,cameraLng),zoomLevel), MapAnimationKind.DEFAULT);
                }
            });
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("locationfragStopselect:" + location);
            if(location!=null) {
                cameraLat = (float) location.getLatitude();
                cameraLng = (float) location.getLongitude();
            }
        } catch (SecurityException e) {
            Toast.makeText(getActivity(),
                    "Default geolocation is used, please retry after enable location service.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
