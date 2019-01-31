package com.homecareplus.app.homecareplus.enumerator;

public enum AppointmentStatus
{
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    CONFIRMED("CONFIRMED");

    private String value;
    AppointmentStatus(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

}
