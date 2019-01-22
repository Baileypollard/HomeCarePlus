package com.homecareplus.app.homecareplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CustomSectionedAdapter extends SectionedRecyclerViewAdapter
{
    private AppointmentRowOnClickListener onClickListener;

    public CustomSectionedAdapter(AppointmentRowOnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public void addAppointment(Appointment appointment)
    {
        if (getSection(appointment.getDate()) == null)
        {
            AppointmentSection section = new AppointmentSection(appointment.getDate(), appointment.getDate(), onClickListener);
            addSection(appointment.getDate(), section);
        }

        AppointmentSection section = (AppointmentSection) getSection(appointment.getDate());
        if (!section.containsAppointment(appointment))
        {
            section.addAppointment(appointment);
            notifyItemChangedInSection(section, section.getAppointmentPosition(appointment));
        }
    }


}
