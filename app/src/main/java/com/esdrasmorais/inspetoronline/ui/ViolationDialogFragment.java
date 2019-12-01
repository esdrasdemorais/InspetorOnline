package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
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
import com.esdrasmorais.inspetoronline.data.model.Department;
import com.esdrasmorais.inspetoronline.data.model.Direction;
import com.esdrasmorais.inspetoronline.data.model.Employee;
import com.esdrasmorais.inspetoronline.data.model.EmployeeType;
import com.esdrasmorais.inspetoronline.data.model.Guidance;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.State;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.esdrasmorais.inspetoronline.data.model.Violation;
import com.esdrasmorais.inspetoronline.data.model.ViolationType;
import com.esdrasmorais.inspetoronline.data.model.WorkTime;
import com.esdrasmorais.inspetoronline.data.repository.GuidanceRepository;
import com.esdrasmorais.inspetoronline.data.repository.ViolationRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ViolationDialogFragment extends AppCompatDialogFragment {
    private FragmentActivity fragmentActivity;
    private GoogleDirections googleDirections;
    private SpTrans spTrans;
    private String cookie;
    private GetVolleyResponse getVolleyResponse;
    private List<Company> companies = new ArrayList<Company>();
    private Integer countCompanies = 1;
    private List<Line> lines = new ArrayList<Line>();
    List<Vehicle> prefixes = new ArrayList<Vehicle>();
    private TextInputLayout inputLayoutWorkTime;
    private TextInputLayout inputLayoutNote;
    private TextInputLayout inputLayoutLines;
    private TextInputLayout inputLayoutDepartment;
    private TextInputLayout inputLayoutPrefixes;
    private TextInputLayout inputLayoutEmployeeType;
    private TextInputLayout inputLayoutState;
    private TextInputLayout inputLayoutType;
    private AutoCompleteTextView lineDropdown;
    private AutoCompleteTextView prefixDropdown;
    private AutoCompleteTextView direction;
    private AutoCompleteTextView violationDepartment;
    private AutoCompleteTextView employeeTypesDropdown;
    private AutoCompleteTextView workTimeDropdown;
    private AutoCompleteTextView stateDropdown;
    private AutoCompleteTextView typeDropdown;
    private AutoCompleteTextView departmentDropdown;
    private EditText editTextNote;
    private View view;
    private Violation violation;
    private ViolationRepository violationRepository;
    //    private GuidanceDialogFragment.GuidanceDialogListener listener;
    private Button save;

    @NonNull
    private Application application;

    @NonNull
    private DataRepository dataReposity;

    public ViolationDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.violation = new Violation();
        application = fragmentActivity.getApplication();
        dataReposity = ((BasicApp) application).getRepository();
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
            lines.add(line);
        }
    }

    private void setPrefixes(JsonArray prefixes) {
        try {
            for (JsonElement prefix : prefixes) {
                JsonObject prefixObject = prefix.getAsJsonObject();

                Vehicle vehicle = new Vehicle();
                vehicle.setPrefix(Integer.parseInt(
                        prefixObject.get("p").toString())
                );
                vehicle.setHandicappedAccessible(Boolean.parseBoolean(
                        prefixObject.get("a").toString()
                ));

                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'"
                );
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date localizedAt = sdf.parse(
                        prefixObject.get("ta").toString().replace("\"", "")
                );
                vehicle.setLocalizatedAt(localizedAt);

                vehicle.setLatitude(Double.parseDouble(
                        prefixObject.get("py").toString())
                );
                vehicle.setLongitude(Double.parseDouble(
                        prefixObject.get("px").toString()
                ));
                this.prefixes.add(vehicle);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setPrefixesAdapter() {
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_prefix_menu_popup_item,
            prefixes
        );*/
//        VehicleAdapter adapter = new VehicleAdapter(
//            getApplicationContext(), prefixes
//        );
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<Vehicle>(
            this.view.getContext(),
            R.layout.dropdown_prefix_menu_popup_item,
            prefixes
        );
        prefixDropdown.setAdapter(adapter);
        prefixDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                    Vehicle selected = (Vehicle)
                        parent.getAdapter().getItem(position);
                    violation.setPrefix(selected);
                }
            }
        );
    }

    private void setSpTrans() {
        this.spTrans = new SpTrans(
            this.view.getContext(),
            this.getLocation()
        );
    }

    private Boolean validate() {
        Boolean isValid = true;

        if (lineDropdown.getText().toString().isEmpty()) {
            inputLayoutLines.setError(
                getString(R.string.error_violation_line)
            );
            isValid = false;
        } else {
            inputLayoutLines.setErrorEnabled(false);
        }

        if (prefixDropdown.getText().toString().isEmpty()) {
            inputLayoutPrefixes.setError(
                    getString(R.string.error_violation_prefix)
            );
            isValid = false;
        } else {
            inputLayoutPrefixes.setErrorEnabled(false);
        }

        if (employeeTypesDropdown.getText().toString().isEmpty()) {
            inputLayoutEmployeeType.setError(
                    getString(R.string.error_violation_employee_type)
            );
            isValid = false;
        } else {
            inputLayoutEmployeeType.setErrorEnabled(false);
        }

        if (workTimeDropdown.getText().toString().isEmpty()) {
            inputLayoutWorkTime.setError(
                getString(R.string.error_violation_work_time)
            );
            isValid = false;
        } else {
            inputLayoutWorkTime.setErrorEnabled(false);
        }

        if (stateDropdown.getText().toString().isEmpty()) {
            inputLayoutState.setError(
                getString(R.string.error_violation_state)
            );
            isValid = false;
        } else {
            inputLayoutState.setErrorEnabled(false);
        }


        if (stateDropdown.getText().toString().isEmpty()) {
            inputLayoutState.setError(
                    getString(R.string.error_violation_state)
            );
            isValid = false;
        } else {
            inputLayoutState.setErrorEnabled(false);
        }
//        if (editTextSubject.getText().toString().trim().length() >= 0 &&
//                editTextSubject.getText().toString().trim().length() < 5
//        ) {
//            inputLayoutSubject.setError(getString(
//                    R.string.error_guidance_subject)
//            );
//            isValid = false;
//        } else {
//            inputLayoutSubject.setErrorEnabled(false);
//        }
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

    private Violation setViolation(Violation violation) {
        String department =
            this.violationDepartment.getText().toString();
        violation.setDepartment(Department.of(Integer.parseInt(department)));
        violation.setAddress(this.getLocation());
        violation.setDate(new Date());

        return violation;
    }

    private Boolean saveViolation() {
        Boolean isSaved = false;
        violation = setViolation(violation);
        violationRepository = new ViolationRepository(Violation.class);
        isSaved = violationRepository.set(violation);
        return isSaved;
    }

    private void save(View view) {
        //Boolean isSaved = false;
        if (validate() && saveViolation()) {
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
                R.layout.dropdown_violation_lines_menu_popup_item,
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
                    violation.setLine(selected);
                }
            }
        );
    }

    private void setEmployeeTypeAdapter() {
        EmployeeType[] employeeTypes = new EmployeeType[] {
            EmployeeType.DRIVER,
            EmployeeType.TICKET_COLLECTOR,
            EmployeeType.SUPERVISOR,
            EmployeeType.COORDINATOR,
            EmployeeType.VALET_PARKING,
            EmployeeType.PLANTONIST,
            EmployeeType.LIFEGUARD
        };
        ArrayAdapter<EmployeeType> adapter = new ArrayAdapter<EmployeeType>(
            this.view.getContext(),
            R.layout.dropdown_violation_employee_type_menu_popup_item,
            employeeTypes
        );
        employeeTypesDropdown.setAdapter(adapter);
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

    private void setWorkTimeAdapter() {
        WorkTime[] workTimes = new WorkTime[] {
            WorkTime.MORNING,
            WorkTime.AFTERNOON,
            WorkTime.NIGHT,
            WorkTime.DAWN
        };
        ArrayAdapter<WorkTime> adapter = new ArrayAdapter<WorkTime>(
            this.view.getContext(),
            R.layout.dropdown_violation_work_time_menu_popup_item,
            workTimes
        );
        workTimeDropdown.setAdapter(adapter);
    }

    private void setStateAdapter() {
        State[] states = new State[] {
            State.LIGHT,
            State.SERIOUS,
            State.CAPITAL,
            State.LOBBY,
            State.PREVENTIVE,
            State.ANALYZE
        };
        ArrayAdapter<State> adapter = new ArrayAdapter<State>(
            this.view.getContext(),
            R.layout.dropdown_violation_state_menu_popup_item,
            states
        );
        stateDropdown.setAdapter(adapter);
    }

    private void setTypeAdapter() {
        ViolationType[] violationTypes = new ViolationType[] {
            ViolationType.NON_JUSTIFIED_ABSENCE,
            ViolationType.JUSTIFIED_ABSENCE,
            ViolationType.LEAVE_JOB,
            ViolationType.AGGRESSION,
            ViolationType.CHANGE_LINE_ITINERARY,
            ViolationType.NOTE_INCORRECTLY_ENTERING_VALIDATOR_DATA,
            ViolationType.SHOW_SYMPTOMS_OF_INTOXICATION,
            ViolationType.GET_INTRODUCED_TO_IRREGULAR_UNIFORM,
            ViolationType.DISHONEST_ATTITUDE,
            ViolationType.DELAY_FAIRS_DELIVERY,
            ViolationType.NEXT_SEMAPHORE_CLOSED,
            ViolationType.ARRIVE_LATE_FOR_SERVICE,
            ViolationType.BEHAVIOR_INCONVENIENT,
            ViolationType.STOP_REPORTING_ACCIDENT_OCCURED,
            ViolationType.STOP_REPORTING,
            ViolationType.DISOBEYING_THE_SUPERVISOR,
            ViolationType.DISOBEYING_SCHEDULE,
            ViolationType.DISRESPECT_EMPLOYEE,
            ViolationType.DISRESPECT_PASSENGER_PEDESTRIAN,
            ViolationType.DRIVING_SPEAKING_TO_CELL_PHONE_HEADSET,
            ViolationType.DRIVING_THE_VEHICLE_WITHOUT_AUTHORIZATION,
            ViolationType.PERFORMING_OVERDRIVE,
            ViolationType.EMBARKATION_DISEMBARKATION_IRREGULAR_POINT,
            ViolationType.IRREGULAR_LOADING_UNLOADING_DOOR,
            ViolationType.IRREGULAR_FAIRS_DELIVERY,
            ViolationType.EXCEED_MAX_SPEED_ALLOWED,
            ViolationType.MAKING_USE_OF_OBJECTS_INADEQUATE_TO_FUNCTION,
            ViolationType.LACK_OF_CLEANLINESS,
            ViolationType.BREAK_OR_HARD_START,
            ViolationType.SMOKING_INSIDE_THE_COLLECTIVE,
            ViolationType.OVERTAKING_IN_PLACE_OF_POINT,
            ViolationType.TRAFFIC_WITHOUT_IDENTIFICATION_PLATE,
            ViolationType.PLACE_OF_WORK_POORLY_PRESERVED,
            ViolationType.DO_NOT_ASSIST_THE_DRIVE,
            ViolationType.DO_NOT_CHARGE,
            ViolationType.NO_CHECK_SCHOOL_SPECIAL_IDENTIFICATION
        };
        ArrayAdapter<ViolationType> adapter = new ArrayAdapter<ViolationType>(
            this.view.getContext(),
            R.layout.dropdown_violation_type_menu_popup_item,
            violationTypes
        );
        typeDropdown.setAdapter(adapter);
    }

    private void setDepartmentAdapter() {
        Department[] departments = new Department[] {
            Department.ONE,
            Department.TWO,
            Department.THREE,
            Department.FOUR,
            Department.FIVE,
            Department.SIX,
            Department.SEVEN,
        };
        ArrayAdapter<Department> adapter = new ArrayAdapter<Department>(
            this.view.getContext(),
            R.layout.dropdown_violation_department_menu_popup_item,
            departments
        );
        departmentDropdown.setAdapter(adapter);
    }

    private void initializeFields() {
        this.inputLayoutLines = view.findViewById(
            R.id.input_layout_violation_lines
        );
        this.lineDropdown = view.findViewById(R.id.violation_lines_dropdown);
        this.inputLayoutPrefixes = view.findViewById(
            R.id.input_layout_violation_prefixes
        );
        this.prefixDropdown = view.findViewById(R.id.violation_prefixes_dropdown);
        this.inputLayoutEmployeeType = view.findViewById(
            R.id.input_layout_violation_employee_type
        );
        this.employeeTypesDropdown = view.findViewById(
            R.id.input_layout_violation_employee_type_dropdown
        );
        this.inputLayoutWorkTime = view.findViewById(
            R.id.input_layout_violation_work_time
        );
        this.workTimeDropdown = view.findViewById(
            R.id.input_layout_violation_work_time_dropdown
        );
        this.inputLayoutState = view.findViewById(R.id.input_layout_violation_state);
        this.stateDropdown = view.findViewById(
            R.id.input_layout_violation_state_dropdown
        );
        this.inputLayoutType = view.findViewById(R.id.input_layout_violation_type);
        this.typeDropdown = view.findViewById(
            R.id.input_layout_violation_type_dropdown
        );
        this.inputLayoutDepartment = view.findViewById(
            R.id.input_layout_violation_department
        );
        this.departmentDropdown =
            view.findViewById(R.id.input_layout_violation_department_dropdown);
//        this.inputLayoutSubject =
//            view.findViewById(R.id.input_layout_guidance_subject);
//        this.editTextNote = view.findViewById(R.id.edit_text_violation_note);
//        this.save = view.findViewById(R.id.violation_button_save);
    }

    private void setListener() {
//        this.save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                save(view);
//            }
//        });
    }

    private void initializeAdapters() {
        this.setGoogleDirections();
        this.setSpTrans();
        this.setEmployeeTypeAdapter();
        this.setWorkTimeAdapter();
        this.setStateAdapter();
        this.setTypeAdapter();
        this.setDepartmentAdapter();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        this.view = inflater.inflate(R.layout.violation_dialog, null);
        this.getVolleyResponse = new GetVolleyResponse(this.view.getContext());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
            .setTitle("Infração");
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

        dataReposity.getVehicles().observe(this, new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(List<Vehicle> v) {
                if (v != null) {
                    prefixes = v;
                    setPrefixesAdapter();
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
