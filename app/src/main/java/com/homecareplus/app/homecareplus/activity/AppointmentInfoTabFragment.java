package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.viewmodel.AppointmentInfoViewModel;

public class AppointmentInfoTabFragment extends Fragment
{
    private AppointmentInfoViewModel  viewModel;
    private Appointment appointment;
    private TextView clientNameTextView;
    private TextView clientAddressTextView;
    private TextView appointmentTimeTextView;
    private TextView appointmentInfoTextView;
    private TextView commentsTextView;
    private TextView appointmentStatusTextView;
    private TextView clientGenderTextView;
    private TextView clientPhoneNumberTextView;

    private View commentsView;

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
        viewModel = ViewModelProviders.of(this).get(AppointmentInfoViewModel.class);
        viewModel.init(appointment);

        clientNameTextView = view.findViewById(R.id.clientNameTextView);
        clientAddressTextView = view.findViewById(R.id.clientAddressTextView);
        appointmentTimeTextView = view.findViewById(R.id.scheduledTimeTextView);
        appointmentInfoTextView = view.findViewById(R.id.appointmentDescriptonTextView);
        commentsTextView = view.findViewById(R.id.appointmentCommentTextView);
        appointmentStatusTextView = view.findViewById(R.id.appointmentStatusTextView);
        clientPhoneNumberTextView = view.findViewById(R.id.clientPhoneNumberTextView);
        clientGenderTextView = view.findViewById(R.id.clientGenderTextView);
        commentsView = view.findViewById(R.id.commentsLayout);
        viewModel.getAppointmentData().observe(this, new Observer<Appointment>()
        {
            @Override
            public void onChanged(@Nullable Appointment appointment)
            {
                displayAppointmentInfo(appointment);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void displayAppointmentInfo(Appointment appointment)
    {
        clientNameTextView.setText(appointment.getClientName());
        clientAddressTextView.setText(appointment.getAddress());
        appointmentTimeTextView.setText(appointment.getAppointmentTime());
        appointmentInfoTextView.setText(appointment.getAppointmentInfo());
        appointmentStatusTextView.setText(appointment.getStatus().getValue());
        clientGenderTextView.setText(appointment.getClientGender());
        clientPhoneNumberTextView.setText(appointment.getClientPhoneNumber());

        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
        {
            commentsView.setVisibility(View.VISIBLE);
            commentsTextView.setText(appointment.getComment());
        }
    }

}
