package com.whiteursa.andromate.news;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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
    private NewsActivity activity;

    NewsAsyncTask(NewsActivity activity) {
        this.activity = activity;
    }
    @Override
    protected Void doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();

        String language = strings[0];
        String url = String.format("https://newscatcher.p.rapidapi.com/v1/search_free?q=news&lang=%s&media=False",
                language);
        Request request = new Request.Builder()
                .url(url)
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
        if (response.isSuccessful()) {
            ArrayList<String> articleTitles = new ArrayList<>();
            ArrayList<String> articleLinks = new ArrayList<>();

            try {
                String data = response.body().string();
                Log.d("Info", data);
                if (data != null) {
                    JSONObject json = new JSONObject(data);
                    if (json.getString("status").equals("ok")) {
                        JSONArray articles = json.getJSONArray("articles");

                        for (int index = 0; index < articles.length(); index++) {
                            JSONObject article = articles.getJSONObject(index);
                            articleTitles.add(article.getString("title"));
                            articleLinks.add(article.getString("link"));
                        }
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            if (!articleTitles.isEmpty()) {

            }
        }
    }
}
