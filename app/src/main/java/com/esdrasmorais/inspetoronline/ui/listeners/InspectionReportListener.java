package com.esdrasmorais.inspetoronline.ui.listeners;

import android.content.Intent;
import android.view.View;

import com.esdrasmorais.inspetoronline.ui.inspection.InspectionReportActivity;

public class InspectionReportListener implements View.OnClickListener {
    public InspectionReportListener(View v) {
        Intent intent = new Intent(v.getContext(), InspectionReportActivity.class);
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), InspectionReportActivity.class);
        v.getContext().startActivity(intent);
    }
}

