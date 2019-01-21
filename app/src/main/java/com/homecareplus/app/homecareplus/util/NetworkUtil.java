package com.homecareplus.app.homecareplus.util;

import android.os.AsyncTask;
import android.util.Log;

import com.homecareplus.app.homecareplus.callback.LoginAttemptedCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class NetworkUtil {

    public static class getSessionId extends AsyncTask<Void, Void, String>
    {
        private String user;
        private String password;
        private LoginAttemptedCallback loginAttemptedCallback;

        public getSessionId(String user, String password, LoginAttemptedCallback loginAttemptedCallback)
        {
            this.loginAttemptedCallback = loginAttemptedCallback;
            this.user = user;
            this.password = password;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            return getSessionId(user, password);
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.d("TAG", "Session id: " + result);

            try
            {
                JSONObject results = new JSONObject(result);
                String sessionId = results.getString("session_id");
                if (sessionId != null)
                {
                    loginAttemptedCallback.onLoginSuccess(user, sessionId);
                }
            }
            catch (JSONException e)
            {
                loginAttemptedCallback.onLoginFailed();
            }
        }
    }

    private static String getSessionId(String id, String password)
    {
        OkHttpClient client = createAuthenticatedClient(id, password);
        String url = "http://10.0.2.2:8080/rest/secured/login";

        final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jo = new JSONObject();
        try
        {
            jo.put("username", id);
            jo.put("password", password);

            RequestBody body = RequestBody.create(JSON, jo.toString());

            try
            {
                return doPostRequest(client, url, body).body().string();
            }
            catch (IOException e)
            {
                Log.d("TAG", "Failed: " + e.getMessage());
            }
        }
        catch (JSONException e)
        {

        }

        return "N/A";
    }


    private static Response doPostRequest(OkHttpClient httpClient, String anyURL, RequestBody body) throws IOException
    {
        Request request = new Request.Builder().url(anyURL).post(body).build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful())
        {
            throw new IOException("Unexpected code " + response);
        }

        return response;
    }

    private static OkHttpClient createAuthenticatedClient(final String username, final String password)
    {
        return new OkHttpClient.Builder().authenticator(new Authenticator()
        {
            public Request authenticate(Route route, Response response) throws IOException
            {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        }).build();
    }
}
