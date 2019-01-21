package com.homecareplus.app.homecareplus.util;

import android.os.AsyncTask;
import android.util.Log;

import com.homecareplus.app.homecareplus.callback.LoginAttemptedCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class NetworkUtil {


    public static Response doGetRequest(OkHttpClient httpClient, String anyURL)
    {
        Request request = new Request.Builder().url(anyURL).get().build();

        try
        {
            return httpClient.newCall(request).execute();
        }
        catch (IOException e)
        {
            Log.e("TAG", "Get request failed... " + e.getMessage());
            return null;
        }
    }

    public static Response doPostRequest(OkHttpClient httpClient, String anyURL, RequestBody body) throws IOException
    {
        Request request = new Request.Builder().url(anyURL).post(body).build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful())
        {
            throw new IOException("Unexpected code " + response);
        }

        return response;
    }

    public static OkHttpClient createAuthenticatedClient(final String username, final String password)
    {
        return new OkHttpClient.Builder().authenticator(new Authenticator()
        {
            public Request authenticate(Route route, Response response) throws IOException
            {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUrl createHttpURL(String scheme, String host, String pathSegment, Map<String, String> parameterMap)
    {
            HttpUrl.Builder httpBuilder = new HttpUrl.Builder()
                    .scheme(scheme)
                    .host(host)
                    .addPathSegments(pathSegment);

            Iterator it = parameterMap.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry) it.next();
                httpBuilder.addQueryParameter(pair.getKey().toString(), pair.getValue().toString());
                it.remove(); // avoids a ConcurrentModificationException
            }
        return httpBuilder.build();
    }
}
