package com.esdrasmorais.inspetoronline.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends ArrayAdapter<Vehicle> {
    public VehicleAdapter(Context context, List<Vehicle> vehicles) {
        super(context, 0, vehicles);
    }

    public View getView(Integer position, View convertView, ViewGroup parent) {
        Vehicle vehicle = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.dropdown_prefix_menu_popup_item, parent, false
            );
        }
        TextView tvPrefix = (TextView)
            convertView.findViewById(R.id.tvPrefix);
        tvPrefix.setText(String.format(
            "%s - %s", vehicle.getPrefix(), vehicle.getLocalizatedAt()
        ));
        return convertView;
    }
}