package com.example.ptvimproved24;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class stopsListAdapter extends ArrayAdapter<stops> {
    private Context mContext;
    int mResource;
    public stopsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<stops> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//      // Ref as SavedStopListAdapter to continue
//    }
}
