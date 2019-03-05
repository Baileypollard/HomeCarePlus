package com.homecareplus.app.homecareplus.model;

import java.util.List;

public class AppointmentSectionModel
{
    private String date;
    private List<Appointment> appointmentList;

    public AppointmentSectionModel(String date, List<Appointment> appointmentList)
    {
        this.date = date;
        this.appointmentList = appointmentList;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public List<Appointment> getAppointmentList()
    {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList)
    {
        this.appointmentList = appointmentList;
    }
}
