package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

public class PreviousAppointmentViewModel extends ViewModel
{
    MutableLiveData<List<Appointment>> appointmentData;

    public void init(Client client)
    {
        if (appointmentData != null)
        {
            return;
        }
        CouchbaseRepository.getInstance().fetchPreviousAppointmentsForClient(client);
        appointmentData = CouchbaseRepository.getInstance().getPreviousAppointmentData();
    }

    public LiveData<List<Appointment>> getAppointmentData()
    {
        return appointmentData;
    }
}
