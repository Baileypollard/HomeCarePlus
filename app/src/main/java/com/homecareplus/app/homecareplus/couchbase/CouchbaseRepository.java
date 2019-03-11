package com.homecareplus.app.homecareplus.couchbase;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Array;
import com.couchbase.lite.ArrayExpression;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Join;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDictionary;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.QueryChange;
import com.couchbase.lite.QueryChangeListener;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorChange;
import com.couchbase.lite.ReplicatorChangeListener;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;
import com.couchbase.lite.SessionAuthenticator;
import com.couchbase.lite.URLEndpoint;
import com.homecareplus.app.homecareplus.contract.CBRepository;
import com.homecareplus.app.homecareplus.model.Appointment;
import com.homecareplus.app.homecareplus.model.AppointmentSectionModel;
import com.homecareplus.app.homecareplus.model.Client;
import com.homecareplus.app.homecareplus.model.Employee;
import com.homecareplus.app.homecareplus.util.DateUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CouchbaseRepository implements CBRepository
{
    private static CBRepository instance;
    private static Database database;
    private static Replicator replicator;
    private static ListenerToken token;
    private MutableLiveData<List<AppointmentSectionModel>> appointmentSectionLiveData = new MutableLiveData<>();;
    private MutableLiveData<String> employeeNameData = new MutableLiveData<>();
    private MutableLiveData<List<Appointment>> previousAppointmentData = new MutableLiveData<>();
    private MutableLiveData<Appointment> appointmentData = new MutableLiveData<>();

    public static void init(Context context, String employeeId, String sessionId)
    {
        if (instance == null)
        {
            instance = new CouchbaseRepository(context, employeeId);
            beginDatabaseReplication(sessionId);
        }
    }

    private CouchbaseRepository(Context context, String employeeId)
    {
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        try
        {
            database = new Database("appointments-" + employeeId, config);
        } catch (CouchbaseLiteException e)
        {
            e.printStackTrace();
        }
    }

    //Methods here

    @Override
    public void saveAppointment(Appointment appointment)
    {
        MutableDocument doc = database.getDocument(appointment.getEmployeeId() + "." + appointment.getDate()).toMutable();

        MutableArray array = doc.getArray("schedule").toMutable();

        for (int i = 0; i < array.count(); i++)
        {
            MutableDictionary dict = array.getDictionary(i).toMutable();
            String appointmentId = dict.getString("appointment_id");
            if (appointmentId.equals(appointment.getId()))
            {
                MutableDictionary punchedInDict = new MutableDictionary();
                if (appointment.getPunchedInLocation() != null)
                {
                    Double lat = appointment.getPunchedInLocation().get("lat") != null ? appointment.getPunchedInLocation().get("lat") : 0.0;
                    Double lng = appointment.getPunchedInLocation().get("lat") != null ? appointment.getPunchedInLocation().get("lng") : 0.0;
                    punchedInDict.setDouble("lat", lat);
                    punchedInDict.setDouble("lng", lng);
                    dict.setDictionary("punched_in_loc", punchedInDict);
                }

                MutableDictionary punchedOutDict = new MutableDictionary();
                if (appointment.getPunchedInLocation() != null)
                {
                    Double lat = appointment.getPunchedOutLocation().get("lat") != null ? appointment.getPunchedOutLocation().get("lat") : 0.0;
                    Double lng = appointment.getPunchedOutLocation().get("lat") != null ? appointment.getPunchedOutLocation().get("lng") : 0.0;

                    punchedOutDict.setDouble("lat", lat);
                    punchedOutDict.setDouble("lng", lng);
                    dict.setDictionary("punched_out_loc", punchedOutDict);
                }

                dict.setString("punched_in_time", appointment.getPunchedInTime());
                dict.setString("punched_out_time", appointment.getPunchedOutTime());
                dict.setString("status", appointment.getStatus().getValue());
                dict.setString("comment", appointment.getComment());
                dict.setString("kms_travelled", appointment.getKmsTravelled());
                array.setDictionary(i, dict);
            }
        }
        doc.setArray("schedule", array);

        try
        {
            database.save(doc);
            appointmentData.postValue(appointment);
        }
        catch (CouchbaseLiteException e)
        {
            Log.d("TAG", "COUCHBASE EXCEPTION: " + e);
        }
    }

    @Override
    public void fetchEmployeeName(String employeeId)
    {
        final Query query = QueryBuilder.select(
                SelectResult.expression(Expression.property("first_name")),
                SelectResult.expression(Expression.property("last_name")))
                .from(DataSource.database(database))
                .where(Expression.property("type").equalTo(Expression.string("employee"))
                        .and(Expression.property("employee_id").equalTo(Expression.string(employeeId))));

        executeQuery(query);

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
                employeeNameData.postValue(fullName);
            }
        });
    }

    @Override
    public void fetchAppointments(String employeeId)
    {
        final DataSource dataSource = DataSource.database(database).as("appointmentDS");
        final DataSource employeeDs = DataSource.database(database).as("employeeDS");

        Join employeeJoin = Join.innerJoin(employeeDs).on(Expression.property("employee_id").from("appointmentDS")
                .equalTo(Expression.property("employee_id").from("employeeDS"))
                .and(Expression.property("type").from("appointmentDS").equalTo(Expression.string("appointment"))
                        .and(Expression.property("type").from("employeeDS").equalTo(Expression.string("employee")))
                        .and(Expression.property("employee_id").from("appointmentDS").equalTo(Expression.string(employeeId)))));

        final Query query = QueryBuilder.select(
                SelectResult.all().from("appointmentDS"),
                SelectResult.all().from("employeeDS"))
                .from(dataSource)
                .join(employeeJoin)
                .where(Expression.property("date").from("appointmentDS").greaterThanOrEqualTo(Expression.string(DateUtil.getTodayFormatted())))
                .orderBy(Ordering.expression(Expression.property("date").from("appointmentDS")).ascending());

        executeQuery(query);

        final List<AppointmentSectionModel> appointmentSectionModels = new ArrayList<>();

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
                    appointmentSectionModels.add(new AppointmentSectionModel(date, appointments));
                }
                appointmentSectionLiveData.postValue(appointmentSectionModels);
            }
        });
    }

    @Override
    public void fetchPreviousAppointmentsForClient(final Client client)
    {
        final DataSource dataSource = DataSource.database(database).as("appointmentDS");
        final DataSource employeeDs = DataSource.database(database).as("employeeDS");

        Join employeeJoin = Join.innerJoin(employeeDs).on(Expression.property("employee_id").from("appointmentDS")
                .equalTo(Expression.property("employee_id").from("employeeDS"))
                .and(Expression.property("type").from("appointmentDS").equalTo(Expression.string("appointment"))
                        .and(Expression.property("type").from("employeeDS").equalTo(Expression.string("employee")))));

        final Query query = QueryBuilder.select(
                SelectResult.all().from("appointmentDS"),
                SelectResult.all().from("employeeDS"))
                .from(dataSource)
                .join(employeeJoin)
                .where(Expression.property("date").from("appointmentDS").lessThan(Expression.string(DateUtil.getTodayFormatted()))
                        .and(ArrayExpression.any(ArrayExpression.variable("appointment"))
                                .in(Expression.property("schedule").from("appointmentDS"))
                                .satisfies(ArrayExpression.variable("appointment.client_id").equalTo(Expression.string(client.getClientId())))))
                .orderBy(Ordering.expression(Expression.property("date").from("appointmentDS")).ascending());

        executeQuery(query);

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
                    previousAppointmentData.postValue(appointments);
                }
            }
        });
    }

    @Override
    public MutableLiveData<List<Appointment>> getPreviousAppointmentData()
    {
        return previousAppointmentData;
    }

    @Override
    public MutableLiveData<Appointment> getAppointmentData(Appointment appointment)
    {
        appointmentData.setValue(appointment);
        return appointmentData;
    }

    @Override
    public MutableLiveData<List<AppointmentSectionModel>> getAppointmentSectionData()
    {
        return appointmentSectionLiveData;
    }

    @Override
    public MutableLiveData<String> getEmployeeNameData()
    {
        return employeeNameData;
    }

    private static void beginDatabaseReplication(String sessionId)
    {
        URI url = null;
        try
        {
            url = new URI("ws://35.235.103.244:4984/homecareplus");
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        ReplicatorConfiguration config = new ReplicatorConfiguration(database, new URLEndpoint(url));
        config.setReplicatorType(ReplicatorConfiguration.ReplicatorType.PUSH_AND_PULL);
        config.setContinuous(true);

        config.setAuthenticator(new SessionAuthenticator(sessionId));

        replicator = new Replicator(config);
        token = replicator.addChangeListener(new ReplicatorChangeListener()
        {
            @Override
            public void changed(ReplicatorChange change)
            {
                if (change.getReplicator().getStatus().getActivityLevel().equals(Replicator.ActivityLevel.IDLE))
                {
                    Log.e("Replication Comp Log", "Scheduler Completed");
                }
                else if (change.getReplicator().getStatus().getActivityLevel().equals(Replicator.ActivityLevel.STOPPED))
                {
                    try
                    {
                        database.close();
                        Log.d("TAG", "Database closed: " + database.getPath());
                    } catch (CouchbaseLiteException e)
                    {
                        Log.d("TAG", "Exception: " + e);
                    }
                }
                else if (change.getReplicator().getStatus().getActivityLevel().equals(Replicator.ActivityLevel.OFFLINE))
                {
                    Log.d("TAG", "OFFLINE");
                }
            }
        });
        replicator.start();
    }

    public static CBRepository getInstance()
    {
        return instance;
    }

    private void executeQuery(Query query)
    {
        try
        {
            query.execute();
        }
        catch (CouchbaseLiteException e)
        {
            Log.d("TAG", "Couchbase Lite Exception: " + e.getMessage());
        }
    }

    @Override
    public void closeDatabase()
    {
        replicator.removeChangeListener(token);
        replicator.stop();
        instance = null;
    }
}
