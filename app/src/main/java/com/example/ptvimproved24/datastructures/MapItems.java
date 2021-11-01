package com.example.ptvimproved24.datastructures;

import com.example.ptvimproved24.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapItems implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private BitmapDescriptor icon;

    public MapItems(double lat, double lng, String title, String snippet) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_emoji_flags_24);
    }

    public MapItems(double lat, double lng, String title, String snippet, int routeType){
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        switch (routeType){
            case 0:icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_train_24_color);break;
            case 1:icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_tram_24);break;
            case 2:icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_bus_filled_24);break;
            case 3:icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_train_24);break;
            case 4:icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_directions_bus_filled_24);break;
            default:icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_emoji_flags_24);
        }

    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }


}
