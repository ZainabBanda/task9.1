package com.mine.lostandfoundapp2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQ = 100;
    private static final int LOCATION_REQ     = 200;

    private EditText etName, etPhone, etDesc, etDate, etLocation;
    private double selectedLat = 0, selectedLng = 0;
    private FusedLocationProviderClient fused;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // 1) find views
        RadioGroup rgType     = findViewById(R.id.rg_type);
        etName               = findViewById(R.id.et_name);
        etPhone              = findViewById(R.id.et_phone);
        etDesc               = findViewById(R.id.et_description);
        etDate               = findViewById(R.id.et_date);
        etLocation           = findViewById(R.id.et_location);
        Button btnGps        = findViewById(R.id.btn_get_current_location);
        Button btnSave       = findViewById(R.id.btn_save);

        db    = new DBHelper(this);
        fused = LocationServices.getFusedLocationProviderClient(this);

        // 2) date‐picker
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(fmt.format(new Date()));
        etDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(
                AddItemActivity.this,
                (DatePicker dp, int y, int m, int d) -> {
                    Date chosen = new GregorianCalendar(y, m, d).getTime();
                    etDate.setText(fmt.format(chosen));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // 3) Places Autocomplete
        if (!Places.isInitialized()) {
            Places.initialize(
                getApplicationContext(),
                "AIzaSyChyls2NsjDy40eqpBD0NwJNJjh-ktDhAA",
                Locale.getDefault()
            );
        }
        etLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.LAT_LNG
            );
            Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                fields
            ).build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQ);
        });

        // 4) “Get Current Location” → new one-shot GPS fix
        btnGps.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_REQ
                );
            } else {
                fetchCurrentLocation();
            }
        });

        // 5) SAVE
        btnSave.setOnClickListener(v -> {
            String status = rgType.getCheckedRadioButtonId() == R.id.rb_lost
                ? "Lost" : "Found";
            boolean ok = db.insertItem(
                etName.getText().toString().trim(),
                etDesc.getText().toString().trim(),
                etDate.getText().toString(),
                etLocation.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                status,
                selectedLat,
                selectedLng
            );
            Toast.makeText(this,
                ok ? "Item saved" : "Save failed",
                Toast.LENGTH_SHORT
            ).show();
            if (ok) finish();
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchCurrentLocation() {

        LocationRequest req = LocationRequest.create()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setInterval(0)
            .setNumUpdates(1);

        fused.requestLocationUpdates(req, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {

                if (result.getLastLocation() != null) {
                    selectedLat = result.getLastLocation().getLatitude();
                    selectedLng = result.getLastLocation().getLongitude();


                    Geocoder geo = new Geocoder(AddItemActivity.this, Locale.getDefault());
                    try {
                        List<Address> list =
                            geo.getFromLocation(selectedLat, selectedLng, 1);
                        if (!list.isEmpty()) {
                            Address a = list.get(0);
                            String line =
                                (a.getThoroughfare() == null ? "" : a.getThoroughfare())
                                    + (a.getFeatureName() != null ? " " + a.getFeatureName() : "")
                                    + ", " + a.getLocality()
                                    + ", " + a.getCountryName();
                            etLocation.setText(line);
                        } else {
                            etLocation.setText(selectedLat + ", " + selectedLng);
                        }
                    } catch (IOException e) {
                        etLocation.setText(selectedLat + ", " + selectedLng);
                    }
                } else {
                    Toast.makeText(AddItemActivity.this,
                        "Unable to get fresh GPS fix",
                        Toast.LENGTH_SHORT).show();
                }


                fused.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        if (requestCode == LOCATION_REQ
            && grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchCurrentLocation();
        } else {
            Toast.makeText(this,
                "Location permission denied",
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode, int resultCode, Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQ
            && resultCode == RESULT_OK
            && data != null) {
            Place p = Autocomplete.getPlaceFromIntent(data);
            etLocation.setText(p.getName());
            if (p.getLatLng() != null) {
                selectedLat = p.getLatLng().latitude;
                selectedLng = p.getLatLng().longitude;
            }
        }
    }
}
