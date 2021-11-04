package com.example.ptvimproved24.datastructures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptvimproved24.R;

import java.util.ArrayList;

public class SavedRouteListAdapter extends ArrayAdapter<SavedRoute> {
    private Context mContext;
    int mResource;
    public SavedRouteListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SavedRoute> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String routename = getItem(position).getSavedRouteGtfsId();
        int routeid = getItem(position).getSavedRouteid();
        String routedirection = getItem(position).getSavedRouteName();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvRoutename = (TextView) convertView.findViewById(R.id.savedroute_name);
        TextView tvRoutedirection = (TextView) convertView.findViewById(R.id.savedroute_direction);

        tvRoutename.setText(routename);
        tvRoutedirection.setText(routedirection);

        return convertView;
    }
}
