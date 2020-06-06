package com.homecareplus.app.homecareplus.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;

public class PreviousAppointmentViewHolder extends RecyclerView.ViewHolder
{
    private TextView clientNameTv;
    private TextView appointmentTimeTv;
    private TextView employeeNameTextView;
    private TextView appointmentDateTextView;
    private View appointmentRow;

    public PreviousAppointmentViewHolder(View view)
    {
        super(view);
        appointmentRow = view.findViewById(R.id.appointmentRow);
        clientNameTv = view.findViewById(R.id.clientNameTextView);
        employeeNameTextView = view.findViewById(R.id.employeeNameTextView);
        appointmentTimeTv = view.findViewById(R.id.appointmentTimeTextView);
        appointmentDateTextView = view.findViewById(R.id.appointmentDateTextView);
    }

    public View getAppointmentRow() {
        return  appointmentRow;
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
