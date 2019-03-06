package com.homecareplus.app.homecareplus.presenter;

import android.util.Log;

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
import com.homecareplus.app.homecareplus.couchbase.DictionaryToModel;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

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
    public void loadPreviousAppointments(final Client client)
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
                .where(Expression.property("date").from("appointmentDS").lessThan(Expression.string(DateUtil.getTodayFormatted()))
                        .and(ArrayExpression.any(ArrayExpression.variable("appointment"))
                                .in(Expression.property("schedule").from("appointmentDS"))
                                .satisfies(ArrayExpression.variable("appointment.client_id").equalTo(Expression.string(client.getClientId())))))
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
                    Employee employee = DictionaryToModel.getEmployeeFromDictionary(employeeDict);

                    Array array = appointmentDict.getArray("schedule");
                    if (array == null)
                        return;

                    for (int i = 0; i < array.count(); i++)
                    {
                        Dictionary dictionary = array.getDictionary(i);
                        Client newClient = DictionaryToModel.getClientFromDictionary(dictionary);
                        Appointment appointment = DictionaryToModel.getAppointmentFromDictionary(dictionary, client, employee, date);
                        if (client.getClientId().equals(newClient.getClientId()) && appointment.isVerified())
                            appointments.add(appointment);
                    }
                }
                view.displayAppointments(appointments);
            }
        });
    }


}
