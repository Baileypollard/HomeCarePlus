package com.homecareplus.app.homecareplus.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;

public class PreviousAppointmentViewHolder extends RecyclerView.ViewHolder
{
    private TextView clientNameTv;
    private TextView appointmentTimeTv;
    private TextView callClientTextView;
    private TextView viewClientInformationTextView;
    private TextView employeeNameTextView;
    private TextView appointmentDateTextView;
    private View appointmentRow;

    public PreviousAppointmentViewHolder(View view)
    {
        super(view);
        appointmentRow = view.findViewById(R.id.appointmentRow);
        callClientTextView = view.findViewById(R.id.callClientTextView);
        viewClientInformationTextView = view.findViewById(R.id.viewClientTextView);
        clientNameTv = view.findViewById(R.id.clientNameTextView);
        employeeNameTextView = view.findViewById(R.id.employeeNameTextView);
        appointmentTimeTv = view.findViewById(R.id.appointmentTimeTextView);
        appointmentDateTextView = view.findViewById(R.id.appointmentDateTextView);
    }

    public void setAppointmentDate(String date)
    {
        appointmentDateTextView.setText(date);
    }

    public void setAppointmentEmployeeName(String employeeName)
    {
        employeeNameTextView.setText(employeeName);
    }

    public void setClientName(String name)
    {
        clientNameTv.setText(name);
    }

    public void setAppointmentTime(String time)
    {
        appointmentTimeTv.setText(time);
    }
}
