package com.homecareplus.app.homecareplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
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
    private String tag;
    private AppointmentRowOnClickListener onClickListener;

    public AppointmentSection(String title, String tag, AppointmentRowOnClickListener appointmentRowOnClickListener) {
        super(SectionParameters.builder()
                .itemResourceId(com.homecareplus.app.homecareplus.R.layout.client_appointment_row)
                .headerResourceId(com.homecareplus.app.homecareplus.R.layout.section_header)
                .build());

        this.appointmentList = new ArrayList<>();
        this.title = title;
        this.tag = tag;
        this.onClickListener = appointmentRowOnClickListener;
    }

    public String getTag()
    {
        return this.tag;
    }

    public void addAppointment(Appointment appointment)
    {
        this.appointmentList.add(appointment);
    }

    public void updateAppointment(int position, Appointment appointment)
    {
        this.appointmentList.set(position, appointment);
    }

    public int getAppointmentPosition(Appointment appointment)
    {
        for (Appointment a : appointmentList)
        {
            if (a.getId().equals(appointment.getId()))
            {
                return appointmentList.indexOf(a);
            }
        }
        return -1;
    }

    @Override
    public int getContentItemsTotal()
    {
        return this.appointmentList.size();
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
        headerHolder.setHeaderTitle(DateUtil.getHeaderDateFormat(title));
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        AppointmentRowViewHolder viewHolder = (AppointmentRowViewHolder) holder;
        final Appointment appointment = appointmentList.get(position);

        viewHolder.setClientName(appointment.getClientName());
        viewHolder.setClientAddress(appointment.getAddress());
        viewHolder.setAppointmentTime(appointment.getAppointmentTime());
        viewHolder.setAppointmentStatus(appointment.getStatus());

        viewHolder.itemView.getRootView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickListener.onItemClick(v, appointment);
            }
        });
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
