package com.whiteursa.andromate.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetWeather {
    public ArrayList<ArrayList<String>> getDataArray(JSONObject json) {
        try {
            ArrayList<ArrayList<String>> result = new ArrayList<>();

            ArrayList<String> generalData = new ArrayList<>();
            generalData.add(json.getJSONObject("city").getString("name"));
            generalData.add(json.getJSONObject("city").getString("country"));
            result.add(generalData);

            long sunrise = json.getJSONObject("city").getLong("sunrise");
            long sunset = json.getJSONObject("city").getLong("sunset");

            JSONArray daysData = json.getJSONArray("list");
            for (int index = 0; index < daysData.length(); index++) {
                ArrayList<String> dataAboutDay = new ArrayList<>();
                JSONObject day = daysData.getJSONObject(index);

                dataAboutDay.add(String.format(Locale.US, "%.0f",
                        day.getJSONObject("main").getDouble("temp")));
                dataAboutDay.add(String.format("%s",day.getJSONObject("main").getInt("humidity")));
                dataAboutDay.add(String.format(Locale.US,"%.0f",day.getJSONObject("wind").getDouble("speed")));
                dataAboutDay.add(parseWeatherIcon(day.getJSONArray("weather").getJSONObject(0).getInt("id"),
                        sunrise, sunset));
                dataAboutDay.add(day.getJSONArray("weather").getJSONObject(0).getString("description"));
                dataAboutDay.add(day.getString("dt_txt"));
                result.add(dataAboutDay);
            }

            return getImportantData(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<ArrayList<String>> getImportantData(ArrayList<ArrayList<String>> allData) {
        ArrayList<ArrayList<String>> importantData = new ArrayList<>();

        importantData.add(allData.get(0));
        importantData.add(allData.get(1));
        importantData.add(allData.get(9));
        importantData.add(allData.get(17));
        importantData.add(allData.get(25));
        importantData.add(allData.get(33));

        return importantData;
    }

    private String parseWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId/100;
        String icon = "";
        if (actualId == 800) {
            icon = "&#xf00d;";
            long currentTime = new Date().getTime();
            if(!(currentTime >= sunrise && currentTime < sunset)) {
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
}
