package com.homecareplus.app.homecareplus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.viewholder.AppointmentRowViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PreviousAppointmentAdapter extends RecyclerView.Adapter<AppointmentRowViewHolder>
{
    private LayoutInflater mInflater;
    private List<Appointment> appointmentList;

    public PreviousAppointmentAdapter(Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        appointmentList = new ArrayList<>();
    }

    public void setAppointmentList(List<Appointment> appointmentList)
    {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.client_appointment_row, viewGroup, false);
        return new AppointmentRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRowViewHolder viewHolder, int i)
    {
        Appointment appointment = appointmentList.get(i);

        viewHolder.setClientName(appointment.getClientName());
        viewHolder.setClientAddress(appointment.getAddress());
        viewHolder.setAppointmentTime(appointment.getAppointmentTime());
        viewHolder.setAppointmentStatus(appointment.getStatus());
    }

    @Override
    public int getItemCount()
    {
        return appointmentList.size();
    }
}
