package com.homecareplus.app.homecareplus.contract;

import android.app.Activity;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;

import java.util.List;

import io.reactivex.Observable;

public interface MainAppointmentsContract
{
    interface view
    {
        Activity getActivity();

        void startLoginActivity();;
    }


    interface presenter
    {
        void destroyObservables();

        void logout();

        void fetchAppointments(String employeeId);

        void fetchEmployeeName(String employeeId);
    }
}
