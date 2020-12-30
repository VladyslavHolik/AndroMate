package com.whiteursa.andromate.news;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.whiteursa.andromate.R;

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
    private ArrayList<String> articleTitles;
    private ArrayList<String> articleLinks;
    @SuppressLint("StaticFieldLeak")
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
            articleTitles = new ArrayList<>();
            articleLinks = new ArrayList<>();

            try {
                assert response.body() != null;
                String data = response.body().string();
                Log.d("Info", data);
                JSONObject json = new JSONObject(data);
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

            if (!articleTitles.isEmpty()) {
                setNews();
                putDataIntoDB();
            }
        } else {
            getDataFromDB();
        }
    }

    private void putDataIntoDB() {
        SQLiteDatabase AgendaDB = activity.openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);
        AgendaDB.execSQL("CREATE TABLE IF NOT EXISTS articles (title VARCHAR(200), link VARCHAR(200))");

        AgendaDB.delete("articles",  null, null);
        for (int index = 0; index < articleTitles.size(); index++) {
            ContentValues data = new ContentValues();
            data.put("title", articleTitles.get(index));
            data.put("link", articleLinks.get(index));
            AgendaDB.insert("articles", null, data);
        }
        AgendaDB.close();
    }

    private void getDataFromDB() {
        SQLiteDatabase AgendaDB = activity.openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);
        Cursor myCursor = AgendaDB.rawQuery(
                "SELECT * FROM articles;", null);

        articleTitles = new ArrayList<>();
        articleLinks = new ArrayList<>();

        while (myCursor.moveToNext()) {
            articleTitles.add(myCursor.getString(0));
            articleLinks.add(myCursor.getString(1));
        }

        myCursor.close();
        setNews();
    }

    private void setNews() {
        ListView newsList = activity.findViewById(R.id.newsList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, articleTitles) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                final Typeface fontList = Typeface.createFromAsset(activity.getAssets(), "font/OpenSans-Light.ttf");
                textView.setTypeface(fontList);
                textView.setTextSize(20);
                textView.setPadding(25, 15,25,15);
                return view;
            }

        };

        removeProgressBar();
        newsList.setAdapter(adapter);

        setListener(newsList);
    }

    private void removeProgressBar() {
        ProgressBar bar = activity.findViewById(R.id.progressBar);
        bar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void setListener(ListView list) {
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.indexOfChild(view);
                if (index != -1) {
                    String url = articleLinks.get(index);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }
                return false;
            }
        });
    }
}
