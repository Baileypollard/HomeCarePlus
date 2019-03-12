package com.homecareplus.app.homecareplus.couchbase;

import com.couchbase.lite.Dictionary;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;

import java.util.HashMap;
import java.util.Map;

public class DictionaryToModel
{
    public static Appointment getAppointmentFromDictionary(Dictionary dictionary, Client client, Employee employee, String date)
    {
        String appointmentId = dictionary.getString("appointment_id");
        String status = dictionary.getString("status");
        long startTime = dictionary.getLong("start_time");
        long endTime = dictionary.getLong("end_time");
        String punchedInTime = dictionary.getString("punched_in_time");
        String punchedOutTime = dictionary.getString("punched_out_time");
        String comment = dictionary.getString("comment");
        String kmsTravelled = dictionary.getString("kms_travelled") != null ? dictionary.getString("kms_travelled") : "";
        String description = dictionary.getString("description");

        Dictionary punchedInDict = dictionary.getDictionary("punched_in_loc");
        Dictionary punchedOutDict = dictionary.getDictionary("punched_out_loc");

        Dictionary type = dictionary.getDictionary("type");
        String name = type.getString("name");
        String abbrev = type.getString("abbreviation");

        Map<String, Double> punchedInLoc = new HashMap<>();
        Map<String, Double> punchedOutLoc = new HashMap<>();

        if (punchedInDict != null && punchedOutDict != null)
        {
            punchedInLoc.put("lat", punchedInDict.getDouble("lat"));
            punchedInLoc.put("lng", punchedInDict.getDouble("lng"));

            punchedOutLoc.put("lat", punchedOutDict.getDouble("lat"));
            punchedOutLoc.put("lng", punchedOutDict.getDouble("lng"));
        }

        return new Appointment(appointmentId, employee, client, date, AppointmentStatus.valueOf(status),
                startTime, endTime, description, punchedInTime, punchedOutTime, comment,
                kmsTravelled, punchedInLoc, punchedOutLoc, abbrev);
    }

    public static Employee getEmployeeFromDictionary(Dictionary dictionary)
    {
        String employeeId = dictionary.getString("employee_id");
        String employeeFirstName = dictionary.getString("first_name");
        String employeeLastName = dictionary.getString("last_name");
        String employeeAddress = dictionary.getString("address");
        String employeeGender = dictionary.getString("gender");
        String employeePhoneNumber = dictionary.getString("phone_number");

        return new Employee(employeeId, employeeFirstName, employeeLastName, employeePhoneNumber, employeeAddress, employeeGender);

    }

    public static Client getClientFromDictionary(Dictionary dictionary)
    {
        String clientFirstName = dictionary.getString("first_name");
        String clientLastName = dictionary.getString("last_name");
        String clientAddress = dictionary.getString("address");
        String clientGender = dictionary.getString("gender");
        String clientPhoneNumber = dictionary.getString("phone_number");
        String clientId = dictionary.getString("client_id");
        String additionalInformation = dictionary.getString("additional_information");
        return new Client(clientId, clientFirstName, clientLastName, clientAddress, clientGender, clientPhoneNumber, additionalInformation);
    }

}
