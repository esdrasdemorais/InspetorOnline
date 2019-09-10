package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.SecurityPreferences;
import com.esdrasmorais.inspetoronline.data.model.Department;
import com.esdrasmorais.inspetoronline.data.model.Permanence;
import com.esdrasmorais.inspetoronline.data.repository.PermanenceRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Date;

import static com.esdrasmorais.inspetoronline.ui.PermanenceDialogFragment.*;

public class PermanenceDialogFragment extends AppCompatDialogFragment
{
    private FragmentActivity fragmentActivity;

    private TextInputLayout inputLayoutDepartment;
    private TextInputLayout inputLayoutNote;
    private AutoCompleteTextView permanenceDepartment;
    private EditText editTextNote;
    private View view;
    private Permanence permanence;
    private PermanenceRepository permanenceRepository;
    private PermanenceDialogListener listener;
    private Button save;

    public PermanenceDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.permanence = new Permanence();
    }

    private Boolean validate() {
        Boolean isValid = true;
        if (permanenceDepartment.getText().toString().isEmpty()) {
            inputLayoutDepartment.setError(
                getString(R.string.error_permanence_department)
            );
            isValid = false;
        } else {
            inputLayoutDepartment.setErrorEnabled(false);
        }

        if (editTextNote.getText().toString().trim().length() >= 0 &&
            editTextNote.getText().toString().trim().length() < 5
        ) {
            inputLayoutNote.setError(getString(R.string.error_permanence_note));
            isValid = false;
        } else {
            inputLayoutNote.setErrorEnabled(false);
        }
        return isValid;
    }

    private Location getLocation() {
        SecurityPreferences securityPreferences = new SecurityPreferences(
            this.getContext()
        );
        Location location = new Gson().fromJson(
            securityPreferences.getStoredString("last_know_location"),
            Location.class
        );
        return location;
    }

    private Permanence setPermanence(Permanence permanence) {
        String department =
            this.permanenceDepartment.getText().toString();
        try {
            permanence.setDepartment(
                Department.of(Integer.parseInt(department))
            );
        } catch (Exception ex) {
            Log.e("PermanenceDialogFragmen", ex.getMessage());
        }

        permanence.setNote(editTextNote.getText().toString());
        permanence.setAddress(this.getLocation());
        permanence.setInitialDate(new Date());

        return permanence;
    }

    private Boolean savePermanence() {
        Boolean isSaved = false;
        permanence = setPermanence(permanence);
        permanenceRepository = new PermanenceRepository(Permanence.class);
        isSaved = permanenceRepository.set(permanence);
        return isSaved;
    }

    private void save(View view) {
        //Boolean isSaved = false;
        if (validate() && savePermanence()) {
            Snackbar.make(view, "Salvo com Sucesso.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            //PermanenceDialogFragment.this.getDialog().cancel();
            //listener.showPermanenceMessage(view,"Salvo com Sucesso.");
            //isSaved = true;
        }
        //return isSaved;
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
        this.save = view.findViewById(R.id.permanence_button_save);
    }

    private void setListener() {
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
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
            .setTitle("Permanencia");
            // Add action buttons
            /*.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    PermanenceDialogFragment.this.getDialog().cancel();
                }
            })
            .setPositiveButton(R.string.text_save,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        save();
                    }
                });*/

        initializeFields();
        setDepartmentAdapter();
        setListener();

        return builder.create();
    }

    public interface PermanenceDialogListener {
         public Boolean showPermanenceMessage(View view, String message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PermanenceDialogListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() +
                "must implement PermanenceDialogListener");
        }
    }
}