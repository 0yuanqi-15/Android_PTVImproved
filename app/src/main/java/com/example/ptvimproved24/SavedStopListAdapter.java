package com.example.ptvimproved24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class SavedStopListAdapter extends ArrayAdapter<SavedStop> {
    private Context mContext;
    int mResource;

    private Time time;

    public SavedStopListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SavedStop> objects) {
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
//        if (gap <= 1) {
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
//        return super.getView(position, convertView, parent);
        String stopname = getItem(position).getStopname();
        String stopsuburb = getItem(position).getSuburb();
        ArrayList<String> routes = getItem(position).getRoutes();
        ArrayList<String> times = getItem(position).getTimes();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.savedstop_name);
        TextView tvSuburb = (TextView) convertView.findViewById(R.id.savedstop_suburb);
        TextView tvroute1 = (TextView) convertView.findViewById(R.id.savedstop_route1);
        TextView tvroute2 = (TextView) convertView.findViewById(R.id.savedstop_route2);
        TextView tvroute3 = (TextView) convertView.findViewById(R.id.savedstop_route3);
        TextView tvtime1 = (TextView) convertView.findViewById(R.id.savedstop_time1);
        TextView tvtime2 = (TextView) convertView.findViewById(R.id.savedstop_time2);
        TextView tvtime3 = (TextView) convertView.findViewById(R.id.savedstop_time3);

        tvName.setText(stopname);
        tvSuburb.setText(stopsuburb);

        if(routes == null || times == null){
            tvroute1.setText("");
            tvtime1.setText("");
            tvroute2.setText("");
            tvtime2.setText("");
            tvroute3.setText("");
            tvtime3.setText("");
            return convertView;
        }

        if (routes.size()>=1 && times.size()>=1){
            tvroute1.setText(routes.get(0));
            tvtime1.setText(time.gapInString(times.get(0)));
            tvroute2.setText("");
            tvtime2.setText("");
            tvroute3.setText("");
            tvtime3.setText("");
        }

        if (routes.size()>=2 && times.size()>=2){
            tvroute2.setText(routes.get(1));
            tvtime2.setText(time.gapInString(times.get(1)));
            tvroute3.setText("");
            tvtime3.setText("");
        }

        if (routes.size()>=3 && times.size()>=3){
            tvroute3.setText(routes.get(2));
            tvtime3.setText(time.gapInString(times.get(2)));
        }

        return convertView;
    }

}
