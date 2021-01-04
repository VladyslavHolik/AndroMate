package com.whiteursa.andromate.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.whiteursa.andromate.R;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private boolean jobStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        requestPermissions();

        if ((ActivityCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) && (!jobStarted)) {
                startJob();
                jobStarted = true;
        }
    }

    private void startJob() {
        FusedLocationProviderClient mFusedLocationProviderClient;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SplashAsyncTask task = new SplashAsyncTask(this, mFusedLocationProviderClient);
        task.execute();
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    this.finishAffinity();
                }
            }
            if (!jobStarted) {
                startJob();
                jobStarted = true;
            }
        }
    }

    public void showNoConnection() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = findViewById(R.id.splashActivity);
        @SuppressLint("InflateParams") final View popupView = inflater.inflate(R.layout.no_connection_popup, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, false);

        Button ok = popupView.findViewById(R.id.noConnectionButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        ok.setOnClickListener(listener);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
}
