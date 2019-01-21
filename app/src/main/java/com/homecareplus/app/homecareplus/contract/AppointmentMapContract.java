package com.homecareplus.app.homecareplus.contract;

import com.homecareplus.app.homecareplus.model.Appointment;

public interface AppointmentMapContract
{
    interface presenter
    {
        void setView(AppointmentMapContract.view view);

        void getAddressInformation(Appointment appointment);
    }


    interface view
    {
        void displayMarkerOnMaps(double lat, double lng);

        void setPresenter(AppointmentMapContract.presenter presenter);
    }
}
