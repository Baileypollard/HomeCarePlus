package com.homecareplus.app.homecareplus.contract;

import com.google.android.gms.maps.model.LatLng;

public interface AppointmentMapContract
{
    interface presenter
    {
        void setView(AppointmentMapContract.view view);

        void getAddressInformation(String address, String apiKey);
    }


    interface view
    {
        void displayMarkerOnMaps(LatLng latLng);

        void setPresenter(AppointmentMapContract.presenter presenter);
    }
}
