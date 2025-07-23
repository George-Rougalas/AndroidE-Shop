package com.example.store.Activity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.store.R;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * An activity that displays a map and a navigation UI, guiding the user from their current location
 * to a single, given destination.
 */
public class NavigationActivitySingleDestination extends AppCompatActivity {
    
    private GoogleMap myMap;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        sentContentView(R.layout.activity_location2);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    private void sentContentView(int activityLocation2) {
    }

    public void onMapReady(GoogleMap googleMap){

        myMap = googleMap;
        LatLng locate = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(locate).title("Sydeny"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(locate));
    }

}