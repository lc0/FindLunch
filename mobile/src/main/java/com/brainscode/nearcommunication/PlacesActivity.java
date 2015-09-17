package com.brainscode.nearcommunication;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PlacesActivity extends Activity {

//    protected LocationManager mLocationManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_places);
        Log.i("HELLO", "FRAGMENT OU YEAH");

//        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
//                LOCATION_REFRESH_DISTANCE, mLocationListener);

        new GetChildList().execute();
    }
}
