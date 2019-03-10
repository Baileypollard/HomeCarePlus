package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.PreviousAppointmentAdapter;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.viewmodel.PreviousAppointmentViewModel;

import java.util.List;

public class ClientPreviousAppointmentsFragment extends Fragment
{
    private Client client;
    private RecyclerView previousAppointmentsRecyclerView;
    private PreviousAppointmentAdapter appointmentAdapter;
    private PreviousAppointmentViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(PreviousAppointmentViewModel.class);
        viewModel.init(client);

        viewModel.getAppointmentData().observe(this, new Observer<List<Appointment>>()
        {
            @Override
            public void onChanged(@Nullable List<Appointment> appointmentList)
            {
                appointmentAdapter.setAppointmentList(appointmentList);
            }
        });

        previousAppointmentsRecyclerView = view.findViewById(R.id.previousAppointmentRv);
        appointmentAdapter = new PreviousAppointmentAdapter(getContext());
        previousAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previousAppointmentsRecyclerView.setAdapter(appointmentAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(previousAppointmentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.cell_divider));
        previousAppointmentsRecyclerView.addItemDecoration(divider);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

}
