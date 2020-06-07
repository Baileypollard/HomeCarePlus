package com.homecareplus.app.homecareplus.enumerator;

public enum AppointmentStatus
{
    NEW("NEW", "New"),
    IN_PROGRESS("IN_PROGRESS", "In Progress"),
    COMPLETED("COMPLETED", "Completed"),
    VERIFIED("VERIFIED", "Verified"),
    DECLINED("DECLINED", "Declined");

    private String value;
    private String friendlyName;

    AppointmentStatus(String value, String friendlyName)
    {
        this.value = value;
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
    public String getValue()
    {
        return this.value;
    }

}
