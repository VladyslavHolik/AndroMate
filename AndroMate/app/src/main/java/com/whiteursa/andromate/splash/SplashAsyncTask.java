package com.whiteursa.andromate.splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.whiteursa.andromate.MainActivity;
import com.whiteursa.andromate.SplashActivity;

public class SplashAsyncTask extends AsyncTask<Void, Void, Void> {
    private SplashActivity activity;

    public SplashAsyncTask(SplashActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(myIntent);
                activity.finish();
            }
        }, 4000);
    }
}
