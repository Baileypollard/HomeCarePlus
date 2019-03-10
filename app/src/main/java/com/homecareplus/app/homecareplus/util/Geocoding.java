package com.homecareplus.app.homecareplus.util;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Geocoding
{
    public static Observable<LatLng> getLatLngForAddress(final String address, final String apiKey)
    {
        return Observable.fromCallable(new Callable<LatLng>()
        {
            @Override
            public LatLng call() throws Exception
            {
                JSONObject geoJSON = getGeocodeJSONForAddress(address, apiKey);
                return getLatLgnFromJSON(geoJSON);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static LatLng getLatLgnFromJSON(JSONObject jsonObject) throws JSONException
    {
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject resultsJSONObject = results.getJSONObject(0);
        JSONObject geometry = resultsJSONObject.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");

        double lng = location.getDouble("lng");
        double lat = location.getDouble("lat");

        return new LatLng(lat, lng);
    }


    private static JSONObject getGeocodeJSONForAddress(String address, String apiKey) throws IOException, JSONException
    {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Map<String, String> urlParameters = new HashMap<>();
        urlParameters.put("address", address);
        urlParameters.put("key", apiKey);

        HttpUrl url = NetworkUtil.createHttpURL("https", "maps.googleapis.com", "maps/api/geocode/json", urlParameters);

        Response response = NetworkUtil.doGetRequest(client, url.toString());
        if (response != null)
        {
            return new JSONObject(response.body().string());
        }
        return new JSONObject();
    }
}
