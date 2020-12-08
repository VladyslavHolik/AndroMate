package com.whiteursa.andromate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.whiteursa.andromate.splash.SplashAsyncTask;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class SplashActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestPermissions();

        FusedLocationProviderClient mFusedLocationProviderClient;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SplashAsyncTask task = new SplashAsyncTask(this, mFusedLocationProviderClient);
        task.execute();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
