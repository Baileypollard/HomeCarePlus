package com.homecareplus.app.homecareplus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreference
{
    private Context context;
    private SharedPreferences sharedPreference;
    private static SharedPreference instance;

    private final String SHARED_PREF_KEY = "SHARED_PREFERENCES";
    private final String KEY_EMPLOYEE_ID = "KEY_EMPLOYEE_ID";
    private final String KEY_EMPLOYEE_PASSWORD = "KEY_EMPLOYEE_PASSWORD";
    public static final String KEY_APPOINTMENT = "KEY_APPOINTMENT";
    public static final String KEY_CLIENT = "KEY_CLIENT";
    private static final String KEY_SESSION_ID = "KEY_SESSION_ID";

    private SharedPreference(Context context)
    {
        this.context = context;
        this.sharedPreference = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }

    public void setEmployeeId(String id)
    {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(KEY_EMPLOYEE_ID, id);
        editor.apply();
    }

    public String getEmployeePassword()
    {
        return sharedPreference.getString(KEY_EMPLOYEE_PASSWORD, "");
    }

    public void setEmployeePassword(String password)
    {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(KEY_EMPLOYEE_PASSWORD, password);
        editor.apply();
    }

    public void setSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.apply();
    }

    public String getSessionId()
    {
        return sharedPreference.getString(KEY_SESSION_ID, "");
    }

    public void clear()
    {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.apply();
    }

    public String getEmployeeId()
    {
        return sharedPreference.getString(KEY_EMPLOYEE_ID, "");
    }

    public static SharedPreference getSharedInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SharedPreference(context);
        }
        return instance;
    }

}
