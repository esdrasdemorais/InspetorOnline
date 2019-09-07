package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.ui.listeners.PermanenceListener;

public class InspectionDialogFragment extends DialogFragment {

    private FragmentActivity fragmentActivity;

    public InspectionDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    private void openPermanence() {
        new PermanenceListener(this.fragmentActivity).onClick(null);
    }

    private void openDialog(String inspectionType) {
        switch(inspectionType) {
            case "Permanência":
                openPermanence();
                break;
            /*case "Orientação":
                openGuidance();
                break;
            case "Infração":
                openViolation();
                break;
            case "Ocorrência":
                openOccurrence();
                break;
            case "Interferência/Desvio":
                openRouteDiversion();
                break;
            case "Fumaça":
                openSmoke();
                break;
            case "Atrasos":
                openDelay();
                break;
            case "Perdas":
                openLoss();
                break;
            case "Falta/Dispensa Fiscal":
                openSupervisor();
                break;
            case "Fiscalização SPTrans":
                openSpTransSupervision();
                break;
            case "Limpeza":
                openCleaning();
                break;
            case "Buraco":
                openHole();
                break;*/
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.inspection_type)
            .setItems(R.array.inspection_type,
                new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which) {
                    ListView lw = ((AlertDialog)dialog).getListView();
//                    Object checkedItem = lw.getAdapter().getItem(
//                        lw.getCheckedItemPosition()
//                    );
                    openDialog(lw.getAdapter().getItem(which).toString());
                }
            });
        return builder.create();
    }
}