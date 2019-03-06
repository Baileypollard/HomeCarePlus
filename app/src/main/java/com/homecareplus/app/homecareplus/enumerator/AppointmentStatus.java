package com.homecareplus.app.homecareplus.enumerator;

public enum AppointmentStatus
{
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED"),
    VERIFIED("VERIFIED"),
    DECLINED("DECLINED");

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
