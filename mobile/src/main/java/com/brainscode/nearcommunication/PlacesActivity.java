package com.brainscode.nearcommunication;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PlacesActivity extends Activity {

    protected LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1,
                (float) 1, mLocationListener);

        double latitude = 48.1506177;
        double longitude = 11.5470593;
        if (mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            latitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            longitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        }



        String coordinates = Double.toString(latitude) +","+ Double.toString(longitude);
        new FoursquareLocations(coordinates).execute();

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
