package com.homecareplus.app.homecareplus.util;

import android.text.format.DateUtils;
import android.util.Log;

import com.couchbase.lite.LogDomain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateUtil
{
    public static String getHeaderDateFormat(String date)
    {
        long dateMs = getDateMilliseconds(date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy");
        Date d = new Date(dateMs);

        if (DateUtils.isToday(dateMs))
        {
            return "TODAY - " + sdf.format(d);
        }

        return sdf.format(d);
    }

    public static String getStartedAppointmentFormat(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public static String getTodayFormatted()
    {
        long dateMs = new Date().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(dateMs);
    }

    public static long getDateMilliseconds(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date d = sdf.parse(date);
            return d.getTime();
        }
        catch (ParseException e)
        {
            Log.d("TAG","Parse failed getting ms");
        }
        return new Date().getTime();
    }

}
