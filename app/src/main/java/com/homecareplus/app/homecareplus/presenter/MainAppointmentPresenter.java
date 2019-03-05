package com.homecareplus.app.homecareplus.presenter;

import android.util.Log;

import com.couchbase.lite.Array;
import com.couchbase.lite.CouchbaseLiteException;
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
import com.homecareplus.app.homecareplus.contract.MainAppointmentsContract;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.couchbase.DictionaryToModel;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.util.DateUtil;
import com.homecareplus.app.homecareplus.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class MainAppointmentPresenter implements MainAppointmentsContract.presenter
{
    @Override
    public void fetchEmployeeName(String employeeId)
    {
        final Database database = DatabaseManager.getDatabase();

        final Query query = QueryBuilder.select(
                SelectResult.expression(Expression.property("first_name")),
                SelectResult.expression(Expression.property("last_name")))
                .from(DataSource.database(database))
                .where(Expression.property("type").equalTo(Expression.string("employee"))
                        .and(Expression.property("employee_id").equalTo(Expression.string(employeeId))));

        query.addChangeListener(new QueryChangeListener()
        {
            @Override
            public void changed(QueryChange change)
            {
                ResultSet results = change.getResults();
                List<Result> resultList = results.allResults();

                if (resultList.size() == 0)
                {
                    return;
                }
                String firstName = resultList.get(0).getString("first_name");
                String lastName = resultList.get(0).getString("last_name");
                String fullName = firstName + " " + lastName;
                view.displayEmployeeName(fullName);
            }
        });

        try
        {
            query.execute();
        } catch (CouchbaseLiteException e)
        {
            Log.e("TAG", "Could not execute query - " + e.getMessage());
        }
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
            //Clear the shared instances
            SharedPreference.getSharedInstance(view.getActivity()).clear();
            view.startLoginActivity();
        }
        catch (CouchbaseLiteException e)
        {
            Log.e("TAG", "Error logging out: " + e.getMessage());
        }
    }

    @Override
    public void fetchAppointments(String employeeId)
    {
        final Database database = DatabaseManager.getDatabase();
        final DataSource dataSource = DataSource.database(database).as("appointmentDS");
        final DataSource employeeDs = DataSource.database(database).as("employeeDS");

        Join employeeJoin = Join.innerJoin(employeeDs).on(Expression.property("employee_id").from("appointmentDS")
                .equalTo(Expression.property("employee_id").from("employeeDS"))
                .and(Expression.property("type").from("appointmentDS").equalTo(Expression.string("appointment"))
                        .and(Expression.property("type").from("employeeDS").equalTo(Expression.string("employee")))
                        .and(Expression.property("employee_id").from("appointmentDS").equalTo(Expression.string(employeeId)))));

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

                for (Result r : rowList)
                {
                    Dictionary appointmentDict = r.getDictionary("appointmentDS");
                    Dictionary employeeDict = r.getDictionary("employeeDS");

                    String date = appointmentDict.getString("date");

                    Employee employee = DictionaryToModel.getEmployeeFromDictionary(employeeDict);

                    Array array = appointmentDict.getArray("schedule");

                    if (array == null)
                        return;

                    List<Appointment> appointments = new ArrayList<>();
                    for (int i = 0; i < array.count(); i++)
                    {
                        Dictionary dictionary = array.getDictionary(i);
                        Client client = DictionaryToModel.getClientFromDictionary(dictionary);
                        Appointment appointment = DictionaryToModel.getAppointmentFromDictionary(dictionary, client, employee, date);
                        appointments.add(appointment);
                    }
                    view.displayAppointmentSection(new AppointmentSectionModel(date, appointments));
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
