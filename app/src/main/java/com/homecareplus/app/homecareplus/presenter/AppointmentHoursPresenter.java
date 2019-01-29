package com.homecareplus.app.homecareplus.presenter;

import android.util.Log;

import com.couchbase.lite.ConcurrencyControl;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDictionary;
import com.couchbase.lite.MutableDocument;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.DateUtil;

import java.util.Date;

public class AppointmentHoursPresenter implements AppointmentHoursContract.presenter
{
    private AppointmentHoursContract.view view;

    @Override
    public void startAppointment(Appointment appointment)
    {
        appointment.setPunchedInTime(DateUtil.getStartedAppointmentFormat(new Date()));
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        saveAppointment(appointment);
        view.displayPunchedInTime(appointment);
    }

    @Override
    public void completeAppointment(Appointment appointment)
    {
        appointment.setPunchedOutTime(DateUtil.getStartedAppointmentFormat(new Date()));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        saveAppointment(appointment);
        view.displayPunchedOutTime(appointment);
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
        }
        catch (CouchbaseLiteException e)
        {
            Log.d("TAG", "COUCHBASE EXCEPTION: " + e);
        }
    }
}
