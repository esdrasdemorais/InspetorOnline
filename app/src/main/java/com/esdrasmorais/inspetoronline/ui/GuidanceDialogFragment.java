package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.BasicApp;
import com.esdrasmorais.inspetoronline.data.DataRepository;
import com.esdrasmorais.inspetoronline.data.GetVolleyResponse;
import com.esdrasmorais.inspetoronline.data.GoogleDirections;
import com.esdrasmorais.inspetoronline.data.SecurityPreferences;
import com.esdrasmorais.inspetoronline.data.SpTrans;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Direction;
import com.esdrasmorais.inspetoronline.data.model.Guidance;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.repository.GuidanceRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuidanceDialogFragment extends AppCompatDialogFragment {
    private FragmentActivity fragmentActivity;
    private GoogleDirections googleDirections;
    private SpTrans spTrans;
    private String cookie;
    private GetVolleyResponse getVolleyResponse;
    private List<Company> companies = new ArrayList<Company>();
    private Integer countCompanies = 1;
    private List<Line> lines;
    private TextInputLayout inputLayoutDirection;
    private TextInputLayout inputLayoutSubject;
    private TextInputLayout inputLayoutLines;
    private AutoCompleteTextView lineDropdown;
    private AutoCompleteTextView direction;
    private EditText editTextSubject;
    private View view;
    private Guidance guidance;
    private GuidanceRepository guidanceRepository;
//    private GuidanceDialogFragment.GuidanceDialogListener listener;
    private Button save;

    @NonNull
    private Application application;

    @NonNull
    private DataRepository dataReposity;

    public GuidanceDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.guidance = new Guidance();
        application = fragmentActivity.getApplication();
        dataReposity = ((BasicApp) application).getRepository();
        dataReposity.getCompanies();
    }

    private void setGoogleDirections() {
        this.googleDirections = new GoogleDirections(
            this.view.getContext(),
            this.getLocation()
        );
//        GetVolleyResponse getVolleyResponse =
//            new GetVolleyResponse(this.view.getContext());
        getVolleyResponse.getResponse(
            Request.Method.GET, googleDirections.getUrl(), null,
            new GetVolleyResponse(this.view.getContext()) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        googleDirections.setJson(
                                new Gson().fromJson(result, JsonObject.class)
                        );
                        setLinesFromGoogleDirections();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    private void setLinesFromGoogleDirections() {
        final JsonArray routes = googleDirections.getJson().get("routes").getAsJsonArray();

        if (routes == null || !routes.isJsonArray() || routes.size() <= 0 /*||
            routes.get(0).getAsJsonObject().getAsJsonArray("legs") == null*/
        )
            return;
        //throw new IllegalArgumentException("json is not an array");

        final JsonObject legsObject = routes.get(0).getAsJsonObject();
        final JsonArray legs = legsObject.getAsJsonArray("legs");

        final JsonObject stepsObject = legs.get(0).getAsJsonObject();
        final JsonArray steps = stepsObject.getAsJsonArray("steps");

        for (JsonElement step : steps) {
            JsonObject transitDetObj = step.getAsJsonObject();
            final JsonObject transitDetail = transitDetObj.getAsJsonObject("transit_details");
            if (transitDetail == null) continue;

            String shortName = transitDetail.get("line").
                    getAsJsonObject().get("short_name").toString();
            String headSign = transitDetail.get("headsign").toString();
            Line line = new Line();
            line.setShortName(shortName.replace("\"", ""));
            line.setName(headSign);
            line.setLineDestinationMarker(headSign);
            //lines.add(line);
        }
    }

    private Boolean validate() {
        Boolean isValid = true;

        if (lineDropdown.getText().toString().isEmpty()) {
            inputLayoutLines.setError(
                getString(R.string.error_guidance_line)
            );
            isValid = false;
        } else {
            inputLayoutLines.setErrorEnabled(false);
        }

        if (direction.getText().toString().isEmpty()) {
            inputLayoutDirection.setError(
                getString(R.string.error_guidance_direction)
            );
            isValid = false;
        } else {
            inputLayoutDirection.setErrorEnabled(false);
        }

        if (editTextSubject.getText().toString().trim().length() >= 0 &&
            editTextSubject.getText().toString().trim().length() < 5
        ) {
            inputLayoutSubject.setError(getString(
                R.string.error_guidance_subject)
            );
            isValid = false;
        } else {
            inputLayoutSubject.setErrorEnabled(false);
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

    private Guidance setGuidance(Guidance guidance) {
        //guidance.setLine();
        String direction =
            this.direction.getText().toString();
        guidance.setDirection(Direction.of(direction));
        guidance.setSupervisor("Teste");
        guidance.setSubject(editTextSubject.getText().toString());
        guidance.setAddress(this.getLocation());
        guidance.setDate(new Date());

        return guidance;
    }

    private Boolean saveGuidance() {
        Boolean isSaved = false;
        guidance = setGuidance(guidance);
        guidanceRepository = new GuidanceRepository(Guidance.class);
        isSaved = guidanceRepository.set(guidance);
        return isSaved;
    }

    private void save(View view) {
        //Boolean isSaved = false;
        if (validate() && saveGuidance()) {
            Snackbar.make(view, "Salvo com Sucesso.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            //PermanenceDialogFragment.this.getDialog().cancel();
            //listener.showPermanenceMessage(view,"Salvo com Sucesso.");
            //isSaved = true;
        }
        //return isSaved;
    }

    //json.routes[0].legs[0].steps[n].transit_details.line.name
    private void setLinesAdapter() {
        ArrayAdapter<Line> adapter = new ArrayAdapter<Line>(
            this.view.getContext(),
            R.layout.dropdown_guidance_lines_menu_popup_item,
            lines
        );
        lineDropdown.setAdapter(adapter);
        lineDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                    Line selected = (Line) parent.getAdapter().getItem(position);
                    guidance.setLine(selected);
                }
            }
        );
    }

    private void setDirectionAdapter() {
        Direction[] directions = new Direction[] {
            Direction.MAIN_TERMINAL,
            Direction.SECONDARY_TERMINAL,
            Direction.GARAGE
        };
        ArrayAdapter<Direction> adapter = new ArrayAdapter<Direction>(
            this.view.getContext(),
            R.layout.dropdown_guidance_directions_menu_popup_item,
            directions
        );
        direction.setAdapter(adapter);
        direction.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                    Direction selected = (Direction)
                        parent.getAdapter().getItem(position);
                    guidance.setDirection(selected);
                }
            }
        );
    }

    private void initializeFields() {
        this.inputLayoutLines = view.findViewById(
            R.id.input_layout_guidance_lines
        );
        this.lineDropdown = view.findViewById(R.id.guidance_lines_dropdown);
        this.inputLayoutDirection =
            view.findViewById(R.id.input_layout_guidance_direction);
        this.inputLayoutSubject =
            view.findViewById(R.id.input_layout_guidance_subject);
        this.direction = view.findViewById(R.id.guidance_directions_dropdown);
        this.editTextSubject = view.findViewById(R.id.edit_text_guidance_subject);
        this.save = view.findViewById(R.id.guidance_button_save);
    }

    private void setListener() {
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

    private void initializeAdapters() {
        this.setGoogleDirections();
        setDirectionAdapter();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.view = inflater.inflate(R.layout.guidance_dialog, null);
        this.getVolleyResponse = new GetVolleyResponse(this.view.getContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
            .setTitle("Orientacao");
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
        dataReposity.getLines().observe(this, new Observer<List<Line>>() {
            @Override
            public void onChanged(List<Line> l) {
                if (l != null) {
                    lines = l;
                    setLinesAdapter();
                }
            }
        });

        initializeFields();
        initializeAdapters();
        setListener();

        return builder.create();
    }

//    public interface GuidanceDialogListener {
//        public Boolean showGuidanceMessage(View view, String message);
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (GuidanceDialogFragment.GuidanceDialogListener) context;
//        } catch (ClassCastException ex) {
//            throw new ClassCastException(context.toString() +
//                "must implement PermanenceDialogListener");
//        }
//    }
}
