package com.homecareplus.app.homecareplus.contract;

import com.homecareplus.app.homecareplus.model.Appointment;

public interface AppointmentHoursContract
{
    interface presenter
    {
        void startAppointment(Appointment appointment);

        void completeAppointment(Appointment appointment);

        void setView(AppointmentHoursContract.view view);
    }

    interface view
    {
        void displayPunchedInTime(Appointment appointment);

        void displayPunchedOutTime(Appointment appointment);

        void setPresenter(AppointmentHoursContract.presenter presenter);
    }
}
