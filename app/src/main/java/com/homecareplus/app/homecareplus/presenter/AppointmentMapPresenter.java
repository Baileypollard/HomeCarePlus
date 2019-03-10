package com.homecareplus.app.homecareplus.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.contract.AppointmentMapContract;
import com.homecareplus.app.homecareplus.util.Geocoding;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AppointmentMapPresenter implements AppointmentMapContract.presenter
{
    private AppointmentMapContract.view view;

    @Override
    public void setView(AppointmentMapContract.view view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getAddressInformation(String address, String apiKey)
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
                view.displayMarkerOnMaps(latLng);
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
