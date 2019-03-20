package com.homecareplus.app.homecareplus.model;

import java.io.Serializable;

public class Client implements Serializable
{
    private String clientId;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private String phoneNumber;
    private String additionalInfo;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String healthCardNumber;

    public Client(String clientId, String firstName, String lastName, String address, String gender, String phoneNumber, String additionalInfo, String emergencyContactName, String emergencyContactNumber, String healthCardNumber)
    {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.additionalInfo = additionalInfo;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactNumber = emergencyContactNumber;
        this.healthCardNumber = healthCardNumber;
    }

    public String getEmergencyContactName()
    {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber()
    {
        return emergencyContactNumber;
    }

    public String getHealthCardNumber()
    {
        return healthCardNumber;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    public String getClientId()
    {
        return clientId;
    }

    public String getGender()
    {
        return this.gender;
    }

    public String getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

}
