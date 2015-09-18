package com.brainscode.nearcommunication;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by platerosanchezm on 17/09/15.
 */

public class SomeFragment extends Fragment {

    MapView mapView;
    GoogleMap map;
    //TODO: put default STYLIGHT
    LatLng coords = new LatLng(48.1506177,11.5470593);

    @SuppressLint("ValidFragment")
    public SomeFragment(double Lat, double Long) {
        this.coords = new LatLng(Lat, Long);
    }

    public SomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_places, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 15);
        map.animateCamera(cameraUpdate);


        Button people = (Button) v.findViewById(R.id.people);
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindPeople newFragment = new FindPeople();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(android.R.id.content, newFragment);
                transaction.commit();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
