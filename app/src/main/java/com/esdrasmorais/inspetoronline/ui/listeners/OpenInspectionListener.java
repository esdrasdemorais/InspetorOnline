package com.esdrasmorais.inspetoronline.ui.listeners;

import android.content.Intent;
import android.view.View;

import com.esdrasmorais.inspetoronline.ui.InspectionActivity;

public class OpenInspectionListener implements View.OnClickListener {

    public OpenInspectionListener(View v) {
        Intent intent = new Intent(v.getContext(), InspectionActivity.class);
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), InspectionActivity.class);
        v.getContext().startActivity(intent);
    }
}