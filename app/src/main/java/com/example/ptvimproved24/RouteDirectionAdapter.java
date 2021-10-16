package com.example.ptvimproved24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RouteDirectionAdapter extends ArrayAdapter<Direction> {
    private Context mContext;
    int mResource;
    public RouteDirectionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Direction> objects) {
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
        if (gap > 60) {
            result = gap/60 + "h";
        } else {
            result = gap + "m";
        }
        return result;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String direction_name = getItem(position).getDirection_name();

        String nearestStopName = "--";
        String time1 = "--:--";
        String time2 = "";
        String time3 = "";

        String timeDiff1 = "";
        String timeDiff2 = "";
        String timeDiff3 = "";

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView serviceTo = (TextView) convertView.findViewById(R.id.rdetails_terminus);
        serviceTo.setText(direction_name);

        if (getItem(position).getNearestStop() != null) {
            nearestStopName = getItem(position).getNearestStop().getStopname();
        }

        ArrayList<Departure> meaningfulDepartures = new ArrayList<>();
        if (getItem(position).getDeparturesForNearestStop().size() > 0) {
            for (Departure d : getItem(position).getDeparturesForNearestStop()) {
                long diff = timeGap(d.getScheduled_departure_utc());
                if (diff >= 0) {
                    meaningfulDepartures.add(d);
                }
            }
        }

        if (meaningfulDepartures.size() > 0) {
            time1 = meaningfulDepartures.get(0).getScheduled_departure_utc();
            timeDiff1 = gapInString(time1);
        }

        if (meaningfulDepartures.size() > 1) {
            time2 = meaningfulDepartures.get(1).getScheduled_departure_utc();
            timeDiff2 = gapInString(time2);
        }

        if (meaningfulDepartures.size() > 2) {
            time3 = meaningfulDepartures.get(2).getScheduled_departure_utc();
            timeDiff3 = gapInString(time3);
        }

        TextView nearStopName = (TextView) convertView.findViewById(R.id.rdetails_stopname);
        nearStopName.setText(nearestStopName);

        TextView time1Text = (TextView) convertView.findViewById(R.id.rdetails_time1);
        time1Text.setText(time1);

        TextView time2Text = (TextView) convertView.findViewById(R.id.rdetails_time2);
        time2Text.setText(time2);

        TextView time3Text = (TextView) convertView.findViewById(R.id.rdetails_time3);
        time3Text.setText(time3);

        TextView timeDiff1Text = (TextView) convertView.findViewById(R.id.rdetails_countdown1);
        timeDiff1Text.setText(timeDiff1);
        TextView timeDiff2Text = (TextView) convertView.findViewById(R.id.rdetails_countdown2);
        timeDiff2Text.setText(timeDiff2);
        TextView timeDiff3Text = (TextView) convertView.findViewById(R.id.rdetails_countdown3);
        timeDiff3Text.setText(timeDiff3);

        return convertView;
    }

}
