package com.homecareplus.app.homecareplus.adapter;

import com.homecareplus.app.homecareplus.model.Appointment;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CustomSectionedAdapter extends SectionedRecyclerViewAdapter
{
    private List<Appointment> appointments;
    public CustomSectionedAdapter()
    {
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment)
    {
        if (getSection(appointment.getDate()) != null)
        {
            AppointmentSection section = (AppointmentSection) getSection(appointment.getDate());
            if (!section.containsAppointment(appointment))
            {
                section.addAppointment(appointment);
            }
        }
        else
        {
            AppointmentSection section = new AppointmentSection(appointment.getDate());
            section.addAppointment(appointment);
            addSection(appointment.getDate(), section);
        }
        notifyDataSetChanged();

    }


}
