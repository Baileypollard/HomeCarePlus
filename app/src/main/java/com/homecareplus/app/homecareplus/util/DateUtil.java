package com.homecareplus.app.homecareplus.util;

import android.text.format.DateUtils;
import android.util.Log;

import com.couchbase.lite.LogDomain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static String calculateMinutesBetweenDates(String date1, String date2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss");

        if (date1 == null || date2 == null)
        {
            return "N/A";
        }

        try
        {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);

            long diff = Math.abs(d1.getTime() - d2.getTime());
            return Long.toString(TimeUnit.MILLISECONDS.toMinutes(diff)) + " minutes "
                    + Long.toString(TimeUnit.MILLISECONDS.toSeconds(diff) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))) + " seconds ";
        } catch (ParseException e)
        {
            Log.d("TAG", "Parse failed getting ms");
        }
        return "";
    }

    public static String getTodayFormatted()
    {
        long dateMs = new Date().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(dateMs);
    }

    public static String getTimeFromMs(long ms)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date date = new Date(ms);

        return sdf.format(date);
    }

    public static long getDateMilliseconds(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date d = sdf.parse(date);
            return d.getTime();
        } catch (ParseException e)
        {
            Log.d("TAG", "Parse failed getting ms");
        }
        return new Date().getTime();
    }

}
