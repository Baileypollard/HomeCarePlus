package com.homecareplus.app.homecareplus.model;

import java.io.Serializable;

public class Employee implements Serializable
{
    private String employeeId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String gender;


    public Employee(String employeeId, String firstName, String lastName, String phoneNumber, String address, String gender)
    {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
    }

    public String getEmployeeId()
    {
        return this.employeeId;
    }
}
