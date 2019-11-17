package com.esdrasmorais.inspetoronline.ui.inspection;

import android.app.Application;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.data.BasicApp;
import com.esdrasmorais.inspetoronline.data.DataRepository;
import com.esdrasmorais.inspetoronline.data.GetVolleyResponse;
import com.esdrasmorais.inspetoronline.data.SpTrans;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.esdrasmorais.inspetoronline.data.model.InspectionReport;
import com.esdrasmorais.inspetoronline.data.model.InspectionType;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Rate;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.esdrasmorais.inspetoronline.data.model.WorkDaySchedule;
import com.esdrasmorais.inspetoronline.data.repository.InspectionReportRepository;
import com.esdrasmorais.inspetoronline.data.repository.InspectionRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.GoogleDirections;
import com.esdrasmorais.inspetoronline.data.SecurityPreferences;
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

public class InspectionReportActivity extends AppCompatActivity {
    public static final String TAG = "Inspecao";
    private GoogleDirections googleDirections;
    private SpTrans spTrans;
    List<Line> lines = new ArrayList<Line>();
    List<Vehicle> prefixes = new ArrayList<Vehicle>();
//    List<Company> companies = new ArrayList<Company>();
//    Integer countCompanies = 1;
    //String cookie;
    GetVolleyResponse getVolleyResponse;
    AutoCompleteTextView editTextLineDropdown;
    AutoCompleteTextView editTextPrefixDropdown;
    AutoCompleteTextView editTextLocationDropdown;
    AutoCompleteTextView editTextVehicleStateDropdown;
    AutoCompleteTextView editTextPresentationEmployeesDropdown;
    AutoCompleteTextView editTextVehicleIdentificationDropdown;
    AutoCompleteTextView editTextPersonalObjectsDropdown;
    AutoCompleteTextView editTextVehicleObjectsDropdown;
    AutoCompleteTextView editTextEmployeeIdentificationDropdown;
    AutoCompleteTextView editTextWeelchairSeatDropdown;
    AutoCompleteTextView editTextObjectsForbidenDropdown;
    AutoCompleteTextView editTextVehicleSecurityDropdown;
    AutoCompleteTextView editTextImpedimentDropdown;
    AutoCompleteTextView editTextRateDropdown;
    EditText editTextComments;
    Button save;
    FloatingActionButton fab;
    TextInputLayout inputLayoutLine;
    TextInputLayout inputLayoutPrefix;
    TextInputLayout inputLayoutLocation;
    TextInputLayout inputLayoutVehicleState;
    TextInputLayout inputLayoutPresentationEmployees;
    TextInputLayout inputLayoutVehicleIdentification;
    TextInputLayout inputLayoutPersonalObjects;
    TextInputLayout inputLayoutVehicleObjectsConservation;
    TextInputLayout inputLayoutEmployeeIdentification;
    TextInputLayout inputLayoutWheelchairSeatBelt;
    TextInputLayout inputLayoutObjectsForbidenToRole;
    TextInputLayout inputLayoutVehicleSecurityAccessories;
    TextInputLayout inputLayoutImpedimentToInspection;
    TextInputLayout inputLayoutInspectionRate;
    TextInputLayout inputLayoutComments;
    Inspection inspection;
    InspectionReport inspectionReport;
    InspectionReportRepository inspectionReportRepository;
//    @NonNull
//    private Application application;
    @NonNull
    private DataRepository dataReposity;

    public InspectionReportActivity() {
        this.getVolleyResponse = new GetVolleyResponse(this);
        this.inspection = new Inspection();
        this.inspectionReport = new InspectionReport();
        inspectionReportRepository =
            new InspectionReportRepository(InspectionReport.class);
    }

    private Location getLocation() {
        SecurityPreferences securityPreferences = new SecurityPreferences(
            this
        );
        Location location = new Gson().fromJson(
            securityPreferences.getStoredString("last_know_location"),
            Location.class
        );
        return location;
    }

    private String getLocationAddress() {
        SecurityPreferences securityPreferences = new SecurityPreferences(
    this
        );
        String addressLocation =
            securityPreferences.getStoredString("last_know_location_address");
        return addressLocation;
    }

