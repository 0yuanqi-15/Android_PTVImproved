package com.example.ptvimproved24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptvimproved24.datastructures.Stop;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Locale;

public class NearStopListAdapter extends ArrayAdapter<Stop> {

    private Context mContext;
    int mResource;
    public NearStopListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Stop> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
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
        String appendStr = "";
        if (gap <= 1) {
            appendStr = " < 1 min";
            result = appendStr;
        } else if (gap > 60) {
            long hours = gap / 60;
            if (hours < 24) {
                appendStr = hours > 1 ? " hours" : " < 1 hour";
                result = hours + appendStr;
            } else {
                result = " > 1 day";
            }
        } else {
            result = gap + " mins";
        }
        return result;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        String stopname = getItem(position).getStop_name();
        String stopsuburb = getItem(position).getStop_suburb();
        int stopdistance = getItem(position).getStop_distance();
        ArrayList<String> routes = getItem(position).getRoutes();
        ArrayList<String> times = getItem(position).getTimes();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.nearstop_name);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.nearstop_distance);
        TextView tvSuburb = (TextView) convertView.findViewById(R.id.nearstop_suburb);
        TextView tvroute1 = (TextView) convertView.findViewById(R.id.nearstop_route1);
        TextView tvroute2 = (TextView) convertView.findViewById(R.id.nearstop_route2);
        TextView tvroute3 = (TextView) convertView.findViewById(R.id.nearstop_route3);
        TextView tvtime1 = (TextView) convertView.findViewById(R.id.nearstop_time1);
        TextView tvtime2 = (TextView) convertView.findViewById(R.id.nearstop_time2);
        TextView tvtime3 = (TextView) convertView.findViewById(R.id.nearstop_time3);

        tvName.setText(stopname);
        tvSuburb.setText(stopsuburb);
        tvDistance.setText(stopdistance+"m away");

        if (routes.size()>=1){
            tvroute1.setText(routes.get(0));
            String time = routes.get(0).equals("--") ? "--:--" : gapInString(times.get(0));
            tvtime1.setText(time);
        }

        if (routes.size()>=2){
            tvroute2.setText(routes.get(1));
            String time = routes.get(1).equals("--") ? "--:--" : gapInString(times.get(1));
            tvtime2.setText(time);
        }

        if (routes.size()>=3){
            tvroute3.setText(routes.get(2));
            String time = routes.get(2).equals("--") ? "--:--" : gapInString(times.get(2));
            tvtime3.setText(time);
        }

        return convertView;
    }
}
