package com.homecareplus.app.homecareplus.util;

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

public class AppointmentVerification
{
    public static Observable<Boolean> verifyAppointment(final Appointment appointment)
    {
        return Observable.fromCallable(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                String username = SharedPreference.getSharedInstance().getEmployeeId();
                String password = SharedPreference.getSharedInstance().getEmployeePassword();

                OkHttpClient client = NetworkUtil.createAuthenticatedClient(username, password);
                String url = "http://192.168.2.24:8080/rest/secured/verify";
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                JSONObject jo = JsonDocCreator.createAppointmentJSON(appointment);
                RequestBody body = RequestBody.create(JSON, jo.toString());
                try
                {
                    NetworkUtil.doPostRequest(client, url, body);
                    return true;
                }
                catch (IOException e)
                {
                    return false;
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

