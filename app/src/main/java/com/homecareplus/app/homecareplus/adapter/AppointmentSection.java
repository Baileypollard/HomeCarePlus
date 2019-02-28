package com.homecareplus.app.homecareplus.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.viewholder.AppointmentHeaderViewHolder;
import com.homecareplus.app.homecareplus.viewholder.AppointmentRowViewHolder;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class AppointmentSection extends StatelessSection
{
    private List<Appointment> appointmentList;
    private String title;
    private AppointmentRowOnClickListener onClickListener;
    private ItemTouchHelperExtension itemTouchHelperExtension;

    public AppointmentSection(String title, String tag, AppointmentRowOnClickListener appointmentRowOnClickListener, ItemTouchHelperExtension mItemTouchHelperExtension)
    {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.client_appointment_row)
                .headerResourceId(R.layout.section_header)
                .build());

        this.appointmentList = new ArrayList<>();
        this.title = title;
        this.onClickListener = appointmentRowOnClickListener;
        this.itemTouchHelperExtension = mItemTouchHelperExtension;
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

        viewHolder.getAppointmentRow().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickListener.onItemClick(v, appointment);
            }
        });

        viewHolder.getCallClientTextView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickListener.onCallClicked(appointment);
            }
        });
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
