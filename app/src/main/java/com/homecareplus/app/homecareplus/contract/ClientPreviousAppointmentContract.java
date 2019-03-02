package com.homecareplus.app.homecareplus.contract;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

public interface ClientPreviousAppointmentContract
{
    interface view
    {
        void setPresenter(ClientPreviousAppointmentContract.presenter presenter);

        void displayAppointments(List<Appointment> appointmentList);
    }

    interface presenter
    {
        void setView(ClientPreviousAppointmentContract.view view);

        void loadPreviousAppointments(Client client);
    }
}
