package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.util.Geocoding;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AppointmentMapViewModel extends ViewModel
{
    private MutableLiveData<LatLng> lngLatData;

    public void init(String address, String apiKey)
    {
        if (lngLatData != null)
        {
            return;
        }

        lngLatData = new MutableLiveData<>();
        getAddressInformation(address, apiKey);
    }

    public LiveData<LatLng> getLatLngData()
    {
        return lngLatData;
    }

    private void getAddressInformation(String address, String apiKey)
    {
        Geocoding.getLatLngForAddress(address, apiKey).subscribe(new Observer<LatLng>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(LatLng latLng)
            {
                lngLatData.postValue(latLng);
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onComplete()
            {

            }
        });
    }
}
