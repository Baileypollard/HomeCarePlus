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
    private boolean isScanning;

    private GPSTracker(Context context)
    {
        this.isScanning = false;
        this.context_ = context;
        this.locationManager = (LocationManager) context_.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationScanning(Activity activity)
    {
        if (isScanning)
            return;

        if (ActivityCompat.checkSelfPermission(context_, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 30, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    isScanning = true;
                    Log.d("TAG", "CHANGED: " + location.getLatitude() + " : " + location.getLongitude());
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
                    isScanning = false;
                }
            });
        }
        else
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }
    }

    public void getCurrentLocation(final CoordinatesReceivedCallback callback)
    {
        if (ActivityCompat.checkSelfPermission(context_, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null)
            {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                callback.onCoordinatesReceived(latLng);
            }
            else
            {
                callback.onCoordinatesFailed();
            }

        }
        else
        {
            callback.onCoordinatesFailed();
        }
    }

    public static GPSTracker getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new GPSTracker(context);
        }
        return instance;
    }
}
