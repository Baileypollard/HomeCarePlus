package com.homecareplus.app.homecareplus.contract;

import com.couchbase.lite.Database;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

import io.reactivex.Flowable;

public interface CBRepository
{
    void saveAppointment(Appointment appointment);

    Flowable<String> fetchEmployeeName(String employeeId);

    Flowable<AppointmentSectionModel> fetchAppointments(String employeeId);

    Flowable<List<Appointment>> loadPreviousAppointmentsForClient(final Client client);

    void closeDatabase();
}
