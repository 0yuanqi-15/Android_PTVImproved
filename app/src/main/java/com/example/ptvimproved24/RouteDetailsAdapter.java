package com.example.ptvimproved24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptvimproved24.datastructures.Pattern;
import com.example.ptvimproved24.datastructures.Stop;
import com.example.ptvimproved24.datastructures.Time;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Locale;

public class RouteDetailsAdapter extends ArrayAdapter<Stop> {
    private Context mContext;
    int mResource;

    private Time time;

    public RouteDetailsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Stop> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;

        time = Time.getInstance();
    }

//    private long timeGap(String timeStr) {
//        ZonedDateTime a = Instant.now().atZone(ZoneId.of("Australia/Melbourne"));
//        TemporalAccessor time = DateTimeFormatter
//                .ofLocalizedDateTime (FormatStyle.SHORT)
//                .withLocale (Locale.UK)
//                .withZone(ZoneId.of("Australia/Melbourne"))
//                .parse(timeStr);
//        ZonedDateTime b = ZonedDateTime.from(time);
//        long diffInMinutes = ChronoUnit.MINUTES.between(a, b);
//        return diffInMinutes;
//    }

//    private String gapInString(String timeStr) {
//        long gap = timeGap(timeStr);
//        String result = "";
//        String appendStr = "";
//        if (gap < 0) {
//            result = "Passed";
//        } else if (gap <= 1) {
//            appendStr = " < 1 min";
//            result = appendStr;
//        } else if (gap > 60) {
//            long hours = gap / 60;
//            if (hours < 24) {
//                appendStr = hours > 1 ? " hours" : " hour";
//                result = hours + appendStr;
//            } else {
//                result = " > 1 day";
//            }
//        } else {
//            result = gap + " mins";
//        }
//        return result;
//    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String stop_name = getItem(position).getStop_name();
        String stop_time = getItem(position).getStop_time();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView serviceName = (TextView) convertView.findViewById(R.id.stop_name);
        TextView serviceTime = (TextView) convertView.findViewById(R.id.stop_time);
        serviceName.setText(stop_name);
        serviceTime.setText(time.gapInString(stop_time));

        return convertView;
    }

}
