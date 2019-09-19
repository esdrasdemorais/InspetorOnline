package com.esdrasmorais.inspetoronline.ui;

import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.DialogCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.R;
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
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.esdrasmorais.inspetoronline.data.model.Violation;
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
    private AutoCompleteTextView lineDropdown;
    private AutoCompleteTextView prefixDropdown;
    private AutoCompleteTextView direction;
    private AutoCompleteTextView violationDepartment;
    private AutoCompleteTextView employeeTypesDropdown;
    private AutoCompleteTextView workTimeDroddown;
    private EditText editTextNote;
    private View view;
    private Violation violation;
    private ViolationRepository violationRepository;
    //    private GuidanceDialogFragment.GuidanceDialogListener listener;
    private Button save;

    public ViolationDialogFragment(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.violation = new Violation();
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

    private void setLines() {
        final JsonArray lines = spTrans.getJson().get("l").getAsJsonArray();

        if (!lines.isJsonArray())
            throw new IllegalArgumentException("json is not an array");

        for (JsonElement line : lines) {
            JsonObject lineObject = line.getAsJsonObject();

            Line lineModel = new Line();
            String shortName = lineObject.get("c").toString();
            lineModel.setShortName(shortName.replace("\"", ""));
            lineModel.setLineCode(Integer.parseInt(lineObject.get("cl").toString()));
            lineModel.setName(
                    lineObject.get("lt0").toString().replace("\"", "")
            );
            lineModel.setLineDestinationMarker(
                    lineObject.get("lt0").toString().replace("\"", "")
            );
            lineModel.setLineOriginMarker(
                    lineObject.get("lt1").toString().replace("\"", "")
            );
            lineModel.setVehiclesQuantityLocalized(
                    Integer.parseInt(lineObject.get("qv").toString())
            );
            this.lines.add(lineModel);

            final JsonArray prefixes = lineObject.getAsJsonArray("vs");
            this.setPrefixes(prefixes);
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
//                Toast.makeText(getApplicationContext(), "Clicked " +
//                    position + " prefix: " + selected.getPrefix(),
//                    Toast.LENGTH_LONG).show();
                    violation.setPrefix(selected);
                }
            }
        );
    }

    private void setLinesFromCompanies() {
        for (Company company : companies) {
            getVolleyResponse.getResponse(
                    Request.Method.GET, spTrans.getUrl() +
                            "/Posicao/Garagem?codigoEmpresa=" +
                            company.getCompanyReferenceCode(), null,
                    new GetVolleyResponse(this.view.getContext()) {
                        @Override
                        public void onSuccessResponse(String result) {
                            try {
                                JSONObject response = new JSONObject(result);
                                spTrans.setJson(
                                        new Gson().fromJson(result, JsonObject.class)
                                );
                                setLines();
                                if (countCompanies == companies.size()) {
                                    setLinesAdapter();
                                    setPrefixesAdapter();
                                }
                                countCompanies++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
        }
    }

    private void authenticate() {
        String url = this.spTrans.getUrl() + "/Login/Autenticar?token=" +
                this.spTrans.getApiToken();
        getVolleyResponse.getResponse(Request.Method.POST, url, null,
                new GetVolleyResponse(this.view.getContext()) {
                    @Override
                    public void onSuccessResponse(String result) {
                        try {
                            //JSONObject response = new JSONObject(result);
                            //spTrans.setJson(new Gson().fromJson(result, JsonObject.class));
                            if (Boolean.parseBoolean(result) == true) {
                                cookie = getVolleyResponse.getCookie();
                                setCompanies();
                                setLinesFromCompanies();
                            }
                            //notifyPropertyChanged(BR.json);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void setSpTrans() {
        this.spTrans = new SpTrans(
                this.view.getContext(),
                this.getLocation()
        );
        this.authenticate();
    }

    private void setCompanies() {
        Company company = new Company();
        company.setOperationAreaCode(1);
        company.setCompanyReferenceCode(38);
        company.setCompanyName("SANTA BRIGIDA");
        companies.add(company);
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
            inputLayoutLines.setError(
                    getString(R.string.error_guidance_direction)
            );
            isValid = false;
        } else {
            inputLayoutLines.setErrorEnabled(false);
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

    private void initializeFields() {
        this.inputLayoutLines = view.findViewById(
            R.id.input_layout_violation_lines
        );
        this.lineDropdown = view.findViewById(R.id.violation_lines_dropdown);
        this.inputLayoutPrefixes = view.findViewById(
            R.id.input_layout_violation_prefixes
        );
        this.prefixDropdown = view.findViewById(R.id.violation_prefixes_dropdown);
//        this.inputLayoutDepartment =
//            view.findViewById(R.id.input_layout_violation_department);
//        this.departmentDropdown =
//            view.findViewById(R.id.violation_department_dropdown);
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
