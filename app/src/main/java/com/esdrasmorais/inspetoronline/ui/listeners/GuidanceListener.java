package com.esdrasmorais.inspetoronline.ui.listeners;

import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.ui.GuidanceDialogFragment;

public class GuidanceListener implements View.OnClickListener {

    private FragmentActivity fragmentActivity;

    public GuidanceListener(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onClick(View v) {
        DialogFragment guidanceDialogFragment =
            new GuidanceDialogFragment(fragmentActivity);

        guidanceDialogFragment.show(
            fragmentActivity.getSupportFragmentManager(),
            "GuidanceDialogFragment"
        );
    }
}