package com.homecareplus.app.homecareplus.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference
{
    private Context context;
    private SharedPreferences sharedPreference;
    private static SharedPreference instance;

    private final String SHARED_PREF_KEY = "SHARED_PREFERENCES";
    private final String KEY_EMPLOYEE_ID = "KEY_EMPLOYEE_ID";

    public SharedPreference(Context context)
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
