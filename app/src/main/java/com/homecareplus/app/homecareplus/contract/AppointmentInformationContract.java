package com.homecareplus.app.homecareplus.contract;

public interface AppointmentInformationContract
{
    interface presenter
    {
        void setView(AppointmentInformationContract.view view);
    }

    interface view
    {
        void setPresenter(AppointmentInformationContract.presenter presenter);
    }
}
