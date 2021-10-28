package com.example.ptvimproved24;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StopDetailAdapter extends ArrayAdapter<Route> {

    private Context mContext;
    int mResource;
    public StopDetailAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Route> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String stopRoute = getItem(position).getRoute_gtfs_id();
        String stopTime = getItem(position).getScheduleDepart();
        String stopDirection = getItem(position).getDestination_name();

        int routeType = getItem(position).getRoute_type();



        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView direction = (TextView) convertView.findViewById(R.id.stop_direction);
        TextView route = (TextView) convertView.findViewById(R.id.stop_route);
        TextView time = (TextView) convertView.findViewById(R.id.stop_time);
        ImageView image = (ImageView) convertView.findViewById(R.id.stop_time_image);

        if (routeType == 1) {
            route.setBackgroundColor(Color.YELLOW);
            image.setBackgroundColor(Color.YELLOW);
        }
        else if (routeType == 2) {
            route.setBackgroundColor(Color.GREEN);
            image.setBackgroundColor(Color.GREEN);
        }
        else if (routeType == 3) {
            route.setBackgroundColor(Color.RED);
            image.setBackgroundColor(Color.RED);
        }
        else if (routeType == 4) {
            route.setBackgroundColor(Color.BLUE);
            image.setBackgroundColor(Color.BLUE);
        }

        direction.setText("To: " + stopDirection);
        route.setText(stopRoute);
        time.setText(stopTime);

        return convertView;
    }

}
