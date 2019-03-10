package com.homecareplus.app.homecareplus.adapter;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.homecareplus.app.homecareplus.contract.AppointmentRowOnClickListener;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.List;

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

    public void displayAppointmentSection(AppointmentSectionModel sec)
    {
        if(sec.getAppointmentList().size() == 0)
        {
            removeSection(sec.getDate());
            notifyDataSetChanged();
            return;
        }

        if (getSection(sec.getDate()) == null)
        {
            AppointmentSection section = new AppointmentSection(sec.getDate(), sec.getAppointmentList(), onClickListener, mItemTouchHelperExtension);
            addSection(sec.getDate(), section);
        }
        else
        {
            AppointmentSection section = (AppointmentSection) getSection(sec.getDate());
            section.setAppointmentList(sec.getAppointmentList());
            notifyDataSetChanged();
        }
    }
}
