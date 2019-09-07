package com.esdrasmorais.inspetoronline.ui.listeners;

import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.ui.PermanenceDialogFragment;

public class PermanenceListener implements View.OnClickListener {

    private FragmentActivity fragmentActivity;

    public PermanenceListener(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onClick(View v) {
        DialogFragment permanenceDialogFragment =
            new PermanenceDialogFragment(fragmentActivity);

        permanenceDialogFragment.show(
            fragmentActivity.getSupportFragmentManager(),
            "PermanenceDialogFragment"
        );
    }
}