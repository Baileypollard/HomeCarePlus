package com.homecareplus.app.homecareplus.util;

import com.homecareplus.app.homecareplus.model.Appointment;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonDocCreator
{
    public static JSONObject createAppointmentJSON(Appointment appointment)
    {
        JSONObject appointmentJson = new JSONObject();
        try
        {
            appointmentJson.put("employee_id", appointment.getEmployeeId());
            appointmentJson.put("appointment_id", appointment.getId());
            appointmentJson.put("appointment_id", appointment.getId());
            appointmentJson.put("client_id", appointment.getClient().getClientId());
            appointmentJson.put("address", appointment.getAddress());
            appointmentJson.put("comment", appointment.getComment());
            appointmentJson.put("end_time", appointment.getEndTime());
            appointmentJson.put("start_time", appointment.getStartTime());
            appointmentJson.put("gender", appointment.getClientGender());
            appointmentJson.put("last_name", appointment.getClientName());
            appointmentJson.put("first_name", appointment.getClientFirstName());
            appointmentJson.put("phone_number", appointment.getClientPhoneNumber());
            appointmentJson.put("punched_in_time", appointment.getClientPhoneNumber());
            appointmentJson.put("punched_out_time", appointment.getPunchedOutTime());
            appointmentJson.put("status", appointment.getStatus().getValue());
            appointmentJson.put("date", appointment.getDate());

            JSONObject punchedInLoc = new JSONObject();
            punchedInLoc.put("lat", appointment.getPunchedInLocation().get("lat"));
            punchedInLoc.put("lng", appointment.getPunchedInLocation().get("lng"));

            JSONObject punchedOutLoc = new JSONObject();
            punchedOutLoc.put("lat", appointment.getPunchedOutLocation().get("lat"));
            punchedOutLoc.put("lng", appointment.getPunchedOutLocation().get("lng"));

            appointmentJson.put("punched_in_loc", punchedInLoc);
            appointmentJson.put("punched_out_loc", punchedOutLoc);

        }
        catch (JSONException e)
        {

        }
        return appointmentJson;
    }
}
