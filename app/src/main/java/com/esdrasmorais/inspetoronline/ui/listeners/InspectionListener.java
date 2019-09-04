package com.esdrasmorais.inspetoronline.ui.listeners;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.ui.InspectionDialogFragment;

public class InspectionListener
    extends FragmentActivity
    implements View.OnClickListener
{
    public InspectionListener(View v) {
//        Intent intent = new Intent(v.getContext(), .class);
//        v.getContext().startActivity(intent);

        DialogFragment inspectionDialogFragment =
            new InspectionDialogFragment();

        inspectionDialogFragment.show(
            getSupportFragmentManager(),
            "InspectionDialogFragment"
        );
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(v.getContext(), .class);
//        v.getContext().startActivity(intent);

        DialogFragment inspectionDialogFragment =
            new InspectionDialogFragment();

        inspectionDialogFragment.show(
            getSupportFragmentManager(),
            "InspectionDialogFragment"
        );
    }
}