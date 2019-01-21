package com.homecareplus.app.homecareplus.util;

import android.os.AsyncTask;
import android.util.Log;

import com.couchbase.lite.internal.utils.JsonUtils;
import com.google.android.gms.maps.model.LatLng;
import com.homecareplus.app.homecareplus.callback.CoordinatesReceivedCallback;
import com.homecareplus.app.homecareplus.callback.LoginAttemptedCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Geocoding
{
    public static class getLatAndLngForAddress extends AsyncTask<Void, Void, JSONObject>
    {
        private String address;
        private String apiKey;
        private CoordinatesReceivedCallback callback;

        public getLatAndLngForAddress(String address, String apiKey, CoordinatesReceivedCallback callback)
        {
            this.address = address;
            this.apiKey = apiKey;
            this.callback = callback;
        }

        @Override
        protected JSONObject doInBackground(Void... params)
        {
            return getGeocodeJSONForAddress(address, apiKey);
        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            LatLng latLongAddressLocation = getLatLgnFromJSON(result);
            callback.onCoordinatesReceived(latLongAddressLocation);
        }
    }

    private static LatLng getLatLgnFromJSON(JSONObject jsonObject)
    {
        try
        {
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject resultsJSONObject = results.getJSONObject(0);
            JSONObject geometry = resultsJSONObject.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");

            double lng = location.getDouble("lng");
            double lat = location.getDouble("lat");

            return new LatLng(lat,lng);
        }
        catch (JSONException e)
        {
            Log.e("TAG", "Could not retrieve lat and lng values from JSON " + e.getLocalizedMessage());
        }
        return new LatLng(45.617,  -61.349); // Default to Lat/Long coordinates for Port Hawkesbury...
    }


    private static JSONObject getGeocodeJSONForAddress(String address, String apiKey)
    {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("address", address);
        urlParameters.put("key", apiKey);

        HttpUrl url = NetworkUtil.createHttpURL("https", "maps.googleapis.com", "maps/api/geocode/json", urlParameters);

        try
        {
            Response response = NetworkUtil.doGetRequest(client, url.toString());
            if (response != null)
            {
                return new JSONObject(response.body().string());
            }
        }
        catch (Exception  e)
        {
            Log.d("TAG", "Error occurred: " + e.getMessage());
        }
        return new JSONObject();
    }
}
