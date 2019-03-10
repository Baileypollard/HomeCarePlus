package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.util.SharedPreference;

import java.util.List;

public class MainActivityViewModel extends ViewModel
{
    private MutableLiveData<List<AppointmentSectionModel>> listMutableLiveData;
    private MutableLiveData<String> employeeNameData;
    private MutableLiveData<Boolean> logoutData;

    public void init(String employeeId)
    {
        if (listMutableLiveData != null && employeeNameData != null)
        {
            return;
        }
        listMutableLiveData = CouchbaseRepository.getInstance().getAppointmentSectionData();
        employeeNameData = CouchbaseRepository.getInstance().getEmployeeNameData();
        logoutData = new MutableLiveData<>();

        CouchbaseRepository.getInstance().fetchAppointments(employeeId);
        CouchbaseRepository.getInstance().fetchEmployeeName(employeeId);
    }

    public void logout()
    {
        CouchbaseRepository.getInstance().closeDatabase();
        //Clear the shared instances
        SharedPreference.getSharedInstance().clear();
        logoutData.postValue(true);
    }

    public LiveData<Boolean> getLogoutData()
    {
        return logoutData;
    }

    public LiveData<List<AppointmentSectionModel>> getAppointmentSections()
    {
        return listMutableLiveData;
    }

    public LiveData<String> getEmployeeName()
    {
        return employeeNameData;
    }

}
