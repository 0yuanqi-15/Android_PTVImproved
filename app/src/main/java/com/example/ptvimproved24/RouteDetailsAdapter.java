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

import java.util.ArrayList;

public class RouteDetailsAdapter extends ArrayAdapter<Stop> {
    private Context mContext;
    int mResource;
    public RouteDetailsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Stop> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }


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
        serviceTime.setText(stop_time.substring(11,16));


        return convertView;
    }

}
