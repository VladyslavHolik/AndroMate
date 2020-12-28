package com.whiteursa.andromate.news;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class NewsAsyncTask extends AsyncTask<String, Void, Void> {
    private Response response;

    @Override
    protected Void doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://newscatcher.p.rapidapi.com/v1/sources?topic=news&lang=en")
                .get()
                .addHeader("x-rapidapi-key", "00f2b6820bmsha803b73bca7a5cbp16f38fjsnc2da1e2a5746")
                .addHeader("x-rapidapi-host", "newscatcher.p.rapidapi.com")
                .build();

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (response.body() != null) {
            ArrayList<String> articleTitles = new ArrayList<>();
            ArrayList<String> articleLinks = new ArrayList<>();

            try {
                JSONObject json = new JSONObject(response.body().string());
                if (json.getString("status").equals("ok")) {
                    JSONArray articles = json.getJSONArray("articles");

                    for (int index = 0; index < articles.length(); index++) {
                        JSONObject article = articles.getJSONObject(index);
                        articleTitles.add(article.getString("title"));
                        articleLinks.add(article.getString("link"));
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

        }
    }
}
