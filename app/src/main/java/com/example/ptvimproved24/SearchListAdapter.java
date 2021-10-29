package com.example.ptvimproved24;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ptvimproved24.datastructures.Search;
import com.example.ptvimproved24.datastructures.SearchResults;

import java.util.ArrayList;

public class SearchListAdapter extends ArrayAdapter<SearchResults> {
    private Context mContext;
    int mResource;
    public SearchListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SearchResults> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView search_service=convertView.findViewById(R.id.search_service);
        TextView search_details=convertView.findViewById(R.id.search_details);
        if (getItem(position).getTarget_type() == 0){
            // Stops
            if (getItem(position).getRoute_type() == 0){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_flag_24_metro,0,0,0);
            } else if (getItem(position).getRoute_type() == 1){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_flag_24_tram,0,0,0);
            } else if (getItem(position).getRoute_type() == 2 || getItem(position).getRoute_type() == 4){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_flag_24_bus,0,0,0);
            } else if(getItem(position).getRoute_type() == 3){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_flag_24_vline,0,0,0);
            }
            search_service.setText("");
            search_details.setText(getItem(position).getTarget_name());
        } else if (getItem(position).getTarget_type() == 1){
            // Routes
            if (getItem(position).getRoute_type() == 0){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_train_24_color,0,0,0);
            } else if (getItem(position).getRoute_type() == 1){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_tram_24_color,0,0,0);
            } else if (getItem(position).getRoute_type() == 2 || getItem(position).getRoute_type() == 4){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_directions_bus_24_color,0,0,0);
            } else if(getItem(position).getRoute_type() == 3){
                search_service.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_directions_transit_24_color,0,0,0);
            }
            search_service.setText(getItem(position).getTarget_name().replaceAll("^(0+)", ""));
            search_details.setText(getItem(position).getNote());
        }


        return convertView;
    }
}
