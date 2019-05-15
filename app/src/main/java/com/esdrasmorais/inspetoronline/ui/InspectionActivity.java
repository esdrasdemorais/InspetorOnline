package com.esdrasmorais.inspetoronline.ui;

 import android.location.Location;
 import android.os.Bundle;
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

public class InspectionActivity extends AppCompatActivity {

    public static final String TAG = "InspectionActivity";

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SecurityPreferences securityPreferences = new SecurityPreferences(this);

        Location location = new Gson().fromJson(
            securityPreferences.getStoredString("last_know_location"),
            Location.class
        );

        GoogleDirections googleDirections = new GoogleDirections(
            this.getApplicationContext(),
            location
        );
        //googleDirections.getResponsibleBody();

        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.dropdown_prefix_menu_popup_item,
                COUNTRIES);

        AutoCompleteTextView editTextPrefixDropdown =
            findViewById(R.id.prefix_dropdown);
        editTextPrefixDropdown .setAdapter(adapter);
    }
}
