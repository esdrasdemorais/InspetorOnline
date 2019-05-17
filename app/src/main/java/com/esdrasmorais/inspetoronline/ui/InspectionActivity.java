package com.esdrasmorais.inspetoronline.ui;

import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.esdrasmorais.inspetoronline.data.GetVolleyResponse;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InspectionActivity extends AppCompatActivity {

    public static final String TAG = "Inspecao";
    private JsonObject json;

    private Location getLocation() {
        SecurityPreferences securityPreferences = new SecurityPreferences(this);

        Location location = new Gson().fromJson(
        securityPreferences.getStoredString("last_know_location"),
        Location.class
    );

        return location;
    }

    private void setGoogleDirections() {
        GoogleDirections googleDirections = new GoogleDirections(
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
//                        Snackbar.make(view, response.getString("message") +
//                         "", Snackbar.LENGTH_LONG)
//                              .setAction("Action", null).show();
                        json = new Gson().fromJson(result, JsonObject.class);
                        setLinesAdapter();
                        //notifyPropertyChanged(BR.json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    private void setLinesAdapter() {
        List<String> prefixes = new ArrayList<String>();

        //json.routes[0].legs[0].steps[1].transit_details.line.name
        //JsonArray routes = json.get("routes").getAsJsonArray();
        //if (!routes.isJsonArray())
          //  throw new IllegalArgumentException("json is not an array");

//        routes.forEach((r) -> {
//            if (r.isJsonObject()) {
//                final JsonObject legs = r.getAsJsonObject();
//                final JsonArray leg = legs.get("legs").getAsJsonArray();
//                leg.forEach((l) -> {
//                    if (l.isJsonObject()) {
//                        final JsonObject steps = l.getAsJsonObject();
//                        final JsonArray step = steps.get("steps").getAsJsonArray();
//                        //prefixes.add(name);
//                    }
//                });
//            }
//        });

        String[] lines = new String[] { "4311-10", "5300-10", "1896-10", "118C-10"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_line_menu_popup_item,
            lines
        );

        AutoCompleteTextView editTextPrefixDropdown =
                findViewById(R.id.line_dropdown);
        editTextPrefixDropdown.setAdapter(adapter);
    }

    private void setPrefixesAdapter() {
        String[] prefixes = new String[] { "10000", "10001", "10002" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getApplicationContext(),
            R.layout.dropdown_prefix_menu_popup_item,
            prefixes
        );

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
        String[] prefixes = new String[] { "ÓTIMO", "BOM", "REGULAR", "RUIM" };
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
        String[] prefixes = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
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
        this.setLinesAdapter();
        this.setPrefixesAdapter();
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
