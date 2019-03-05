package com.homecareplus.app.homecareplus.contract;

import android.view.View;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;

public interface AppointmentRowOnClickListener
{
    void onItemClick(View v, Appointment appointment);

    void onCallClicked(Appointment appointment);

    void onClientInfoClicked(Client client);
}
