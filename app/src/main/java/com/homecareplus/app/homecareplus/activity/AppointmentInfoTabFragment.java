package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentInformationContract;
import com.homecareplus.app.homecareplus.model.Appointment;

public class AppointmentInfoTabFragment extends Fragment implements AppointmentInformationContract.view
{
    private AppointmentInformationContract.presenter presenter;
    private Appointment appointment;
    private TextView clientNameTextView;
    private TextView clientAddressTextView;
    private TextView appointmentTimeTextView;
    private TextView appointmentInfoTextView;
    private TextView commentsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppointmentActivity activity = (AppointmentActivity) getActivity();
        if (activity != null)
        {
            this.appointment = activity.getAppointment();
        }
        return inflater.inflate(R.layout.fragment_appointment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        clientNameTextView = view.findViewById(R.id.clientNameTextView);
        clientAddressTextView = view.findViewById(R.id.clientAddressTextView);
        appointmentTimeTextView = view.findViewById(R.id.scheduledTimeTextView);
        appointmentInfoTextView = view.findViewById(R.id.appointmentDescriptonTextView);
        commentsTextView = view.findViewById(R.id.appointmentCommentTextView);

        displayAppointmentInfo();
    }

    @Override
    public void setPresenter(AppointmentInformationContract.presenter presenter)
    {
        this.presenter = presenter;
    }

    public void displayAppointmentInfo()
    {
        clientNameTextView.setText(appointment.getClientName());
        clientAddressTextView.setText(appointment.getAddress());
        appointmentTimeTextView.setText(appointment.getAppointmentTime());
        appointmentInfoTextView.setText(appointment.getAppointmentInfo());
        commentsTextView.setText(appointment.getComment());
    }
}
