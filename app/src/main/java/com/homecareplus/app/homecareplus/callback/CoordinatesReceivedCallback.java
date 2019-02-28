package com.homecareplus.app.homecareplus.callback;

import com.google.android.gms.maps.model.LatLng;

public interface CoordinatesReceivedCallback
{
    void onCoordinatesReceived(LatLng latLng);

    void onCoordinatesFailed();
}
