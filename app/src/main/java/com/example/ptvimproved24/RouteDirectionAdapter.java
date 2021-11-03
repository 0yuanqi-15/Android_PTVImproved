package com.example.ptvimproved24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptvimproved24.datastructures.Departure;
import com.example.ptvimproved24.datastructures.Time;

import java.util.ArrayList;

public class RouteDirectionAdapter extends ArrayAdapter<Direction> {
    private Context mContext;
    int mResource;

    public RouteDirectionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Direction> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String direction_name = getItem(position).getDirection_name();

        String nearestStopName = "";
        String time1 = "No departure";
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
            nearestStopName = getItem(position).getNearestStop().getStop_name();
        }

        ArrayList<Departure> meaningfulDepartures = new ArrayList<>();
        if (getItem(position).getDeparturesForNearestStop().size() > 0) {
            for (Departure d : getItem(position).getDeparturesForNearestStop()) {
                long diff = Time.getInstance().timeGap(d.getScheduled_departure_utc());
                if (diff >= 0) {
                    meaningfulDepartures.add(d);
                }
            }
        }

        if (meaningfulDepartures.size() > 0) {
            time1 = meaningfulDepartures.get(0).getScheduled_departure_utc();
            timeDiff1 = Time.getInstance().gapInString(time1);
        }

        if (meaningfulDepartures.size() > 1) {
            time2 = meaningfulDepartures.get(1).getScheduled_departure_utc();
            timeDiff2 = Time.getInstance().gapInString(time2);
        }

        if (meaningfulDepartures.size() > 2) {
            time3 = meaningfulDepartures.get(2).getScheduled_departure_utc();
            timeDiff3 = Time.getInstance().gapInString(time3);
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
