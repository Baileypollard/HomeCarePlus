package com.homecareplus.app.homecareplus.presenter;

import com.homecareplus.app.homecareplus.contract.AppointmentMapContract;
import com.homecareplus.app.homecareplus.model.Appointment;

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
    public void getAddressInformation(Appointment appointment)
    {
        view.displayMarkerOnMaps(45.616953, -61.362123);
    }
}
