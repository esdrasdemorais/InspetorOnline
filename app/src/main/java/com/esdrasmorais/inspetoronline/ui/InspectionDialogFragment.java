package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.ui.adapters.InspectionTypeImageAdapter;
import com.esdrasmorais.inspetoronline.ui.listeners.GuidanceListener;
import com.esdrasmorais.inspetoronline.ui.listeners.PermanenceListener;
import com.esdrasmorais.inspetoronline.ui.models.InspectionTypeImageModel;

import java.util.ArrayList;

public class InspectionDialogFragment extends AppCompatDialogFragment {

    private FragmentActivity fragmentActivity;
    private InspectionTypeImageAdapter inspectionTypeImageAdapter;
    private ArrayList<InspectionTypeImageModel> inspectionTypeImageModels = new ArrayList<>();
    private View view;
    private ListView inspectionType;

    private Integer[] imageList = new Integer[] {
        R.drawable.ic_permanence, R.drawable.ic_guidance,
        R.drawable.ic_violation, R.drawable.ic_occurrence,
        R.drawable.ic_route_diversion, R.drawable.ic_smoke,
        R.drawable.ic_delay, R.drawable.ic_loss,
        R.drawable.ic_supervisor, R.drawable.ic_sptrans_supervision,
        R.drawable.ic_cleaning, R.drawable.ic_hole
    };

    public InspectionDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    private void openPermanence() {
        new PermanenceListener(this.fragmentActivity).onClick(null);
    }

    private void openGuidance() {
        new GuidanceListener(this.fragmentActivity).onClick(null);
    }

    private void openDialog(String inspectionType) {
        switch(inspectionType) {
            case "Permanência":
                openPermanence();
                break;
            case "Orientação":
                openGuidance();
                break;
            /*case "Infração":
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

    private ArrayList<InspectionTypeImageModel> populateList() {
        ArrayList<InspectionTypeImageModel> list = new ArrayList<>();
        Integer i = 0;

        for (String inspectionType :
            getResources().getStringArray(R.array.inspection_type))
        {
            InspectionTypeImageModel inspectionTypeImageModel =
                new InspectionTypeImageModel();

            inspectionTypeImageModel.setName(inspectionType);
            inspectionTypeImageModel.setImageDrawable(imageList[i]);
            list.add(inspectionTypeImageModel);

            i++;
        }

        return list;
    }

    private void showDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.view = inflater.inflate(R.layout.inspection_dialog, null);

        builder.setView(view)
            .setTitle(R.string.inspection_type);

        inspectionTypeImageModels = populateList();
        inspectionTypeImageAdapter = new InspectionTypeImageAdapter(
            this.view.getContext(),
            R.layout.inspection_type_list_item,
            inspectionTypeImageModels
        );

        inspectionType = this.view.findViewById(R.id.inspection_type_list);
        inspectionType.setAdapter(inspectionTypeImageAdapter);
        inspectionType.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                    //openDialog(view.findViewById(R.id.textView).toString());
                }
            }
        );

        //builder.setTitle(R.string.inspection_type)
//            .setAdapter(inspectionTypeImageAdapter,
//                new DialogInterface.OnClickListener()
//            {
//                public void onClick(DialogInterface dialog, int which) {
//                    ListView lw = ((AlertDialog)dialog).getListView();
//                    Object checkedItem = lw.getAdapter().getItem(
//                        lw.getCheckedItemPosition()
//                    );
//                    openDialog(lw.getAdapter().getItem(which).toString());
//                }
//            });

        return builder.create();
    }
}