package com.homecareplus.app.homecareplus.adapter;

import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CustomSectionedAdapter extends SectionedRecyclerViewAdapter
{
    private AppointmentRowOnClickListener onClickListener;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public CustomSectionedAdapter(AppointmentRowOnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension mItemTouchHelperExtension)
    {
        this.mItemTouchHelperExtension = mItemTouchHelperExtension;
    }

    public void addAppointment(Appointment appointment)
    {
        if (getSection(appointment.getDate()) == null)
        {
            AppointmentSection section = new AppointmentSection(appointment.getDate(), appointment.getDate(), onClickListener, mItemTouchHelperExtension);
            addSection(appointment.getDate(), section);
        }

        AppointmentSection section = (AppointmentSection) getSection(appointment.getDate());
        if (section.containsAppointment(appointment))
        {
            section.updateAppointment(section.getAppointmentPosition(appointment), appointment);
            notifyItemChangedInSection(section, section.getAppointmentPosition(appointment));
        }
        else
        {
            section.addAppointment(appointment);
            notifyItemChangedInSection(section, section.getAppointmentPosition(appointment));
        }
    }


}
