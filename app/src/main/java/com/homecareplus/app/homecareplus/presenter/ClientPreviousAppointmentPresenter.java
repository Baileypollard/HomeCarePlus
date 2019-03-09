package com.homecareplus.app.homecareplus.presenter;

import com.homecareplus.app.homecareplus.contract.ClientPreviousAppointmentContract;
import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ClientPreviousAppointmentPresenter implements ClientPreviousAppointmentContract.presenter
{
    private ClientPreviousAppointmentContract.view view;

    @Override
    public void setView(ClientPreviousAppointmentContract.view view)
    {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadPreviousAppointments(final Client client)
    {
        Disposable disposable = CouchbaseRepository.getInstance().loadPreviousAppointmentsForClient(client)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Appointment>>()
                {
                    @Override
                    public void accept(List<Appointment> section)
                    {
                        view.displayAppointments(section);
                    }
                });
    }


}
