package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.Appointment;

public class AppointmentInfoViewModel extends ViewModel
{
    private MutableLiveData<Appointment> appointmentData;

    public void init(Appointment appointment)
    {
        appointmentData = CouchbaseRepository.getInstance().getAppointmentData(appointment);
    }

    public LiveData<Appointment> getAppointmentData()
    {
        return appointmentData;
    }
}
