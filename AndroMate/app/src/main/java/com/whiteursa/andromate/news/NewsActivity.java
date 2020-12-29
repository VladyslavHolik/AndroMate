package com.whiteursa.andromate.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

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
    }

    private void setProgressBar()  {
        ProgressBar bar = findViewById(R.id.progressBar);
        bar.setVisibility(ProgressBar.VISIBLE);
    }
    private void findNews() {
        NewsAsyncTask task = new NewsAsyncTask(this);
        task.execute("en");
    }
}
