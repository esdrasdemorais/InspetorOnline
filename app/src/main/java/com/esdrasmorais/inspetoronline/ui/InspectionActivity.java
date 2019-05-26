package com.esdrasmorais.inspetoronline.ui;

import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.data.GetVolleyResponse;
import com.esdrasmorais.inspetoronline.data.SpTrans;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.esdrasmorais.inspetoronline.R;
import com.esdrasmorais.inspetoronline.data.GoogleDirections;
import com.esdrasmorais.inspetoronline.data.SecurityPreferences;
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

    public InspectionActivity() {
        this.getVolleyResponse = new GetVolleyResponse(this);
    }

    private Location getLocation() {
        SecurityPreferences securityPreferences = new SecurityPreferences(this);
        Location location = new Gson().fromJson(
            securityPreferences.getStoredString("last_know_location"),
            Location.class
        );
        return location;
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
        AutoCompleteTextView editTextPrefixDropdown = findViewById(R.id.line_dropdown);
        editTextPrefixDropdown.setAdapter(adapter);
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
        VehicleAdapter adapter = new VehicleAdapter(this, prefixes);
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.prefix_dropdown);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setLocationsAdapter() {
        String[] prefixes = new String[] { "PIRITUBA", "LAPA", "CENTRO", "PINHEIROS" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_location_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.location_dropdown);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setVehicleStateAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_state_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.vehicle_state_dropdown);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setPresentationEmployeesAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_presentation_employees_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.presentation_employees);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setVehicleIdentificationAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_identification_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.vehicle_identification);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setPersonalObjectsCleaningAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.dropdown_objects_cleaning_menu_popup_item,
                prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.personal_objects_cleaning);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setVehicleObjectsConservationAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_conservation_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.vehicle_objects_conservation);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setEmployeeIdentificationAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_employee_identification_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.employee_identification);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setWheelchairSeatBeltAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_wheelchair_seat_belt_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.wheelchair_seat_belt);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setObjectsForbidenToRoleAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_objects_forbiden_to_role_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.objects_forbiden_to_role);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setVehicleSecurityAccessoriesAdapter() {
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_vehicle_security_accessories_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.vehicle_security_accessories);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setImpedimentToInspectionAdapter() {
        String[] prefixes = new String[] { "SIM", "NAO" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_impediment_to_inspection_menu_popup_item,
            prefixes
        );
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.impediment_to_inspection);
        editTextPrefixDropdown.setAdapter(adapter);
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
        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.inspection_rate);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Snackbar.make(view, "Salvo com Sucesso", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setGoogleDirections();
        //this.setLinesAdapter();
        this.setSpTrans();
        //this.setPrefixesAdapter();
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
}
