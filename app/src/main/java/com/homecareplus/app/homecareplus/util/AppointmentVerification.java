package com.homecareplus.app.homecareplus.util;

import android.content.Context;

import com.homecareplus.app.homecareplus.model.Appointment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentVerification
{
    public static Observable<Boolean> verifyAppointment(final Appointment appointment, final Context context)
    {
        return Observable.fromCallable(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                String username = SharedPreference.getSharedInstance(context).getEmployeeId();
                String password = SharedPreference.getSharedInstance(context).getEmployeePassword();

                OkHttpClient client = NetworkUtil.createAuthenticatedClient(username, password);
                String url = "http://130.211.238.195:8080/rest/appointment/verify";
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject jo = JsonDocCreator.createAppointmentJSON(appointment);
                RequestBody body = RequestBody.create(JSON, jo.toString());

                Response response = NetworkUtil.doPostRequest(client, url, body);

                return response.isSuccessful();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