    private void setGoogleDirections() {
        this.googleDirections = new GoogleDirections(
            this.getApplicationContext(),
            this.getLocation()
        );
        GetVolleyResponse getVolleyResponse = new GetVolleyResponse(this);
        getVolleyResponse.getResponse(
            Request.Method.GET, googleDirections.getUrl(), null,
            new GetVolleyResponse(this) {
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

    private void setLinesDao() {
        this.dataReposity.getLines().
            observe(this, new Observer<List<Line>>() {
                    @Override
                    public void onChanged(List<Line> l) {
                        if (l != null) {
                            lines = l;
                            setLinesAdapter();
                        }
                    }
                }
            );
    }

    private void setVehiclesDao() {
        this.dataReposity.getVehicles().
            observe(this, new Observer<List<Vehicle>>() {
                @Override
                public void onChanged(List<Vehicle> vehicles) {
                    if (vehicles != null) {
                        prefixes = vehicles;
                        setPrefixesAdapter();
                    }
                }
            }
        );
    }

    //json.routes[0].legs[0].steps[n].transit_details.line.name
    private void setLinesAdapter() {
        ArrayAdapter<Line> adapter = new ArrayAdapter<Line>(
            getApplicationContext(), R.layout.dropdown_line_menu_popup_item, lines
        );
        editTextLineDropdown.setAdapter(adapter);
        editTextLineDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Line selected = (Line) parent.getAdapter().getItem(position);
//                Toast.makeText(getApplicationContext(), "Clicked" +
//                    "position = " + position + " line = " +
//                     selected.getShortName(), Toast.LENGTH_LONG).show();
                inspectionReport.setLine(selected);
                }
            }
        );
    }

