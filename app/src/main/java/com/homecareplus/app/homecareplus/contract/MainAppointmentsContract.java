package com.homecareplus.app.homecareplus.contract;

import android.app.Activity;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Employee;

public interface MainAppointmentsContract
{
    interface view
    {
        Activity getActivity();

        void startLoginActivity();

        void displayAppointment(Appointment appointment);

        void displayEmployeeName(String name);
    }


    interface presenter
    {
        void logout();

        void fetchAppointments();

        void fetchEmployeeName(String employeeId);
    }
}
