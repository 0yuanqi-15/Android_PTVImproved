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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NearStopListAdapter extends ArrayAdapter<NearStop> {

    private Context mContext;
    int mResource;
    public NearStopListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NearStop> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        String stopname = getItem(position).getStopname();
        String stopsuburb = getItem(position).getSuburb();
        int stopdistance = getItem(position).getDistance();
        ArrayList<String> routes = getItem(position).getRoutes();
        ArrayList<String> times = getItem(position).getTimes();

        NearStop nearStop = new NearStop(stopsuburb,stopname,stopdistance,routes,times);

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

        if (routes.size()>=1 && times.size()>=1){
            tvroute1.setText(routes.get(0));
            tvtime1.setText(times.get(0));
        }

        if (routes.size()>=2 && times.size()>=2){
            tvroute1.setText(routes.get(1));
            tvtime1.setText(times.get(1));
        }

        if (routes.size()>=3 && times.size()>=3){
            tvroute1.setText(routes.get(2));
            tvtime1.setText(times.get(2));
        }

        return convertView;
    }
}
