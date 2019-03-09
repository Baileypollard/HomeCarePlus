package com.homecareplus.app.homecareplus.presenter;

import android.util.DisplayMetrics;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.homecareplus.app.homecareplus.contract.MainAppointmentsContract;
import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.util.SharedPreference;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainAppointmentPresenter implements MainAppointmentsContract.presenter
{
    private MainAppointmentsContract.view view;
    private Disposable appointmentDisposable;
    private Disposable employeeNameDisposable;

    public MainAppointmentPresenter(MainAppointmentsContract.view view)
    {
        this.view = view;
    }

    @Override
    public void fetchAppointments(String employeeId)
    {
        appointmentDisposable = CouchbaseRepository.getInstance().fetchAppointments(employeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AppointmentSectionModel>()
                {
                    @Override
                    public void accept(AppointmentSectionModel section)
                    {
                        view.displayAppointmentSection(section);
                    }
                });
    }

    @Override
    public void fetchEmployeeName(String employeeId)
    {
        employeeNameDisposable = CouchbaseRepository.getInstance().fetchEmployeeName(employeeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>()
                {
                    @Override
                    public void accept(String fullName)
                    {
                        view.displayEmployeeName(fullName);
                    }
                });
    }

    @Override
    public void destroyObservables()
    {
        Log.d("TAG", "Destroyed");
        employeeNameDisposable.dispose();
        appointmentDisposable.dispose();
    }

    @Override
    public void logout()
    {
        CouchbaseRepository.getInstance().closeDatabase();
        //Clear the shared instances
        SharedPreference.getSharedInstance(view.getActivity()).clear();
        view.startLoginActivity();
    }
}
