package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.model.Client;

public class ClientInformationViewModel extends ViewModel
{
    private MutableLiveData<Client> clientData;

    public void init(Client client)
    {
        clientData = CouchbaseRepository.getInstance().getClientData(client);
        CouchbaseRepository.getInstance().fetchClientInformation(client);
    }


    public LiveData<Client> getClientData()
    {
        return clientData;
    }
}
