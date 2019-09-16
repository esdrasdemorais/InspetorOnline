package com.esdrasmorais.inspetoronline.ui.listeners;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.ui.adapters.InspectionTypeImageAdapter;
import com.esdrasmorais.inspetoronline.ui.models.InspectionTypeImageModel;

import java.util.ArrayList;

public class InspectionListener implements View.OnClickListener
{
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

    private void openPermanence() {
        new PermanenceListener(this.fragmentActivity).onClick(null);
    }

    private void openGuidance() {
        new GuidanceListener(this.fragmentActivity).onClick(null);
    }

    private void openViolation() { new ViolationListener(this.fragmentActivity).onClick(null); }

    private void openOccurrence() {}

    private void openRouteDiversion() {}

    private void openSmoke() {}

    private void openDelay() {}

    private void openLoss() {}

    private void openSupervisor() {}

    private void openSpTransSupervision() {}

    private void openCleaning() {}

    private void openHole() {}

    private void openDialog(String inspectionType) {
        switch(inspectionType) {
            case "Permanência":
                openPermanence();
                break;
            case "Orientação":
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
                break;
        }
    }

    private ArrayList<InspectionTypeImageModel> populateList() {
        ArrayList<InspectionTypeImageModel> list = new ArrayList<>();
        Integer i = 0;

        for (String inspectionType :
            view.getResources().getStringArray(R.array.inspection_type))
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

    public void showDialog(View view) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.inspection_dialog);
        dialog.setTitle(R.string.inspection_type);

        inspectionTypeImageModels = populateList();
        inspectionTypeImageAdapter = new InspectionTypeImageAdapter(
            view.getContext(),
            R.layout.inspection_type_list_item,
            inspectionTypeImageModels
        );

        inspectionType = dialog.findViewById(R.id.inspection_type_list);
//        ArrayAdapter arrayAdapter =
//            new ArrayAdapter(this,R.layout.inspection_type_list_item,
//                R.id.tv,
//                    myImageNameList);
        inspectionType.setAdapter(inspectionTypeImageAdapter);
        inspectionType.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent, View view, int position, long id
                ) {
                openDialog(
                    inspectionTypeImageModels.get(position).getName()
                );
                }
            }
        );

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        this.view = v;
//        Intent intent = new Intent(v.getContext(), .class);
//        v.getContext().startActivity(intent);

//        FragmentManager fragmentManager =
//            fragmentActivity.getSupportFragmentManager();
//        InspectionDialogFragment inspectionDialogFragment =
//            new InspectionDialogFragment(fragmentActivity);

//        inspectionDialogFragment.show(
//            fragmentManager,
//            "InspectionDialogFragment"
//        );
        showDialog(v);
    }
}