package com.esdrasmorais.inspetoronline.ui.inspection;

import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.data.GetVolleyResponse;
import com.esdrasmorais.inspetoronline.data.SpTrans;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.esdrasmorais.inspetoronline.data.model.InspectionReport;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.esdrasmorais.inspetoronline.data.repository.InspectionReportRepository;
import com.esdrasmorais.inspetoronline.data.repository.InspectionRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

public class InspectionActivity extends AppCompatActivity {
    public static final String TAG = "Inspecao";
    private GoogleDirections googleDirections;
    private SpTrans spTrans;
    List<Line> lines = new ArrayList<Line>();
    List<Vehicle> prefixes = new ArrayList<Vehicle>();
    List<Company> companies = new ArrayList<Company>();
    Integer countCompanies = 1;
    String cookie;
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
    AutoCompleteTextView editTextPWeelchairSeatDropdown;
    AutoCompleteTextView editTextObjectsForbidenDropdown;
    AutoCompleteTextView editTextVehicleSecurityDropdown;
    AutoCompleteTextView editTextImpedimentDropdown;
    AutoCompleteTextView editTextRateDropdown;
    Button save;
    FloatingActionButton fab;
    TextInputLayout inputLayoutLine;
    TextInputLayout inputLayoutPrefix;

    public InspectionActivity() {
        this.getVolleyResponse = new GetVolleyResponse(this);
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

        if (!routes.isJsonArray() ||
            routes.get(0).getAsJsonObject().getAsJsonArray("legs") == null
        )
            throw new IllegalArgumentException("json is not an array");

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
                Toast.makeText(getApplicationContext(), "Clicked" +
                    "position = " + position + " line = " +
                     selected.getShortName(), Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    private void authenticate() {
        String url = this.spTrans.getUrl() + "/Login/Autenticar?token=" +
            this.spTrans.getApiToken();
        getVolleyResponse.getResponse(Request.Method.POST, url, null,
            new GetVolleyResponse(this) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        //JSONObject response = new JSONObject(result);
                        //spTrans.setJson(new Gson().fromJson(result, JsonObject.class));
                        if (Boolean.parseBoolean(result) == true) {
                            cookie = getVolleyResponse.getCookie();
                            getCompanies();
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
            this.getApplicationContext(),
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

    private void getCompanies() {
        getVolleyResponse.getResponse(
            Request.Method.GET, spTrans.getUrl() + "/Empresa", null,
            new GetVolleyResponse(this) {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject response = new JSONObject(result);
                        spTrans.setJson(
                            new Gson().fromJson(result, JsonObject.class)
                        );
                        setCompanies();
                        setLinesFromCompanies();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
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

    private void setLinesFromCompanies() {
        for (Company company : companies) {
            getVolleyResponse.getResponse(
                Request.Method.GET, spTrans.getUrl() +
                    "/Posicao/Garagem?codigoEmpresa=" +
                    company.getCompanyReferenceCode(), null,
                new GetVolleyResponse(this) {
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
                Toast.makeText(getApplicationContext(), "Clicked " +
                    position + " prefix: " + selected.getPrefix(),
                    Toast.LENGTH_LONG).show();
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
    }

    private void setVehicleStateAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_state_menu_popup_item,
            prefixes
        );
        editTextVehicleStateDropdown.setAdapter(adapter);
    }

    private void setPresentationEmployeesAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_presentation_employees_menu_popup_item,
            prefixes
        );
        editTextPresentationEmployeesDropdown.setAdapter(adapter);
    }

    private void setVehicleIdentificationAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_identification_menu_popup_item,
            prefixes
        );
        editTextVehicleIdentificationDropdown.setAdapter(adapter);
    }

    private void setPersonalObjectsCleaningAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_cleaning_menu_popup_item,
            prefixes
        );
        editTextPersonalObjectsDropdown.setAdapter(adapter);
    }

    private void setVehicleObjectsConservationAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_conservation_menu_popup_item,
            prefixes
        );
        editTextVehicleObjectsDropdown.setAdapter(adapter);
    }

    private void setEmployeeIdentificationAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_employee_identification_menu_popup_item,
            prefixes
        );
        editTextEmployeeIdentificationDropdown.setAdapter(adapter);
    }

    private void setWheelchairSeatBeltAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_wheelchair_seat_belt_menu_popup_item,
            prefixes
        );
        editTextPWeelchairSeatDropdown.setAdapter(adapter);
    }

    private void setObjectsForbidenToRoleAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_forbiden_to_role_menu_popup_item,
            prefixes
        );
        editTextObjectsForbidenDropdown.setAdapter(adapter);
    }

    private void setVehicleSecurityAccessoriesAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_security_accessories_menu_popup_item,
            prefixes
        );
        editTextVehicleSecurityDropdown.setAdapter(adapter);
    }

    private void setImpedimentToInspectionAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_impediment_to_inspection_menu_popup_item,
            prefixes
        );
        editTextImpedimentDropdown.setAdapter(adapter);
    }

    private void setInspectionRateAdapter() {
        String[] prefixes = new String[] {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_inspection_rate_menu_popup_item,
            prefixes
        );
        editTextRateDropdown.setAdapter(adapter);
    }

    private void initializeWidgets() {
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
        this.editTextPWeelchairSeatDropdown =
            findViewById(R.id.wheelchair_seat_belt);
        this.editTextObjectsForbidenDropdown =
            findViewById(R.id.objects_forbiden_to_role);
        this.editTextVehicleSecurityDropdown =
            findViewById(R.id.vehicle_security_accessories);
        this.editTextImpedimentDropdown =
           findViewById(R.id.impediment_to_inspection);
        this.editTextRateDropdown = findViewById(R.id.inspection_rate);
        this.save = findViewById(R.id.button_save);
        this.fab = findViewById(R.id.fab);
        this.inputLayoutLine = findViewById(R.id.input_layout_line);
        this.inputLayoutPrefix = findViewById(R.id.input_layout_prefix);
    }

    private void initializeAdapters() {
        this.setGoogleDirections();
        this.setSpTrans();
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
        setContentView(R.layout.activity_inspection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.initializeWidgets();
        this.initializeAdapters();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

    private Boolean validate() {
        Boolean isValid = false;
        if (editTextLineDropdown.getText().toString().isEmpty()) {
            inputLayoutLine.setError(getString(R.string.error_message_line));
        } else if (editTextPrefixDropdown.getText().toString().isEmpty()) {
            inputLayoutPrefix.setError(getString(R.string.error_message_prefix));
        } else {
            isValid = true;
        }
        return isValid;
    }

    private Inspection setInspection(Inspection inspection) {
        return inspection;
    }

    private InspectionReport setInspectionReport(
        InspectionReport inspectionReport
    ) {
        return inspectionReport;
    }

    private Boolean saveInspection() {
        Boolean isSaved = false;

        Inspection inspection = new Inspection();
        inspection = setInspection(inspection);
        InspectionRepository inspectionRepository = new InspectionRepository();

        if (inspectionRepository.set(inspection)) {
            InspectionReport inspectionReport = new InspectionReport();
            inspectionReport = setInspectionReport(inspectionReport);
            InspectionReportRepository inspectionReportRepository =
                new InspectionReportRepository();
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