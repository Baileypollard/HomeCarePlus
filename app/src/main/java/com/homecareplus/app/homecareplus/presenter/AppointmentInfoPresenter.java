package com.homecareplus.app.homecareplus.presenter;

import com.homecareplus.app.homecareplus.contract.AppointmentInformationContract;

public class AppointmentInfoPresenter implements AppointmentInformationContract.presenter
{
    private AppointmentInformationContract.view view;

    @Override
    public void setView(AppointmentInformationContract.view view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }
}