    private void setSpTrans() {
        this.spTrans = new SpTrans(
            this.getApplicationContext(),
            this.getLocation()
        );
        //this.authenticate();
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
            getApplicationContext(),
            R.layout.dropdown_prefix_menu_popup_item,
            prefixes
        );
        editTextPrefixDropdown.setAdapter(adapter);
        editTextPrefixDropdown.setOnItemClickListener(
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
                inspectionReport.setPrefix(selected);
                }
            }
        );
    }

    private void setLocationsAdapter() {
//        String[] locations = new String[] {
//            "PIRITUBA", "LAPA", "CENTRO", "PINHEIROS"
//        };
        String[] locations = new String[] {
            this.getLocationAddress().replace("\"", "")
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_location_menu_popup_item,
                locations
        );
        editTextLocationDropdown.setAdapter(adapter);
        editTextLocationDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                    parent.getAdapter().getItem(position);
                inspectionReport.setLocation(getLocation());
                }
        }
        );
    }

    private void setVehicleStateAdapter() {
        Rate[] rates = new Rate[] {
            Rate.GREAT, Rate.GOOD, Rate.REGULATE, Rate.BAD
        };
        ArrayAdapter<Rate> adapter = new ArrayAdapter<Rate>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_state_menu_popup_item,
            rates
        );
        editTextVehicleStateDropdown.setAdapter(adapter);
        editTextVehicleStateDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Rate selected = (Rate)
                    parent.getAdapter().getItem(position);
                inspectionReport.setVehicleState(selected);
                }
            }
        );
    }

    private void setPresentationEmployeesAdapter() {
        Rate[] rates = new Rate[] {
            Rate.GREAT, Rate.GOOD, Rate.REGULATE, Rate.BAD
        };
        ArrayAdapter<Rate> adapter = new ArrayAdapter<Rate>(
            getApplicationContext(),
            R.layout.dropdown_presentation_employees_menu_popup_item,
            rates
        );
        editTextPresentationEmployeesDropdown.setAdapter(adapter);
        editTextPresentationEmployeesDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Rate selected = (Rate)
                    parent.getAdapter().getItem(position);
                inspectionReport.setPresentationEmployees(selected);
                }
            }
        );
    }

    private void setVehicleIdentificationAdapter() {
        String[] question = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_identification_menu_popup_item,
            question
        );
        editTextVehicleIdentificationDropdown.setAdapter(adapter);
        editTextVehicleIdentificationDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                    parent.getAdapter().getItem(position);
                inspectionReport.setVehicleIdentification(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setPersonalObjectsCleaningAdapter() {
        Rate[] rates = new Rate[] {
            Rate.GREAT, Rate.GOOD, Rate.REGULATE, Rate.BAD
        };
        ArrayAdapter<Rate> adapter = new ArrayAdapter<Rate>(
            getApplicationContext(),
            R.layout.dropdown_objects_cleaning_menu_popup_item,
            rates
        );
        editTextPersonalObjectsDropdown.setAdapter(adapter);
        editTextPersonalObjectsDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Rate selected = (Rate)
                    parent.getAdapter().getItem(position);
                inspectionReport.setPersonalObjectsCleaning(selected);
                }
            }
        );
    }

    private void setVehicleObjectsConservationAdapter() {
        Rate[] rates = new Rate[] {
            Rate.GREAT, Rate.GOOD, Rate.REGULATE, Rate.BAD
        };
        ArrayAdapter<Rate> adapter = new ArrayAdapter<Rate>(
            getApplicationContext(),
            R.layout.dropdown_objects_conservation_menu_popup_item,
            rates
        );
        editTextVehicleObjectsDropdown.setAdapter(adapter);
        editTextVehicleObjectsDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Rate selected = (Rate)
                    parent.getAdapter().getItem(position);
                inspectionReport.setVehicleObjectsConservation(selected);
                }
            }
        );
    }

    private void setEmployeeIdentificationAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_employee_identification_menu_popup_item,
            prefixes
        );
        editTextEmployeeIdentificationDropdown.setAdapter(adapter);
        editTextEmployeeIdentificationDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                        parent.getAdapter().getItem(position);
                inspectionReport.setEmployeeIdentification(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setWheelchairSeatBeltAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_wheelchair_seat_belt_menu_popup_item,
            prefixes
        );
        editTextWeelchairSeatDropdown.setAdapter(adapter);
        editTextWeelchairSeatDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                        parent.getAdapter().getItem(position);
                inspectionReport.setWheelchairSeatBelt(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setObjectsForbidenToRoleAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_forbiden_to_role_menu_popup_item,
            prefixes
        );
        editTextObjectsForbidenDropdown.setAdapter(adapter);
        editTextObjectsForbidenDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                    parent.getAdapter().getItem(position);
                inspectionReport.setObjectsForbidenToRole(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setVehicleSecurityAccessoriesAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_security_accessories_menu_popup_item,
            prefixes
        );
        editTextVehicleSecurityDropdown.setAdapter(adapter);
        editTextVehicleSecurityDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                    parent.getAdapter().getItem(position);
                inspectionReport.setVehicleSecurityAccessories(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setImpedimentToInspectionAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_impediment_to_inspection_menu_popup_item,
            prefixes
        );
        editTextImpedimentDropdown.setAdapter(adapter);
        editTextImpedimentDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                String selected = (String)
                    parent.getAdapter().getItem(position);
                inspectionReport.setImpedimentToInspection(
                    selected == "SIM" ? true : false
                );
                }
            }
        );
    }

    private void setInspectionRateAdapter() {
        Integer[] prefixes = new Integer[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
            getApplicationContext(),
            R.layout.dropdown_inspection_rate_menu_popup_item,
            prefixes
        );
        editTextRateDropdown.setAdapter(adapter);
        editTextRateDropdown.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id
                ) {
                Integer selected = (Integer)
                    parent.getAdapter().getItem(position);
                inspectionReport.setRate(selected);
                }
            }
        );
    }

    private void initializeTextDropDowns() {
        this.editTextLineDropdown = findViewById(R.id.line_dropdown);
        this.editTextPrefixDropdown = findViewById(R.id.prefix_dropdown);
        this.editTextLocationDropdown = findViewById(R.id.location_dropdown);
        this.editTextVehicleStateDropdown =
            findViewById(R.id.vehicle_state_dropdown);
        this.editTextPresentationEmployeesDropdown =
            findViewById(R.id.presentation_employees);
        this.editTextVehicleIdentificationDropdown =
            findViewById(R.id.vehicle_identification);
        this.editTextPersonalObjectsDropdown =
            findViewById(R.id.personal_objects_cleaning);
        this.editTextVehicleObjectsDropdown =
            findViewById(R.id.vehicle_objects_conservation);
        this.editTextEmployeeIdentificationDropdown =
            findViewById(R.id.employee_identification);
        this.editTextWeelchairSeatDropdown =
            findViewById(R.id.wheelchair_seat_belt);
        this.editTextObjectsForbidenDropdown =
            findViewById(R.id.objects_forbiden_to_role);
        this.editTextVehicleSecurityDropdown =
            findViewById(R.id.vehicle_security_accessories);
        this.editTextImpedimentDropdown =
            findViewById(R.id.impediment_to_inspection);
        this.editTextRateDropdown = findViewById(R.id.inspection_rate);
    }

    private void initializeInputLayouts() {
        this.inputLayoutLine = findViewById(R.id.input_layout_line);
        this.inputLayoutPrefix = findViewById(R.id.input_layout_prefix);
        this.inputLayoutLocation = findViewById(R.id.input_layout_location);
        this.inputLayoutVehicleState =
            findViewById(R.id.input_layout_vehicle_state);
        this.inputLayoutPresentationEmployees =
            findViewById(R.id.input_layout_presentation_employees);
        this.inputLayoutVehicleIdentification =
            findViewById(R.id.input_layout_vehicle_identification);
        this.inputLayoutPersonalObjects =
            findViewById(R.id.input_layout_personal_objects);
        this.inputLayoutVehicleObjectsConservation =
            findViewById(R.id.input_layout_vehicle_objects_conservation);
        this.inputLayoutEmployeeIdentification =
            findViewById(R.id.input_layout_employee_identification);
        this.inputLayoutWheelchairSeatBelt =
            findViewById(R.id.input_layout_wheelchair_seat_belt);
        this.inputLayoutObjectsForbidenToRole =
            findViewById(R.id.input_layout_objects_forbiden_to_role);
        this.inputLayoutVehicleSecurityAccessories =
            findViewById(R.id.input_layout_vehicle_security_accessories);
        this.inputLayoutImpedimentToInspection =
            findViewById(R.id.input_layout_impediment_to_inspection);
        this.inputLayoutInspectionRate =
            findViewById(R.id.input_layout_inspection_rate);
        this.inputLayoutComments = findViewById(R.id.input_layout_comments);
    }

    private void initializeWidgets() {
        this.initializeTextDropDowns();
        this.initializeInputLayouts();

        this.editTextComments = findViewById(R.id.edit_text_comments);
        this.save = findViewById(R.id.button_save);
        //this.fab = findViewById(R.id.fab);
    }

    private void initializeAdapters() {
        this.setGoogleDirections();
        this.setSpTrans();
        this.setLinesDao();
        this.setVehiclesDao();
        this.setLocationsAdapter();
        this.setVehicleStateAdapter();
        this.setPresentationEmployeesAdapter();
        this.setVehicleIdentificationAdapter();
        this.setPersonalObjectsCleaningAdapter();
        this.setVehicleObjectsConservationAdapter();
        this.setEmployeeIdentificationAdapter();
        this.setWheelchairSeatBeltAdapter();
        this.setObjectsForbidenToRoleAdapter();
        this.setVehicleSecurityAccessoriesAdapter();
        this.setImpedimentToInspectionAdapter();
        this.setInspectionRateAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.dataReposity = new BasicApp(this.getApplicationContext()).getRepository();
        this.initializeWidgets();
        this.initializeAdapters();

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

    private Boolean validate() {
        Boolean isValid = true;
        if (editTextLineDropdown.getText().toString().isEmpty()) {
            inputLayoutLine.setError(getString(R.string.error_line));
            isValid = false;
        } else {
            inputLayoutLine.setErrorEnabled(false);
        }

        if (editTextPrefixDropdown.getText().toString().isEmpty()) {
            inputLayoutPrefix.setError(getString(R.string.error_prefix));
            isValid = false;
        } else {
            inputLayoutPrefix.setErrorEnabled(false);
        }

        if (editTextLocationDropdown.getText().toString().isEmpty()) {
            inputLayoutLocation.setError(getString(R.string.error_location));
            isValid = false;
        } else {
            inputLayoutLocation.setErrorEnabled(false);
        }

        if (editTextVehicleStateDropdown.getText().toString().isEmpty()) {
            inputLayoutVehicleState.setError(
                getString(R.string.error_vehicle_state)
            );
            isValid = false;
        } else {
            inputLayoutVehicleState.setErrorEnabled(false);
        }

        if (
            editTextPresentationEmployeesDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutPresentationEmployees.setError(
                getString(R.string.error_presentation_employees)
            );
            isValid = false;
        } else {
            inputLayoutPresentationEmployees.setErrorEnabled(false);
        }

        if (
            editTextVehicleIdentificationDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutVehicleIdentification.setError(
                getString(R.string.error_vehicle_identification)
            );
            isValid = false;
        } else {
            inputLayoutVehicleIdentification.setErrorEnabled(false);
        }

        if (
            editTextPersonalObjectsDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutPersonalObjects.setError(
                getString(R.string.error_personal_objects_cleaning)
            );
            isValid = false;
        } else {
            inputLayoutPersonalObjects.setErrorEnabled(false);
        }

        if (
            editTextVehicleObjectsDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutVehicleObjectsConservation.setError(
                getString(R.string.error_vehicle_objects_conservation)
            );
            isValid = false;
        } else {
            inputLayoutVehicleObjectsConservation.setErrorEnabled(false);
        }

        if (
            editTextEmployeeIdentificationDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutEmployeeIdentification.setError(
                getString(R.string.error_employee_identification)
            );
            isValid = false;
        } else {
            inputLayoutEmployeeIdentification.setErrorEnabled(false);
        }

        if (
            editTextWeelchairSeatDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutWheelchairSeatBelt.setError(
                    getString(R.string.error_wheelchair_seat_belt)
            );
            isValid = false;
        } else {
            inputLayoutWheelchairSeatBelt.setErrorEnabled(false);
        }

        if (
            editTextObjectsForbidenDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutObjectsForbidenToRole.setError(
                getString(R.string.error_objects_forbiden_to_role)
            );
            isValid = false;
        } else {
            inputLayoutObjectsForbidenToRole.setErrorEnabled(false);
        }

        if (
            editTextVehicleSecurityDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutVehicleSecurityAccessories.setError(
                getString(R.string.error_vehicle_security_accessories)
            );
            isValid = false;
        } else {
            inputLayoutVehicleSecurityAccessories.setErrorEnabled(false);
        }

        if (
            editTextImpedimentDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutImpedimentToInspection.setError(
                getString(R.string.error_impediment_to_inspection)
            );
            isValid = false;
        } else {
            inputLayoutImpedimentToInspection.setErrorEnabled(false);
        }

        if (
            editTextRateDropdown.getText().toString().isEmpty()
        ) {
            inputLayoutInspectionRate.setError(
                getString(R.string.error_inspection_rate)
            );
            isValid = false;
        } else {
            inputLayoutInspectionRate.setErrorEnabled(false);
        }

        if (
            editTextComments.getText().toString().trim().length() > 0 &&
            editTextComments.getText().toString().trim().length() < 5
        ) {
            inputLayoutComments.setError(
                getString(R.string.error_comments)
            );
            isValid = false;
        } else {
            inputLayoutComments.setErrorEnabled(false);
        }

        return isValid;
    }

    private Inspection setInspection(Inspection inspection) {
//        inspection.setId(1L);

        inspection.setStartTime(new Date());
        Date endTime = new Date();
        endTime.setHours(8);

        inspection.setEndTime(endTime);
        inspection.setAddress(this.getLocation());
        inspection.setNote("teste");
        inspection.setType(InspectionType.REPORT);
        inspection.setSchedule(new WorkDaySchedule());

        return inspection;
    }

    private InspectionReport setInspectionReport(
        InspectionReport inspectionReport
    ) {
        inspectionReport.setComments(editTextComments.getText().toString());
        inspectionReport.setInspection(inspection);
        return inspectionReport;
    }

    private Boolean saveInspection() {
        Boolean isSaved = false;
        inspection = setInspection(inspection);
        InspectionRepository inspectionRepository = new InspectionRepository(
            Inspection.class
        );

        if (inspectionRepository.set(inspection)) {
//            List<Inspection> i = new ArrayList<Inspection>();
//            inspectionRepository.search(i);
            inspectionReport = setInspectionReport(inspectionReport);
            InspectionReportRepository inspectionReportRepository =
                new InspectionReportRepository(InspectionReport.class);
            isSaved = inspectionReportRepository.set(inspectionReport);
        }

        return isSaved;
    }

    private void save(View view) {
        if (validate() && saveInspection())
            Snackbar.make(view, "Salvo com Sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}