package com.esdrasmorais.inspetoronline.ui.listeners;

import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.ui.PermanenceDialogFragment;
import com.esdrasmorais.inspetoronline.ui.ViolationDialogFragment;

public class ViolationListener implements View.OnClickListener {
    private FragmentActivity fragmentActivity;

    public ViolationListener(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onClick(View v) {
        DialogFragment violationDialogFragment =
            new ViolationDialogFragment(fragmentActivity);

        violationDialogFragment.show(
            fragmentActivity.getSupportFragmentManager(),
            "ViolationDialogFragment"
        );
    }
}