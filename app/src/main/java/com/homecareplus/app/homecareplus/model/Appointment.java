package com.homecareplus.app.homecareplus.model;

import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.util.DateUtil;

import java.io.Serializable;
import java.util.Map;

public class Appointment implements Serializable
{
    private String id;
    private Employee employee;
    private String date;
    private String appointmentInfo;
    private Client client;
    private long startTime;
    private long endTime;
    private AppointmentStatus status;
    private String punchedInTime;
    private String punchedOutTime;
    private String comment;
    private String kmsTravelled;
    private String totalTimeSpent;
    private Map<String, Double> punchedInLocation;
    private Map<String, Double> punchedOutLocation;

    public Appointment(String id, Employee employee, Client client, String date, AppointmentStatus status,
                       long startTime, long endTime, String appointmentInfo, String punchedInTime,
                       String punchedOutTime, String comment, String kmsTravelled, Map<String, Double> punchedInLocation,
                       Map<String, Double> punchedOutLocation)
    {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.appointmentInfo = appointmentInfo;
        this.client = client;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.punchedInTime = punchedInTime;
        this.punchedOutTime = punchedOutTime;
        this.comment = comment;
        this.kmsTravelled = kmsTravelled;
        this.totalTimeSpent = DateUtil.calculateMinutesBetweenDates(punchedInTime, punchedOutTime);
        this.punchedInLocation = punchedInLocation;
        this.punchedOutLocation = punchedOutLocation;
    }

    public Client getClient()
    {
        return client;
    }

    public Map<String, Double> getPunchedInLocation()
    {
        return punchedInLocation;
    }

    public void setPunchedInLocation(Map<String, Double> punchedInLocation)
    {
        this.punchedInLocation = punchedInLocation;
    }

    public Map<String, Double> getPunchedOutLocation()
    {
        return punchedOutLocation;
    }

    public void setPunchedOutLocation(Map<String, Double> punchedOutLocation)
    {
        this.punchedOutLocation = punchedOutLocation;
    }

    public String getClientLastName()
    {
        return client.getLastName();
    }

    public String getClientFirstName()
    {
        return client.getFirstName();
    }

    public void setTotalTimeSpent(String totalTime)
    {
        this.totalTimeSpent = totalTime;
    }

    public String getTotalTimeSpent()
    {
        return this.totalTimeSpent;
    }

    public void setKmsTravelled(String kms)
    {
        this.kmsTravelled = kms;
    }

    public String getKmsTravelled()
    {
        return this.kmsTravelled;
    }

    public String getClientPhoneNumber()
    {
        return this.client.getPhoneNumber();
    }

    public String getClientGender()
    {
        return this.client.getGender();
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setStatus(AppointmentStatus status)
    {
        this.status = status;
    }

    public AppointmentStatus getStatus()
    {
        return this.status;
    }

    public void setPunchedInTime(String time)
    {
        this.punchedInTime = time;
    }

    public void setPunchedOutTime(String time)
    {
        this.punchedOutTime = time;
    }

    public String getPunchedOutTime()
    {
        return this.punchedOutTime;
    }

    public String getPunchedInTime()
    {
        return this.punchedInTime;
    }

    public String getEmployeeId()
    {
        return this.employee.getEmployeeId();
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

    public String getAppointmentInfo()
    {
        return this.appointmentInfo;
    }

    public String getAddress()
    {
        return this.client.getAddress();
    }

    public long getStartTime()
    {
        return this.startTime;
    }

    public long getEndTime()
    {
        return this.endTime;
    }

    public String getAppointmentTime()
    {
        return DateUtil.getTimeFromMs(getStartTime()) + " - " + DateUtil.getTimeFromMs(getEndTime());
    }

    public boolean isVerified()
    {
        return status.equals(AppointmentStatus.COMPLETED) || status.equals(AppointmentStatus.VERIFIED);
    }
}
