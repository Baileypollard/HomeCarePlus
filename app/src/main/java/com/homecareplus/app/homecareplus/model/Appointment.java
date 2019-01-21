package com.homecareplus.app.homecareplus.model;

public class Appointment
{
    private String id;
    private Employee employee;
    private String date;
    private String appointmentInfo;
    private Client client;
    private String startTime;
    private String endTime;
    private String status;

    public Appointment(String id, Employee employee, Client client, String date, String status,
                       String startTime, String endTime, String appointmentInfo)
    {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.appointmentInfo = appointmentInfo;
        this.client = client;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getClientName()
    {
        return this.client.getFirstName() + " " + client.getLastName();
    }

    public String getAddress()
    {
        return this.client.getAddress();
    }

    public String getStartTime()
    {
        return this.startTime;
    }

    public String getEndTime()
    {
        return this.endTime;
    }

    public String getAppointmentTime()
    {
        return getStartTime() + " - " + getEndTime();
    }
}
