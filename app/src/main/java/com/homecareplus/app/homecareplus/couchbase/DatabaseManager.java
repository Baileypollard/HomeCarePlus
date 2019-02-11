package com.homecareplus.app.homecareplus.couchbase;

import android.content.Context;
import android.se.omapi.Session;
import android.util.Log;

import com.couchbase.lite.BasicAuthenticator;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorChange;
import com.couchbase.lite.ReplicatorChangeListener;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.SessionAuthenticator;
import com.couchbase.lite.URLEndpoint;

import java.net.URI;
import java.net.URISyntaxException;

public class DatabaseManager
{
    private static DatabaseManager instance;
    private static Database database;
    private static Replicator replicator;
    private static ListenerToken token;

    private DatabaseManager(Context context, String employeeId, String sessionId)
    {
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        try
        {
            database = new Database("appointments-" + employeeId, config);
            beginDatabaseReplication(sessionId);
        }
        catch (CouchbaseLiteException e)
        {
            e.printStackTrace();
        }
    }

    public static void beginDatabaseReplication(String sessionId)
    {
        URI url = null;
        try
        {
            url = new URI("ws://35.235.100.173:4984/homecareplus");
        }
        catch (URISyntaxException e)
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
                if (change.getReplicator().getStatus().getActivityLevel().equals(Replicator.ActivityLevel.STOPPED))
                {
                    try
                    {
                        database.close();
                        Log.d("TAG","Database deleted: " + database.getPath());
                    }
                    catch (CouchbaseLiteException e)
                    {
                        Log.d("TAG", "Exception: " + e);
                    }
                }
            }
        });
        replicator.start();
    }

    public static void closeDatabase() throws CouchbaseLiteException
    {
        replicator.removeChangeListener(token);
        replicator.stop();
        instance = null;
    }

    public static DatabaseManager getSharedInstance(Context context, String employeeId, String sessionId)
    {
        if (instance == null)
        {
            instance = new DatabaseManager(context, employeeId, sessionId);
        }
        return instance;
    }

    public static Database getDatabase()
    {
        return database;
    }

}
