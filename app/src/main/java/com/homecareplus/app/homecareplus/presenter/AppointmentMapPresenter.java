package com.homecareplus.app.homecareplus.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;
import com.homecareplus.app.homecareplus.contract.AppointmentMapContract;
import com.homecareplus.app.homecareplus.util.Geocoding;

public class AppointmentMapPresenter implements AppointmentMapContract.presenter
{
    private AppointmentMapContract.view view;

    @Override
    public void setView(AppointmentMapContract.view view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getAddressInformation(String address, String apiKey)
    {
        new Geocoding.getLatAndLngForAddress(address, apiKey, new CoordinatesReceivedCallback()
        {
            @Override
            public void onCoordinatesReceived(LatLng latLng)
            {
                view.displayMarkerOnMaps(latLng);
            }
        }).execute();
    }
}
