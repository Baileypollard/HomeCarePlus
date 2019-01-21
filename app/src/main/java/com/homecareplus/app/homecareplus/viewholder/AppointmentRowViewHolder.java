package com.homecareplus.app.homecareplus.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AppointmentRowViewHolder extends RecyclerView.ViewHolder
{
    private TextView clientNameTv;
    private TextView clientAddressTv;
    private TextView appointmentTimeTv;

    public AppointmentRowViewHolder(View view)
    {
        super(view);

        clientNameTv = view.findViewById(com.homecareplus.app.homecareplus.R.id.clientNameTextView);
        clientAddressTv = view.findViewById(com.homecareplus.app.homecareplus.R.id.clientAddressTextView);
        appointmentTimeTv = view.findViewById(com.homecareplus.app.homecareplus.R.id.appointmentTimeTextView);
    }


    public void setClientName(String name)
    {
        clientNameTv.setText(name);
    }

    public void setClientAddress(String address)
    {
        clientAddressTv.setText(address);
    }

    public void setAppointmentTime(String time)
    {
        appointmentTimeTv.setText(time);
    }
}
