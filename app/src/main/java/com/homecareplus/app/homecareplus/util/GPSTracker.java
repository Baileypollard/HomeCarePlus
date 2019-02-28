package com.homecareplus.app.homecareplus.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;

public class GPSTracker
{
    private static GPSTracker instance;
    private Context context_;
    private LocationManager locationManager;

    private GPSTracker(Activity activity)
    {
        this.context_ = activity.getApplicationContext();
        this.locationManager = (LocationManager) context_.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context_, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }
    }

    public void getCurrentLocation(final CoordinatesReceivedCallback callback)
    {
        if (ActivityCompat.checkSelfPermission(context_, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    callback.onCoordinatesReceived(new LatLng(location.getLatitude(), location.getLongitude()));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras)
                {

                }

                @Override
                public void onProviderEnabled(String provider)
                {

                }

                @Override
                public void onProviderDisabled(String provider)
                {

                }
            }, null);
        }
        else
        {
            callback.onCoordinatesFailed();
        }
    }

    public static void init(Activity activity)
    {
        if (instance == null)
        {
            instance = new GPSTracker(activity);
        }
    }

    public static GPSTracker getInstance()
    {
        return instance;
    }
}
