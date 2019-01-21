package com.homecareplus.app.homecareplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.viewholder.AppointmentHeaderViewHolder;
import com.homecareplus.app.homecareplus.viewholder.AppointmentRowViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class AppointmentSection extends StatelessSection
{
    private List<Appointment> appointmentList;
    private String title;

    public AppointmentSection(String title) {
        super(SectionParameters.builder()
                .itemResourceId(com.homecareplus.app.homecareplus.R.layout.client_appointment_row)
                .headerResourceId(com.homecareplus.app.homecareplus.R.layout.section_header)
                .build());

        this.appointmentList = new ArrayList<>();
        this.title = title;
    }

    public void addAppointment(Appointment appointment)
    {
        appointmentList.add(appointment);
    }

    @Override
    public int getContentItemsTotal()
    {
        return appointmentList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {
        return new AppointmentRowViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {
        return new AppointmentHeaderViewHolder(view);
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {
        AppointmentHeaderViewHolder headerHolder = (AppointmentHeaderViewHolder) holder;

        // bind your header view here
        headerHolder.setHeaderTitle(DateUtil.getHeaderDateFormat(title));
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        AppointmentRowViewHolder viewHolder = (AppointmentRowViewHolder) holder;
        Appointment appointment = appointmentList.get(position);

        viewHolder.setClientName(appointment.getClientName());
        viewHolder.setClientAddress(appointment.getAddress());
        viewHolder.setAppointmentTime(appointment.getAppointmentTime());
        //Set all the values and shit here
    }

    public boolean containsAppointment(Appointment appointment)
    {
        for (Appointment a : appointmentList)
        {
            if (a.getId().equals(appointment.getId()))
            {
                return true;
            }
        }
        return false;
    }
}
