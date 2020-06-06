package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.adapter.PreviousAppointmentAdapter;
import com.homecareplus.app.homecareplus.contract.PrevAppointmentOnClickListener;
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
    private TextView noPrevAppointmentTextView;

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
            public void onChanged(List<Appointment> appointmentList)
            {
                if (!appointmentList.isEmpty()) {
                    noPrevAppointmentTextView.setVisibility(View.GONE);
                    previousAppointmentsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    noPrevAppointmentTextView.setVisibility(View.VISIBLE);
                    previousAppointmentsRecyclerView.setVisibility(View.GONE);
                }

                appointmentAdapter.setAppointmentList(appointmentList);
            }
        });

        previousAppointmentsRecyclerView = view.findViewById(R.id.previousAppointmentRv);
        appointmentAdapter = new PreviousAppointmentAdapter(getContext(), new PrevAppointmentOnClickListener() {
            @Override
            public void onItemClick(View v, Appointment appointment) {
                displayAppointmentComment(appointment, v);
            }
        });

        previousAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        previousAppointmentsRecyclerView.setAdapter(appointmentAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(previousAppointmentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.cell_divider));
        previousAppointmentsRecyclerView.addItemDecoration(divider);


        noPrevAppointmentTextView = view.findViewById(R.id.noPrevAppointmentsTextView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    private void displayAppointmentComment(Appointment appointment, View view) {
        PopupWindow popup = new PopupWindow();
        View layout = getLayoutInflater().inflate(R.layout.pop_out_layout, null);

        TextView comment = layout.findViewById(R.id.tvCaption);
        comment.setText(appointment.getComment());

        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(view);
    }
}
