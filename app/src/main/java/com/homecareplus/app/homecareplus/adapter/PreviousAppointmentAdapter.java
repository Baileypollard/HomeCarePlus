package com.homecareplus.app.homecareplus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.PrevAppointmentOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.viewholder.PreviousAppointmentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PreviousAppointmentAdapter extends RecyclerView.Adapter<PreviousAppointmentViewHolder>
{
    private LayoutInflater mInflater;
    private List<Appointment> appointmentList;
    private PrevAppointmentOnClickListener listener;

    public PreviousAppointmentAdapter(Context context, PrevAppointmentOnClickListener listener)
    {
        this.mInflater = LayoutInflater.from(context);
        this.appointmentList = new ArrayList<>();
        this.listener = listener;
    }

    public void setAppointmentList(List<Appointment> appointmentList)
    {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PreviousAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.client_previous_appointment_row, viewGroup, false);
        return new PreviousAppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousAppointmentViewHolder viewHolder, int i)
    {
        Appointment appointment = appointmentList.get(i);

        viewHolder.setAppointmentDate(appointment.getNiceDate());
        viewHolder.setClientName(appointment.getClientName());
        viewHolder.setAppointmentEmployeeName(appointment.getEmployee().getFullName());
        viewHolder.setAppointmentTime(appointment.getAppointmentTime());
        viewHolder.getAppointmentRow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(viewHolder.getAppointmentRow(), appointment);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return appointmentList.size();
    }
}
