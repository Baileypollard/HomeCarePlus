package com.homecareplus.app.homecareplus.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;
import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.AppointmentVerification;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.util.GPSTracker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AppointmentHoursViewModel extends ViewModel
{
    private MutableLiveData<Appointment> appointmentData;
    private MutableLiveData<Boolean> coordinatesFailedData;
    private MutableLiveData<Boolean> verificationData;

    public void init(Appointment appointment)
    {
        appointmentData = CouchbaseRepository.getInstance().getAppointmentData(appointment);
        coordinatesFailedData = new MutableLiveData<>();
        verificationData = new MutableLiveData<>();
    }

    public void completeAppointment(final Appointment appointment, String comment, String km)
    {
        appointment.setPunchedOutTime(DateUtil.getStartedAppointmentFormat(new Date()));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setTotalTimeSpent(DateUtil.calculateMinutesBetweenDates(appointment.getPunchedInTime(),
                appointment.getPunchedOutTime()));
        appointment.setComment(comment);
        appointment.setKmsTravelled(km);

        GPSTracker.getInstance().getCurrentLocation(new CoordinatesReceivedCallback()
        {
            @Override
            public void onCoordinatesReceived(LatLng latLng)
            {
                appointment.setPunchedOutLocation(createLatLngMap(latLng));
                saveAppointment(appointment);

                AppointmentVerification.verifyAppointment(appointment).subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(Boolean success)
                    {
                        if (success)
                        {
                            verificationData.postValue(true);
                        }
                        else
                        {
                            verificationData.postValue(false);
                        }
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

            @Override
            public void onCoordinatesFailed()
            {
                coordinatesFailedData.postValue(true);
            }
        });
    }

    public void startAppointment(final Appointment appointment)
    {
        appointment.setPunchedInTime(DateUtil.getStartedAppointmentFormat(new Date()));
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);

        GPSTracker.getInstance().getCurrentLocation(new CoordinatesReceivedCallback()
        {
            @Override
            public void onCoordinatesReceived(LatLng latLng)
            {
                appointment.setPunchedInLocation(createLatLngMap(latLng));
                saveAppointment(appointment);
            }

            @Override
            public void onCoordinatesFailed()
            {
                coordinatesFailedData.postValue(true);
            }
        });
    }

    public LiveData<Boolean> getVerificationData()
    {
        return verificationData;
    }

    public LiveData<Boolean> coordinatesFailedData()
    {
        return coordinatesFailedData;
    }

    public LiveData<Appointment> getAppointmentData()
    {
        return appointmentData;
    }

    private Map<String, Double> createLatLngMap(LatLng latLng)
    {
        Map<String, Double> punchedInLocation = new HashMap<>();
        punchedInLocation.put("lat", latLng.latitude);
        punchedInLocation.put("lng", latLng.longitude);
        return punchedInLocation;
    }

    private void saveAppointment(Appointment appointment)
    {
        CouchbaseRepository.getInstance().saveAppointment(appointment);
    }
}
