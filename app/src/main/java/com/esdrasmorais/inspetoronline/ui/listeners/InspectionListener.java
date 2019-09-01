package com.esdrasmorais.inspetoronline.ui.listeners;

import android.content.Intent;
import android.view.View;

public class InspectionListener implements View.OnClickListener {
    public InspectionListener(View v) {
        Intent intent = new Intent(v.getContext(), .class);
        v.getContext().startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), .class);
        v.getContext().startActivity(intent);
    }
}