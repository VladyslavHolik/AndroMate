package com.whiteursa.andromate.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.whiteursa.andromate.MainActivity;
import com.whiteursa.andromate.R;
import com.whiteursa.andromate.SplashActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_API = "1f0baa768f962c90a6346c6372678dce";

    @SuppressLint("StaticFieldLeak")
    private SplashActivity activity;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String city;
    private String details;
    private String currentTemperature;
    private String humidity;
    private String pressure;
    private String weatherIcon;
    private String lastUpdated;
    private String latitude;
    private String longtitude;
    private boolean noConnection = false;

    public SplashAsyncTask(SplashActivity activity, FusedLocationProviderClient mFusedLocationProviderClient) {
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

            String[] jsonData = getJSONResponse();

            if (jsonData[0] != null) {
                city = jsonData[0];
                details = jsonData[1];
                currentTemperature = jsonData[2];
                humidity = "Humidity : " + jsonData[3];
                pressure = "Pressure : " + jsonData[4];
                lastUpdated = jsonData[5];
                weatherIcon = jsonData[6];
            } else {
                noConnection = true;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (noConnection) {
            activity.showNoConnection();
        } else {
            Intent myIntent = new Intent(activity, MainActivity.class);

            myIntent.putExtra("latitude", this.latitude);
            myIntent.putExtra("longtitude", this.longtitude);
            myIntent.putExtra("city", this.city);
            myIntent.putExtra("details", this.details);
            myIntent.putExtra("currentTemperature", this.currentTemperature);
            myIntent.putExtra("humidity", this.humidity);
            myIntent.putExtra("pressure", this.pressure);
            myIntent.putExtra("lastUpdated", this.lastUpdated);
            myIntent.putExtra("weatherIcon", this.weatherIcon);

            activity.startActivity(myIntent);
            activity.overridePendingTransition(R.anim.become_visible, R.anim.become_invisible);
            activity.finish();
        }

    }

    private String[] getJSONResponse() {
        String[] jsonData = new String[7];
        JSONObject jsonWeather = null;
        try {
            jsonWeather = getWeatherJson(latitude, longtitude);
        } catch (Exception e) {
            Log.d("Error", "Cannot get JSON results", e);
        }

        try {
            if (jsonWeather != null) {
                JSONObject details = jsonWeather.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonWeather.getJSONObject("main");
                DateFormat df = DateFormat.getDateInstance();

                String city = jsonWeather.getString("name") + ", " +
                        jsonWeather.getJSONObject("sys").getString("country");
                String description = details.getString("description").toLowerCase(Locale.US);
                String temperature = String.format(Locale.US, "%.0f", main.getDouble("temp"));
                String humidity = main.getString("humidity") + "%";
                String pressure = main.getString("pressure") + " hPa";
                String lastUpdated = df.format(new Date(jsonWeather.getLong("dt")*1000));
                String icon = setWeatherIcon(details.getInt("id"), jsonWeather.getJSONObject("sys").getLong("sunrise")*1000,
                        jsonWeather.getJSONObject("sys").getLong("sunset")*1000);

                jsonData[0] = city;
                jsonData[1] = description;
                jsonData[2] = temperature;
                jsonData[3] = humidity;
                jsonData[4] = pressure;
                jsonData[5] = lastUpdated;
                jsonData[6] = icon;
            }
        } catch (Exception ex) {
            Log.e("Error", "Error with parsing json data");
        }
        return jsonData;
    }

    private String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId/100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch (id) {
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xf01c;";
                    break;
                case 7:
                    icon = "&#xf014;";
                    break;
                case 8:
                    icon = "&#xf013;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
            }
        }
        return icon;
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
