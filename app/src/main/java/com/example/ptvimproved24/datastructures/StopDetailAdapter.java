package com.example.ptvimproved24.datastructures;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.ptvimproved24.R;
import com.example.ptvimproved24.datastructures.Route;
import com.example.ptvimproved24.datastructures.Time;

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

        stopTime = Time.getInstance().gapInString(stopTime);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView direction = (TextView) convertView.findViewById(R.id.stop_direction);
        TextView route = (TextView) convertView.findViewById(R.id.stop_route);
        TextView time = (TextView) convertView.findViewById(R.id.stop_time);
        ImageView image = (ImageView) convertView.findViewById(R.id.stop_time_image);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.outline_watch_later_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);


        if (routeType == 0) {
            route.setBackgroundResource(R.drawable.hzt_ridiotext_blue);
            DrawableCompat.setTint(wrappedDrawable, Color.rgb(0, 114, 206));
        }
        else if (routeType == 1) {
            route.setBackgroundResource(R.drawable.hzt_ridiotext_green);
            DrawableCompat.setTint(wrappedDrawable, Color.rgb(120, 190, 32));
        }
        else if (routeType == 2 || routeType == 4) {
            route.setBackgroundResource(R.drawable.hzt_ridiotext_orange);
            DrawableCompat.setTint(wrappedDrawable, Color.rgb(255, 130, 0));
        }
        else if (routeType == 3) {
            route.setBackgroundResource(R.drawable.hzt_ridiotext_purple);
            DrawableCompat.setTint(wrappedDrawable, Color.rgb(127, 13, 130));
        }

        direction.setText("To: " + stopDirection);
        route.setText(stopRoute);
        time.setText(stopTime);

        return convertView;
    }

}
