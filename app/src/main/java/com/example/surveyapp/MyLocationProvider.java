package com.example.surveyapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import com.google.type.LatLng;


public class MyLocationProvider {
    private LatLng current_location;
    MyLocation myLocation;
    public void getUserLastLocation(Context context, MyLocationListener listener)
    {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                if(location!=null)
                {
                    listener.onLocationReceived(location);
                }
            }
        };
        myLocation= new MyLocation(context);
        myLocation.getLocation(locationResult);
    }
    public void onLocationRequestAllowed()
    {
        myLocation.onLocationPermitted();
    }

}
