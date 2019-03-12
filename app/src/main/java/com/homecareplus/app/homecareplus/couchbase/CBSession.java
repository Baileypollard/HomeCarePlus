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

public class CBSession
{
    public static Observable<String> getSessionId(final String username, final String password, final Context context)
    {
        return Observable.fromCallable(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                String sessionJSON = postForSessionId(username, password);
                Log.d("TAG", "SESSION: " + sessionJSON);
                JSONObject results = new JSONObject(sessionJSON);
                String sessionId = results.getString("session_id");
                CouchbaseRepository.init(context, username, sessionId);

                return sessionId;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static String postForSessionId(String id, String password) throws JSONException
    {
        OkHttpClient client = NetworkUtil.createAuthenticatedClient(id, password);

        String url = "http://192.168.2.24:8080/rest/secured/login";

        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jo = new JSONObject();

        jo.put("username", id);
        jo.put("password", password);

        RequestBody body = RequestBody.create(JSON, jo.toString());
        try
        {
            return NetworkUtil.doPostRequest(client, url, body).body().string();
        }
        catch (IOException e)
        {
            return "Cannot Receive Session ID";
        }
    }

}
