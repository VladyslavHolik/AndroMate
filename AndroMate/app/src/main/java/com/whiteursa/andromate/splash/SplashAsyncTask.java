package com.whiteursa.andromate.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.whiteursa.andromate.weather.GetWeather;
import com.whiteursa.andromate.weather.MainActivity;
import com.whiteursa.andromate.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SplashAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_API = "1f0baa768f962c90a6346c6372678dce";

    @SuppressLint("StaticFieldLeak")
    private SplashActivity activity;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ArrayList<ArrayList<String>> data;
    private String latitude;
    private String longtitude;
    private boolean noConnection = false;

    SplashAsyncTask(SplashActivity activity, FusedLocationProviderClient mFusedLocationProviderClient) {
        this.activity = activity;
        this.mFusedLocationProviderClient = mFusedLocationProviderClient;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Task<Location> getLocationTask = mFusedLocationProviderClient.getLastLocation();

        while (!getLocationTask.isComplete()) {}
        Location location = getLocationTask.getResult();
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longtitude = String.valueOf(location.getLongitude());

            ArrayList<ArrayList<String>> jsonData = getJSONResponse();

            if (jsonData != null) {
                data = jsonData;
            } else {
                noConnection = true;
            }
        }
        SQLiteDatabase AgendaDB = activity.openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);
        AgendaDB.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (noConnection) {
            activity.showNoConnection();
        } else {
            Intent myIntent = new Intent(activity, MainActivity.class);

            myIntent.putExtra("data", data);

            activity.startActivity(myIntent);
            activity.overridePendingTransition(R.anim.become_visible, R.anim.become_invisible);
            activity.finish();
        }

    }

    private ArrayList<ArrayList<String>> getJSONResponse() {
        ArrayList<ArrayList<String>> jsonData = null;
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJson(latitude, longtitude);
        } catch (Exception e) {
            Log.d("Error", "Cannot get JSON results", e);
        }

        if (jsonWeather != null) {
            GetWeather weatherGetter = new GetWeather();
            jsonData = weatherGetter.getDataArray(jsonWeather);
        }

        return jsonData;
    }



    private JSONObject getWeatherJson(String latitude, String longtitude) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_URL, latitude, longtitude));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", OPEN_WEATHER_API);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder(1024);
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception ex) {
            return null;
        }
    }
}
