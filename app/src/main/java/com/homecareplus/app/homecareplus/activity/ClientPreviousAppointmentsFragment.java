package com.homecareplus.app.homecareplus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.PreviousAppointmentAdapter;
import com.homecareplus.app.homecareplus.contract.ClientPreviousAppointmentContract;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;

import java.util.List;

public class ClientPreviousAppointmentsFragment extends Fragment implements ClientPreviousAppointmentContract.view
{
    private ClientPreviousAppointmentContract.presenter presenter;
    private Client client;
    private RecyclerView previousAppointmentsRecyclerView;
    private PreviousAppointmentAdapter appointmentAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ClientInformationActivity activity = (ClientInformationActivity) getActivity();
        if (activity != null)
        {
            this.client = activity.getClient();
        }
        return inflater.inflate(R.layout.fragment_client_previous_apps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        previousAppointmentsRecyclerView = view.findViewById(R.id.previousAppointmentRv);
        appointmentAdapter = new PreviousAppointmentAdapter(getContext());
        previousAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previousAppointmentsRecyclerView.setAdapter(appointmentAdapter);

        presenter.loadPreviousAppointments(client);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void setPresenter(ClientPreviousAppointmentContract.presenter presenter)
    {
        this.presenter = presenter;
    }

    @Override
    public void displayAppointments(List<Appointment> appointmentList)
    {
        appointmentAdapter.setAppointmentList(appointmentList);
    }
}
