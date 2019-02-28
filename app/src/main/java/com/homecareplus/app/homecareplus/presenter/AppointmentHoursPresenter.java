package com.homecareplus.app.homecareplus.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDictionary;
import com.couchbase.lite.MutableDocument;
import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.AppointmentVerification;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.util.GPSTracker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

                AppointmentVerification.VerifyAppointment verify = new AppointmentVerification.VerifyAppointment(appointment);
                if (verify.getStatus() != AsyncTask.Status.RUNNING)
                {
                    verify.execute();
                }
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
        Database database = DatabaseManager.getDatabase();

        MutableDocument doc = database.getDocument(appointment.getEmployeeId() + "." + appointment.getDate()).toMutable();

        MutableArray array = doc.getArray("schedule").toMutable();

        for (int i = 0; i < array.count(); i++)
        {
            MutableDictionary dict = array.getDictionary(i).toMutable();
            String appointmentId = dict.getString("appointment_id");
            if (appointmentId.equals(appointment.getId()))
            {
                MutableDictionary punchedInDict = new MutableDictionary();
                if (appointment.getPunchedInLocation() != null)
                {
                    Double lat = appointment.getPunchedInLocation().get("lat") != null ? appointment.getPunchedInLocation().get("lat") : 0.0;
                    Double lng = appointment.getPunchedInLocation().get("lat") != null ? appointment.getPunchedInLocation().get("lng") : 0.0;
                    punchedInDict.setDouble("lat", lat);
                    punchedInDict.setDouble("lng", lng);
                    dict.setDictionary("punched_in_loc", punchedInDict);
                }

                MutableDictionary punchedOutDict = new MutableDictionary();
                if (appointment.getPunchedInLocation() != null)
                {
                    Double lat = appointment.getPunchedOutLocation().get("lat") != null ? appointment.getPunchedOutLocation().get("lat") : 0.0;
                    Double lng = appointment.getPunchedOutLocation().get("lat") != null ? appointment.getPunchedOutLocation().get("lng") : 0.0;

                    punchedOutDict.setDouble("lat", lat);
                    punchedOutDict.setDouble("lng", lng);
                    dict.setDictionary("punched_out_loc", punchedOutDict);
                }

                dict.setString("punched_in_time", appointment.getPunchedInTime());
                dict.setString("punched_out_time", appointment.getPunchedOutTime());
                dict.setString("status", appointment.getStatus().getValue());
                dict.setString("comment", appointment.getComment());
                dict.setString("kms_travelled", appointment.getKmsTravelled());
                array.setDictionary(i, dict);
            }
        }
        doc.setArray("schedule", array);

        try
        {
            database.save(doc);
        } catch (CouchbaseLiteException e)
        {
            Log.d("TAG", "COUCHBASE EXCEPTION: " + e);
        }
    }

    private Map<String, Double> createLatLngMap(LatLng latLng)
    {
        Map<String, Double> punchedInLocation = new HashMap<>();
        punchedInLocation.put("lat", latLng.latitude);
        punchedInLocation.put("lng", latLng.longitude);
        return punchedInLocation;
    }
}
