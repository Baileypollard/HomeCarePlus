package com.homecareplus.app.homecareplus.contract;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Employee;

public interface MainAppointmentsContract
{
    interface view
    {
        void displayAppointment(Appointment appointment);

        void displayEmployeeInfo(Employee employee);
    }


    interface presenter
    {
        void fetchAppointments();
    }
}
