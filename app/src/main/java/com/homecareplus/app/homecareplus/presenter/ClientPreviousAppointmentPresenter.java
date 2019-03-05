package com.homecareplus.app.homecareplus.presenter;

import com.couchbase.lite.Array;
import com.couchbase.lite.ArrayExpression;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Join;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.QueryChange;
import com.couchbase.lite.QueryChangeListener;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.homecareplus.app.homecareplus.contract.ClientPreviousAppointmentContract;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.enumerator.AppointmentStatus;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientPreviousAppointmentPresenter implements ClientPreviousAppointmentContract.presenter
{
    private ClientPreviousAppointmentContract.view view;

    @Override
    public void setView(ClientPreviousAppointmentContract.view view)
    {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadPreviousAppointments(Client client)
    {
        Database database = DatabaseManager.getDatabase();


        final DataSource dataSource = DataSource.database(database).as("appointmentDS");
        final DataSource employeeDs = DataSource.database(database).as("employeeDS");

        Join employeeJoin = Join.innerJoin(employeeDs).on(Expression.property("employee_id").from("appointmentDS")
                .equalTo(Expression.property("employee_id").from("employeeDS"))
                .and(Expression.property("type").from("appointmentDS").equalTo(Expression.string("appointment"))
                        .and(Expression.property("type").from("employeeDS").equalTo(Expression.string("employee")))));

        Query query = QueryBuilder.select(
                SelectResult.all().from("appointmentDS"),
                SelectResult.all().from("employeeDS"))
                .from(dataSource)
                .join(employeeJoin)
                .where(ArrayExpression.any(ArrayExpression.variable("appointment"))
                                .in(Expression.property("schedule").from("appointmentDS"))
                                .satisfies(ArrayExpression.variable("appointment.client_id").equalTo(Expression.string(client.getClientId()))))
                .orderBy(Ordering.expression(Expression.property("date").from("appointmentDS")).ascending());

        query.addChangeListener(new QueryChangeListener()
        {
            @Override
            public void changed(QueryChange change)
            {
                List<Appointment> appointments = new ArrayList<>();
                ResultSet rows = change.getResults();

                if (rows == null)
                {
                    return;
                }
                List<Result> rowList = rows.allResults();

                for (Result r : rowList)
                {

                    Dictionary appointmentDict = r.getDictionary("appointmentDS");
                    Dictionary employeeDict = r.getDictionary("employeeDS");

                    String date = appointmentDict.getString("date");

                    String employeeId = employeeDict.getString("employee_id");
                    String employeeFirstName = employeeDict.getString("first_name");
                    String employeeLastName = employeeDict.getString("last_name");
                    String employeeAddress = employeeDict.getString("address");
                    String employeeGender = employeeDict.getString("gender");
                    String employeePhoneNumber = employeeDict.getString("phone_number");

                    Employee employee = new Employee(employeeId, employeeFirstName, employeeLastName, employeePhoneNumber, employeeAddress, employeeGender);

                    Array array = appointmentDict.getArray("schedule");

                    if (array == null)
                        return;

                    for (int i = 0; i < array.count(); i++)
                    {
                        Dictionary dictionary = array.getDictionary(i);

                        String clientFirstName = dictionary.getString("first_name");

                        String clientLastName = dictionary.getString("last_name");
                        String clientAddress = dictionary.getString("address");
                        String clientGender = dictionary.getString("gender");
                        String clientPhoneNumber = dictionary.getString("phone_number");

                        String appointmentId = dictionary.getString("appointment_id");
                        String status = dictionary.getString("status");
                        long startTime = dictionary.getLong("start_time");
                        long endTime = dictionary.getLong("end_time");
                        String punchedInTime = dictionary.getString("punched_in_time");
                        String punchedOutTime = dictionary.getString("punched_out_time");
                        String comment = dictionary.getString("comment");
                        String kmsTravelled = dictionary.getString("kms_travelled") != null ? dictionary.getString("kms_travelled") : "";
                        String clientId = dictionary.getString("client_id");

                        Dictionary punchedInDict = dictionary.getDictionary("punched_in_loc");
                        Dictionary punchedOutDict = dictionary.getDictionary("punched_out_loc");

                        Map<String, Double> punchedInLoc = new HashMap<>();
                        Map<String, Double> punchedOutLoc = new HashMap<>();

                        if (punchedInDict != null && punchedOutDict != null)
                        {
                            punchedInLoc.put("lat", punchedInDict.getDouble("lat"));
                            punchedInLoc.put("lng", punchedInDict.getDouble("lng"));

                            punchedOutLoc.put("lat", punchedOutDict.getDouble("lat"));
                            punchedOutLoc.put("lng", punchedOutDict.getDouble("lng"));
                        }
                        Client client = new Client(clientId, clientFirstName, clientLastName, clientAddress, clientGender, clientPhoneNumber);

                        Appointment appointment = new Appointment(appointmentId, employee, client, date, AppointmentStatus.valueOf(status),
                                startTime, endTime, "PC - This client will need a bath a breakfast", punchedInTime, punchedOutTime, comment,
                                kmsTravelled, punchedInLoc, punchedOutLoc);

                        appointments.add(appointment);
                    }
                }
                view.displayAppointments(appointments);
            }
        });
    }


}
