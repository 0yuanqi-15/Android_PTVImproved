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

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView serviceTo = (TextView) convertView.findViewById(R.id.rdetails_terminus);
        serviceTo.setText(direction_name);


        return convertView;
    }

}
