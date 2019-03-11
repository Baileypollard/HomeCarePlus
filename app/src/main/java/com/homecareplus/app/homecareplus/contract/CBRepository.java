package com.homecareplus.app.homecareplus.contract;

import android.arch.lifecycle.MutableLiveData;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

public interface CBRepository
{
    void saveAppointment(Appointment appointment);

    void fetchEmployeeName(String employeeId);

    void fetchAppointments(String employeeId);

    void fetchPreviousAppointmentsForClient(final Client client);

    void closeDatabase();

    MutableLiveData<List<AppointmentSectionModel>> getAppointmentSectionData();

    MutableLiveData<String> getEmployeeNameData();

    MutableLiveData<List<Appointment>> getPreviousAppointmentData();

    MutableLiveData<Appointment> getAppointmentData(Appointment appointment);
}
