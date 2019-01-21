package com.homecareplus.app.homecareplus.util;

import android.util.Log;

import com.couchbase.lite.LogDomain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    public static String getHeaderDateFormat(String date)
    {
        long dateMs = getDateMilliseconds(date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd YYYY");

        Date d = new Date(dateMs);
        return sdf.format(d);
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
