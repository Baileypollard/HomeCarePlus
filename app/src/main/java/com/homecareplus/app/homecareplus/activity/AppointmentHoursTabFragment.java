package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentHoursContract;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.viewmodel.AppointmentHoursViewModel;

public class AppointmentHoursTabFragment extends Fragment
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
    private AppointmentHoursViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(AppointmentHoursViewModel.class);
        viewModel.init();

        viewModel.getAppointmentData().observe(this, new Observer<Appointment>()
        {
            @Override
            public void onChanged(Appointment appointment)
            {
                if (appointment.isInProgress())
                {
                    configureAppointmentInProgress();
                }
                else if (appointment.isCompleted())
                {
                    configureAppointmentCompleted();
                }

            }
        });

        viewModel.coordinatesFailedData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean failed)
            {
                if (failed)
                {
                    displayWarningMessage();
                }
            }
        });

        viewModel.getVerificationData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean verified)
            {
                if (verified)
                {
                    displaySuccessToast();
                }
                else
                {
                    displayErrorToast();
                }
            }
        });

        appointmentEndTimeTextView = view.findViewById(R.id.endDateTextView);
        appointmentStartTimeTextView = view.findViewById(R.id.startDateTextView);
        totalAppointmentTimeTextView = view.findViewById(R.id.totalTimeTextView);
        kmTravelledEditText = view.findViewById(R.id.kmEditText);
        commentsEditText = view.findViewById(R.id.commentEditText);
        startAppointmentButton = view.findViewById(R.id.startAppButton);
        completeAppointmentButton = view.findViewById(R.id.completeAppButton);

        startAppointmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.startAppointment(appointment);
            }
        });

        completeAppointmentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.completeAppointment(appointment, getCommentText(), getKmText());
            }
        });

        appointmentStartTimeTextView.setText(appointment.getPunchedInTime());
        appointmentEndTimeTextView.setText(appointment.getPunchedOutTime());
        totalAppointmentTimeTextView.setText(appointment.getTotalTimeSpent());

        configureView();
    }

    public String getCommentText()
    {
        return commentsEditText.getText().toString();
    }

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
            case DECLINED:
            case VERIFIED:
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

    private void displayWarningMessage()
    {
        Toast.makeText(getActivity(), "You must have location services enabled, please do so in the application settings.",
                Toast.LENGTH_LONG).show();
    }

    private void displaySuccessToast()
    {
        Toast.makeText(getContext(), "Your appointment has been completed successfully", Toast.LENGTH_SHORT).show();
    }

    private void displayErrorToast()
    {
        Toast.makeText(getContext(), "There has been an error completing your appointment", Toast.LENGTH_LONG).show();
    }

    private void showAppointmentStarted(Appointment appointment)
    {
        appointmentStartTimeTextView.setText(appointment.getPunchedInTime());
        configureAppointmentInProgress();
    }

    private void showAppointmentCompleted(Appointment appointment)
    {
        appointmentEndTimeTextView.setText(appointment.getPunchedOutTime());
        totalAppointmentTimeTextView.setText(appointment.getTotalTimeSpent());
        configureAppointmentCompleted();
    }
}
