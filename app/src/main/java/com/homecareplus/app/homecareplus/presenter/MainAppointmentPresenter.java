package com.homecareplus.app.homecareplus.presenter;

import android.util.Log;

import com.couchbase.lite.Array;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Join;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.QueryChange;
import com.couchbase.lite.QueryChangeListener;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.homecareplus.app.homecareplus.contract.MainAppointmentsContract;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.util.DateUtil;

import java.util.Date;
import java.util.List;

public class MainAppointmentPresenter implements MainAppointmentsContract.presenter
{
    @Override
    public void fetchEmployeeName(String employeeId)
    {
        final Database database = DatabaseManager.getDatabase();
        String fullName;

        Query query = QueryBuilder.select(SelectResult.expression(Expression.property("first_name")),
                SelectResult.expression(Expression.property("last_name")))
                .from(DataSource.database(database))
                .where(Expression.property("employee_id").equalTo(Expression.string(employeeId)));
        try
        {
            ResultSet results = query.execute();
            List<Result> resultList = results.allResults();

            if (resultList.size() == 0)
            {
                return;
            }
            String firstName = resultList.get(0).getString("first_name");
            String lastName = resultList.get(0).getString("last_name");
            fullName = firstName + " " + lastName;
        }
        catch(CouchbaseLiteException e)
        {
            fullName = "ERROR FETCHING NAME";
            Log.e("TAG", "Could not execute query - " + e.getMessage());
        }
        view.displayEmployeeName(fullName);
    }

    private MainAppointmentsContract.view view;

    public MainAppointmentPresenter(MainAppointmentsContract.view view)
    {
        this.view = view;
    }

    @Override
    public void logout()
    {
        try
        {
            DatabaseManager.closeDatabase();
            view.startLoginActivity();
        }
        catch (CouchbaseLiteException e)
        {
            Log.e("TAG", "Error logging out: " + e.getMessage());
        }
    }

    @Override
    public void fetchAppointments()
    {
        final Database database = DatabaseManager.getDatabase();
        final DataSource dataSource = DataSource.database(database).as("appointmentDS");
        final DataSource employeeDs = DataSource.database(database).as("employeeDS");

        Join employeeJoin = Join.innerJoin(employeeDs).on(Expression.property("employee_id").from("appointmentDS")
                        .equalTo(Expression.property("employee_id").from("employeeDS"))
                        .and(Expression.property("type").from("appointmentDS").equalTo(Expression.string("appointment"))
                        .and(Expression.property("type").from("employeeDS").equalTo(Expression.string("employee")))
                        .and(Expression.property("employee_id").from("appointmentDS").equalTo(Expression.string("testuser")))));

        Query query = QueryBuilder.select(
                SelectResult.all().from("appointmentDS"),
                SelectResult.all().from("employeeDS"))
                .from(dataSource)
                .join(employeeJoin)
                .where(Expression.property("date").from("appointmentDS").greaterThanOrEqualTo(Expression.string(DateUtil.getTodayFormatted())))
                .orderBy(Ordering.expression(Expression.property("date").from("appointmentDS")).ascending());

        query.addChangeListener(new QueryChangeListener()
        {
            @Override
            public void changed(QueryChange change)
            {
                ResultSet rows = change.getResults();

                if (rows == null)
                {
                    return;
                }
                List<Result> rowList = rows.allResults();

                for (Result r: rowList)
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

                    for (int i = 0; i < array.count(); i++)
                    {
                        String clientFirstName = array.getDictionary(i).getString("first_name");

                        String clientLastName = array.getDictionary(i).getString("last_name");
                        String clientAddress = array.getDictionary(i).getString("address");
                        String clientGender = array.getDictionary(i).getString("gender");

                        String appointmentId = array.getDictionary(i).getString("appointment_id");
                        String status =  array.getDictionary(i).getString("status");
                        String startTime =  array.getDictionary(i).getString("start_time");
                        String endTime =  array.getDictionary(i).getString("end_time");

                        Client client = new Client("1", clientFirstName, clientLastName, clientAddress, clientGender);

                        Appointment appointment = new Appointment(appointmentId, employee, client, date, status, startTime, endTime, "Clean shit");

                        view.displayAppointment(appointment);
                    }
                }
            }
        });

        try
        {
            query.execute();
        }
        catch (CouchbaseLiteException e)
        {
            e.printStackTrace();
        }
    }
}
