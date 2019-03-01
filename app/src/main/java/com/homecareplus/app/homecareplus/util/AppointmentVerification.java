package com.homecareplus.app.homecareplus.util;

import android.os.AsyncTask;
import android.util.Log;

import com.homecareplus.app.homecareplus.model.Appointment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AppointmentVerification
{
    public static class VerifyAppointment extends AsyncTask<Void, Void, Boolean>
    {
        private String username;
        private String password;
        private Appointment appointment;

        public VerifyAppointment(Appointment appointment)
        {
            this.username = SharedPreference.getSharedInstance(null).getEmployeeId();
            this.password = SharedPreference.getSharedInstance(null).getEmployeePassword();
            this.appointment = appointment;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            OkHttpClient client = NetworkUtil.createAuthenticatedClient(username, password);
            String url = "http://10.0.2.2:8080/rest/secured/verify";


            final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            JSONObject jo = JsonDocCreator.createAppointmentJSON(appointment);

            RequestBody body = RequestBody.create(JSON, jo.toString());

            try
            {
                NetworkUtil.doPostRequest(client, url, body);
                return true;
            }
            catch (IOException e)
            {
                Log.d("TAG", "Failed: " + e.getMessage());
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result)
        {

        }
    }
}

