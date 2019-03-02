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

    public Client(String id, String firstName, String lastName, String address, String gender, String phoneNumber)
    {
        this.clientId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
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

}
