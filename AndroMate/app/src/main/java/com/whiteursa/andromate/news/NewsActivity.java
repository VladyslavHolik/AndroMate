package com.whiteursa.andromate.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.whiteursa.andromate.weather.MainActivity;
import com.whiteursa.andromate.R;

import java.util.Objects;


public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setProgressBar();
        findNews();

        findViewById(R.id.newsList).setOnTouchListener(new OnSwipeTouchForNewsListener(findViewById(R.id.newsList).getContext()) {
            @Override
            public void onSwipeRight() {
                Intent intent = new Intent(NewsActivity.this, MainActivity.class);
                setIntentProperties(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
            }
        });
    }

    private void setProgressBar()  {
        ProgressBar bar = findViewById(R.id.progressBar);
        bar.setVisibility(ProgressBar.VISIBLE);
    }
    private void findNews() {
        NewsAsyncTask task = new NewsAsyncTask(this);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(NewsActivity.this);
        String language = myPreferences.getString("language", "en");
        String searchText = myPreferences.getString("searchText", "news");
        task.execute(language, searchText);
    }

    private void setIntentProperties(Intent intent) {
        intent.putExtra("city", getIntent().getStringExtra("city"));
        intent.putExtra("details", getIntent().getStringExtra("details"));
        intent.putExtra("currentTemperature", getIntent().getStringExtra("currentTemperature"));
        intent.putExtra("humidity", getIntent().getStringExtra("humidity"));
        intent.putExtra("pressure", getIntent().getStringExtra("pressure"));
        intent.putExtra("lastUpdated", getIntent().getStringExtra("lastUpdated"));
        intent.putExtra("weatherIcon",getIntent().getStringExtra("weatherIcon"));
    }
}
