package com.homecareplus.app.homecareplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.viewholder.AppointmentHeaderViewHolder;
import com.homecareplus.app.homecareplus.viewholder.AppointmentRowViewHolder;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class AppointmentSection extends StatelessSection
{
    private List<Appointment> appointmentList;
    private String title;
    private AppointmentRowOnClickListener onClickListener;

    public AppointmentSection(String title, List<Appointment> appointments, AppointmentRowOnClickListener appointmentRowOnClickListener, ItemTouchHelperExtension mItemTouchHelperExtension)
    {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.client_appointment_row)
                .headerResourceId(R.layout.section_header)
                .build());

        this.appointmentList = appointments;
        this.title = title;
        this.onClickListener = appointmentRowOnClickListener;
    }

    public void setAppointmentList(List<Appointment> appointmentList)
    {
        this.appointmentList = appointmentList;
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
}
