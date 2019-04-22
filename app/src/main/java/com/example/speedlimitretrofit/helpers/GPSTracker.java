package com.example.speedlimitretrofit.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GPSTracker implements LocationListener {
    Context context;
    Location tempLoc;

    public GPSTracker(Context c){
        context =c;
    }

    // returns users current Location object
    public Location getLocation(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "GPS is not granted",Toast.LENGTH_LONG).show();
        }
        LocationManager LocMa= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = LocMa.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            //6000 is minimum millisecond
            //10 min distance
            // play with that to optimize our app
            LocMa.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            tempLoc= LocMa.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }else{
            Toast.makeText(context,"Please enable the GPS", Toast.LENGTH_LONG).show();

        }
        return tempLoc;
    }
    @Override
    public void onLocationChanged(Location location) {

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
}
