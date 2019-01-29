package com.homecareplus.app.homecareplus.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;

public class AppointmentRowViewHolder extends RecyclerView.ViewHolder
{
    private TextView clientNameTv;
    private TextView clientAddressTv;
    private TextView appointmentTimeTv;
    private TextView appointmentStatusTv;

    public AppointmentRowViewHolder(View view)
    {
        super(view);

        clientNameTv = view.findViewById(com.homecareplus.app.homecareplus.R.id.clientNameTextView);
        clientAddressTv = view.findViewById(com.homecareplus.app.homecareplus.R.id.clientAddressTextView);
        clientAddressTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + clientAddressTv.getText().toString()));
                v.getContext().startActivity(geoIntent);
            }
        });
        appointmentTimeTv = view.findViewById(R.id.appointmentTimeTextView);
        appointmentStatusTv = view.findViewById(R.id.appointmentStatusTextView);
    }

    public void setAppointmentStatus(AppointmentStatus status)
    {
        appointmentStatusTv.setText(status.getValue());
    }

    public void setClientName(String name)
    {
        clientNameTv.setText(name);
    }

    public void setClientAddress(String address)
    {
        SpannableString spanStr = new SpannableString(address);
        spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
        clientAddressTv.setText(spanStr);
    }

    public void setAppointmentTime(String time)
    {
        appointmentTimeTv.setText(time);
    }
}
