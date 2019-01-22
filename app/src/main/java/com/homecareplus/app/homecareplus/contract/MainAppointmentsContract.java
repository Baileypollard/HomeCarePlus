package com.homecareplus.app.homecareplus.contract;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Employee;

public interface MainAppointmentsContract
{
    interface view
    {
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
