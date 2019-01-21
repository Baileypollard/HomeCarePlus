package com.homecareplus.app.homecareplus.model;

import java.io.Serializable;

public class Client implements Serializable
{
    private String clientId;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public Client(String id, String firstName, String lastName, String address, String gender)
    {
        this.clientId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.gender = gender;
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
