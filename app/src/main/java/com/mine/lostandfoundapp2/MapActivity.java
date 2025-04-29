package com.mine.lostandfoundapp2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Google Maps APIs
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // initialize your DB helper
        db = new DBHelper(this);

        // grab the SupportMapFragment from the layout and request its async callback
        SupportMapFragment mapFrag = (SupportMapFragment)
            getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        List<Item> items = db.getAllItems();
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (Item it : items) {
            // assume your Item has getLatitude() / getLongitude()
            LatLng pos = new LatLng(it.getLatitude(), it.getLongitude());
            map.addMarker(new MarkerOptions()
                .position(pos)
                .title(it.getName())
                .snippet(it.getStatus()));
            bounds.include(pos);
        }

        // zoom so that all markers fit into view, if there are any
        if (!items.isEmpty()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngBounds(bounds.build(), 100)
            );
        }
    }
}
