package com.homecareplus.app.homecareplus.couchbase;

import android.content.Context;
import android.util.Log;

import com.homecareplus.app.homecareplus.util.NetworkUtil;

import org.json.JSONException;
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

public class CBSession
{
    public static Observable<Response> getSessionId(final String username, final String password)
    {
        return Observable.fromCallable(new Callable<Response>()
        {
            @Override
            public Response call() throws Exception
            {
                return postForSessionId(username, password);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static Response postForSessionId(String id, String password) throws JSONException , IOException
    {
        OkHttpClient client = NetworkUtil.createAuthenticatedClient(id, password);

        String url = "https://homecare-plus.herokuapp.com/rest/user/login";

        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jo = new JSONObject();

        jo.put("username", id);
        jo.put("password", password);

        RequestBody body = RequestBody.create(JSON, jo.toString());

        return NetworkUtil.doPostRequest(client, url, body);
    }
}
