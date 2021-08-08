package com.example.ptvimproved24;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DisruptionsListAdapter extends ArrayAdapter<Disruptions> {

    private Context mContext;
    int mResource;

    public DisruptionsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Disruptions> objects) {
        super(context, resource, objects);
        mContext =context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        String disruption_title = getItem(position).getTitle();
        String disruption_time = getItem(position).getDatetime();
        int disruption_id = getItem(position).getDisruptionid();
        int disruption_type = getItem(position).getType();


        Disruptions disruptions = new Disruptions(disruption_id,disruption_title,disruption_time,disruption_type);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitle = convertView.findViewById(R.id.dis_title);
        TextView tvTime = convertView.findViewById(R.id.dis_time);
        TextView tvcolor1 = convertView.findViewById(R.id.dis_color1);
        TextView tvcolor2 = convertView.findViewById(R.id.dis_color2);

        tvTitle.setText(disruption_title);
        tvTime.setText(disruption_time);
        if (disruption_type == 0){ // Metro
            tvcolor1.setBackgroundColor(Color.parseColor("#3D9CE3"));
            tvcolor2.setBackgroundColor(Color.parseColor("#3D9CE3");
        }
        if (disruption_type == 1){ // Tram
            tvcolor1.setBackgroundColor(Color.parseColor("#64B46B"));
            tvcolor2.setBackgroundColor(Color.parseColor("#64B46B"));
        }
        if (disruption_type == 2){ // Bus
            tvcolor1.setBackgroundColor(Color.parseColor("#E88A20"));
            tvcolor2.setBackgroundColor(Color.parseColor("#E88A20"));
        }
        if (disruption_type == 3){ // Vline
            tvcolor1.setBackgroundColor(Color.parseColor("#B464A9"));
            tvcolor2.setBackgroundColor(Color.parseColor("#B464A9"));
        }

        return convertView;
    }
}
