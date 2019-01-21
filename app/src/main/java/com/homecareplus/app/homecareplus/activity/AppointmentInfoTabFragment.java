package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentInformationContract;
import com.homecareplus.app.homecareplus.model.Appointment;

public class AppointmentInfoTabFragment extends Fragment implements AppointmentInformationContract.view
{
    private AppointmentInformationContract.presenter presenter;
    private Appointment appointment;

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

    }

    @Override
    public void setPresenter(AppointmentInformationContract.presenter presenter)
    {
        this.presenter = presenter;
    }
}
