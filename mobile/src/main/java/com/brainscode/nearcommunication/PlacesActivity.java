package com.brainscode.nearcommunication;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class PlacesActivity extends Activity {

    protected LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places);
        Log.i("HELLO", "FRAGMENT OU YEAH");

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1,
                (float) 1, mLocationListener);

        double latitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        double longitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

        String coordinates = Double.toString(latitude) +","+ Double.toString(longitude);
        Log.i("JSON", "COORDINATES: " + coordinates);

        new GetChildList(coordinates).execute();

        SomeFragment newFragment = new SomeFragment(latitude, longitude);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, newFragment);
        transaction.commit();


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}
