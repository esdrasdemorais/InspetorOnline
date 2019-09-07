package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.model.Department;
import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.esdrasmorais.inspetoronline.data.model.Permanence;
import com.esdrasmorais.inspetoronline.data.repository.PermanenceRepository;
import com.google.android.material.textfield.TextInputLayout;

public class PermanenceDialogFragment extends AppCompatDialogFragment {
    private FragmentActivity fragmentActivity;

    private TextInputLayout inputLayoutDepartment;
    private TextInputLayout inputLayoutNote;
    private AutoCompleteTextView permanenceDepartment;
    private EditText editTextNote;
    private View view;
    private Inspection inspection;
    private Permanence permanence;
    private PermanenceRepository permanenceRepository;

    public PermanenceDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    private Boolean save() {
        return true;
    }

    private void setDepartmentAdapter() {
        Integer[] departments = new Integer[] {
            1, 2, 3, 4, 5, 6, 7
        };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
            this.view.getContext(),
            R.layout.dropdown_department_menu_popup_item,
            departments
        );
        permanenceDepartment.setAdapter(adapter);
//        permanenceDepartment.setOnItemClickListener(
//            new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(
//                    AdapterView<?> parent, View view, int position, long id
//                ) {
//                    Department selected = (Department)
//                        parent.getAdapter().getItem(position);
//                    permanence.setDepartment(selected);
//                }
//            }
//        );
    }

    private void initializeFields() {
        this.inputLayoutDepartment =
            view.findViewById(R.id.input_layout_permanence_department);
        this.inputLayoutNote = view.findViewById(R.id.input_layout_note);
        this.permanenceDepartment = view.findViewById(R.id.permanence_department);
        this.editTextNote = view.findViewById(R.id.edit_text_note);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.view = inflater.inflate(R.layout.permanence_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
            .setTitle("Permanencia")
            // Add action buttons
            .setPositiveButton(R.string.text_save,
                new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    save();
                }
            })
            .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    PermanenceDialogFragment.this.getDialog().cancel();
                }
            });

        initializeFields();

        setDepartmentAdapter();

        return builder.create();
    }
}