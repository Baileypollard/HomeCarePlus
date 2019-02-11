package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
import com.homecareplus.app.homecareplus.model.Appointment;

public class AppointmentHoursTabFragment extends Fragment implements AppointmentHoursContract.view
{
    private AppointmentHoursContract.presenter presenter;
    private Appointment appointment;
    private TextView appointmentStartTimeTextView;
    private TextView appointmentEndTimeTextView;
    private TextView totalAppointmentTimeTextView;
    private EditText kmTravelledEditText;
    private EditText commentsEditText;
    private Button startAppointmentButton;
    private Button completeAppointmentButton;
    private View inProgressLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppointmentActivity activity = (AppointmentActivity) getActivity();
        if (activity != null)
        {
            this.appointment = activity.getAppointment();
        }
        return inflater.inflate(R.layout.fragment_appointment_hours, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        appointmentEndTimeTextView = view.findViewById(R.id.endDateTextView);
        appointmentStartTimeTextView = view.findViewById(R.id.startDateTextView);
        totalAppointmentTimeTextView = view.findViewById(R.id.totalTimeTextView);
        kmTravelledEditText = view.findViewById(R.id.kmEditText);
        commentsEditText = view.findViewById(R.id.commentEditText);
        startAppointmentButton = view.findViewById(R.id.startAppButton);
        completeAppointmentButton = view.findViewById(R.id.completeAppButton);
        inProgressLayout = view.findViewById(R.id.inProgressLayout);

        startAppointmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                presenter.startAppointment(appointment);
            }
        });

        completeAppointmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                presenter.completeAppointment(appointment);
            }
        });
        appointmentStartTimeTextView.setText(appointment.getPunchedInTime());
        appointmentEndTimeTextView.setText(appointment.getPunchedOutTime());
        totalAppointmentTimeTextView.setText(appointment.getTotalTimeSpent());

        configureView();
    }

    @Override
    public String getCommentText()
    {
        return commentsEditText.getText().toString();
    }

    @Override
    public String getKmText()
    {
        return kmTravelledEditText.getText().toString();
    }

    private void configureView()
    {
        switch (appointment.getStatus())
        {
            case NEW:
                configureAppointmentNew();
                break;
            case IN_PROGRESS:
                configureAppointmentInProgress();
                break;
            case COMPLETED:
                configureAppointmentCompleted();
                break;
            default:
                configureAppointmentNew();
        }
    }

    private void configureAppointmentInProgress()
    {
        kmTravelledEditText.setEnabled(true);
        commentsEditText.setEnabled(true);

        startAppointmentButton.setEnabled(false);
        completeAppointmentButton.setEnabled(true);
    }

    private void configureAppointmentNew()
    {
        kmTravelledEditText.setEnabled(false);
        commentsEditText.setEnabled(false);

        startAppointmentButton.setEnabled(true);
        completeAppointmentButton.setEnabled(false);
    }

    private void configureAppointmentCompleted()
    {
        kmTravelledEditText.setEnabled(false);
        commentsEditText.setEnabled(false);

        commentsEditText.setText(appointment.getComment());
        kmTravelledEditText.setText(appointment.getKmsTravelled());

        kmTravelledEditText.setEnabled(false);
        commentsEditText.setEnabled(false);
        startAppointmentButton.setEnabled(false);
        completeAppointmentButton.setEnabled(false);
    }

    @Override
    public void showAppointmentStarted(Appointment appointment)
    {
        appointmentStartTimeTextView.setText(appointment.getPunchedInTime());
        configureAppointmentInProgress();
    }

    @Override
    public void showAppointmentCompleted(Appointment appointment)
    {
        appointmentEndTimeTextView.setText(appointment.getPunchedOutTime());
        totalAppointmentTimeTextView.setText(appointment.getTotalTimeSpent());
        configureAppointmentCompleted();
    }

    @Override
    public void setPresenter(AppointmentHoursContract.presenter presenter)
    {
        this.presenter = presenter;
    }
}