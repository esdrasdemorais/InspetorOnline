package com.esdrasmorais.inspetoronline.ui.listeners;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.ui.InspectionDialogFragment;

public class InspectionListener implements View.OnClickListener {
    private FragmentActivity fragmentActivity;

    public InspectionListener(FragmentActivity fragmentActivity) {
//        Intent intent = new Intent(v.getContext(), .class);
//        v.getContext().startActivity(intent);

        /*DialogFragment inspectionDialogFragment =
            new InspectionDialogFragment();

        inspectionDialogFragment.show(
            v.getSupportFragmentManager(),
            "InspectionDialogFragment"
        );*/
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(v.getContext(), .class);
//        v.getContext().startActivity(intent);

        DialogFragment inspectionDialogFragment =
            new InspectionDialogFragment(fragmentActivity);

        inspectionDialogFragment.show(
            fragmentActivity.getSupportFragmentManager(),
            "InspectionDialogFragment"
        );
    }
}