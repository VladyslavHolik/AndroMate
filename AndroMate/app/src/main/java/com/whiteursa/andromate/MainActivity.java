package com.whiteursa.andromate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.whiteursa.andromate.agenda.AgendaActivity;

public class MainActivity extends AppCompatActivity {
    TextView city, details, currentTemperature, humidity, pressure, weatherIcon, lastUpdated;
    Typeface weatherFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setViews();

        findViewById(R.id.weather).setOnTouchListener(new OnSwipeTouchListener(findViewById(R.id.weather).getContext()) {
            public void onSwipeLeft() {
                Intent intent = new Intent(MainActivity.this, WhatToWearActivity.class);
                setIntentProperties(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
            }

            public void onSwipeRight() {
                Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
                setIntentProperties(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setViews() {
        weatherFont = Typeface.createFromAsset(getAssets(), "font/webfont.ttf");

        city = findViewById(R.id.city);
        currentTemperature = findViewById(R.id.currentTemperature);
        lastUpdated = findViewById(R.id.lastUpdated);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        details = findViewById(R.id.details);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherIcon.setTypeface(weatherFont);

        city.setText(getIntent().getStringExtra("city"));
        details.setText(getIntent().getStringExtra("details"));
        currentTemperature.setText(getIntent().getStringExtra("currentTemperature")+ "°");
        humidity.setText(getIntent().getStringExtra("humidity"));
        pressure.setText(getIntent().getStringExtra("pressure"));
        lastUpdated.setText(getIntent().getStringExtra("lastUpdated"));
        weatherIcon.setText(Html.fromHtml(getIntent().getStringExtra("weatherIcon")));
    }
    public void showSettings(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.settings_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        Button save = popupView.findViewById(R.id.saveButton);

        View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor myEditor = myPreferences.edit();
                RadioButton male = popupView.findViewById(R.id.radioMan);
                RadioButton female = popupView.findViewById(R.id.radioWoman);

                if (male.isChecked()) {
                    myEditor.putString("GENDER", "Male");
                } else {
                    myEditor.putString("GENDER", "Female");
                }

                myEditor.apply();
                popupWindow.dismiss();
            }
        };

        save.setOnClickListener(saveListener);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String gender = myPreferences.getString("GENDER", "Unknown");
        if (!gender.equals("Unknown") ) {
            if (gender.equals("Male") ) {
                RadioButton male = popupView.findViewById(R.id.radioMan);
                male.setChecked(true);
            } else {
                RadioButton female = popupView.findViewById(R.id.radioWoman);
                female.setChecked(true);
            }
        }

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });


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
