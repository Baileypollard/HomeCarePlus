package com.homecareplus.app.homecareplus.model;

import java.util.List;

public class DayAppointments
{
    private String date;
    private List<Appointment> appointmentList;

    public DayAppointments(String date, List<Appointment> appointments)
    {
        this.date = date;
        this.appointmentList = appointments;
    }
}
