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

import com.example.ptvimproved24.datastructures.Route;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Locale;

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

        stopTime = gapInString(stopTime);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView direction = (TextView) convertView.findViewById(R.id.stop_direction);
        TextView route = (TextView) convertView.findViewById(R.id.stop_route);
        TextView time = (TextView) convertView.findViewById(R.id.stop_time);
        ImageView image = (ImageView) convertView.findViewById(R.id.stop_time_image);

        if (routeType == 0) {
            route.setBackgroundColor(Color.rgb(0, 114, 206));
            image.setBackgroundColor(Color.rgb(0, 114, 206));
        }
        else if (routeType == 1) {
            route.setBackgroundColor(Color.rgb(120, 190, 32));
            image.setBackgroundColor(Color.rgb(120, 190, 32));
        }
        else if (routeType == 2 || routeType == 4) {
            route.setBackgroundColor(Color.rgb(255, 130, 0));
            image.setBackgroundColor(Color.rgb(255, 130, 0));
        }
        else if (routeType == 3) {
            route.setBackgroundColor(Color.rgb(127, 13, 130));
            image.setBackgroundColor(Color.rgb(127, 13, 130));
        }

        direction.setText("To: " + stopDirection);
        route.setText(stopRoute);
        time.setText(stopTime);

        return convertView;
    }

    private long timeGap(String timeStr) {
        ZonedDateTime a = Instant.now().atZone(ZoneId.of("Australia/Melbourne"));
        TemporalAccessor time = DateTimeFormatter
                .ofLocalizedDateTime (FormatStyle.SHORT)
                .withLocale (Locale.UK)
                .withZone(ZoneId.of("Australia/Melbourne"))
                .parse(timeStr);
        ZonedDateTime b = ZonedDateTime.from(time);
        long diffInMinutes = ChronoUnit.MINUTES.between(a, b);
        return diffInMinutes;
    }

    private String gapInString(String timeStr) {
        long gap = timeGap(timeStr);
        String result = "";
        if (gap > 60) {
            result = gap/60 + "h";
        } else {
            result = gap + "m";
        }
        return result;
    }

}
