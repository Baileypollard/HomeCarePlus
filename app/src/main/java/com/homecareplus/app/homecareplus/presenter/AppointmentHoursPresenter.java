package com.homecareplus.app.homecareplus.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
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

public class AppointmentHoursPresenter implements AppointmentHoursContract.presenter
{
    private AppointmentHoursContract.view view;

    @Override
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
                view.showAppointmentStarted(appointment);
            }

            @Override
            public void onCoordinatesFailed()
            {
                view.displayWarningMessage();
            }
        });
    }

    @Override
    public void completeAppointment(final Appointment appointment)
    {
        appointment.setPunchedOutTime(DateUtil.getStartedAppointmentFormat(new Date()));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setTotalTimeSpent(DateUtil.calculateMinutesBetweenDates(appointment.getPunchedInTime(),
                appointment.getPunchedOutTime()));
        appointment.setComment(view.getCommentText());
        appointment.setKmsTravelled(view.getKmText());

        GPSTracker.getInstance().getCurrentLocation(new CoordinatesReceivedCallback()
        {
            @Override
            public void onCoordinatesReceived(LatLng latLng)
            {
                appointment.setPunchedOutLocation(createLatLngMap(latLng));
                saveAppointment(appointment);
                view.showAppointmentCompleted(appointment);

                AppointmentVerification.verifyAppointment(appointment).subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            view.displaySuccessToast();
                        }
                        else
                        {
                            view.displayErrorToast();
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
                view.displayWarningMessage();
            }
        });
    }

    @Override
    public void setView(AppointmentHoursContract.view view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }

    private void saveAppointment(Appointment appointment)
    {
        CouchbaseRepository.getInstance().saveAppointment(appointment);
    }

    private Map<String, Double> createLatLngMap(LatLng latLng)
    {
        Map<String, Double> punchedInLocation = new HashMap<>();
        punchedInLocation.put("lat", latLng.latitude);
        punchedInLocation.put("lng", latLng.longitude);
        return punchedInLocation;
    }
}
