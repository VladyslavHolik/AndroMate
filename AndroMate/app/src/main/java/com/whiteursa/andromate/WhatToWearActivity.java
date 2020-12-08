package com.whiteursa.andromate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.whiteursa.andromate.weather.ClothesChooser;

import java.util.Objects;

public class WhatToWearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.what_to_wear);

        findViewById(R.id.listOfClothes).setOnTouchListener(new OnSwipeTouchListener(findViewById(R.id.listOfClothes).getContext()) {
            public void onSwipeRight() {
                Intent intent = new Intent(WhatToWearActivity.this, MainActivity.class);
                intent.putExtra("city", getIntent().getStringExtra("city"));
                intent.putExtra("details", getIntent().getStringExtra("details"));
                intent.putExtra("currentTemperature", getIntent().getStringExtra("currentTemperature"));
                intent.putExtra("humidity", getIntent().getStringExtra("humidity"));
                intent.putExtra("pressure", getIntent().getStringExtra("pressure"));
                intent.putExtra("lastUpdated", getIntent().getStringExtra("lastUpdated"));
                intent.putExtra("weatherIcon",getIntent().getStringExtra("weatherIcon"));
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        drawClothes();
    }

    void drawClothes() {
        String temperature = getIntent().getStringExtra("currentTemperature");

        ClothesChooser chooser = new ClothesChooser();

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(WhatToWearActivity.this);
        String gender = myPreferences.getString("GENDER", "Unknown");

        boolean forMan = false;
        if (gender.equals("Male")) {
            forMan = true;
        }
        assert temperature != null;
        String[] whatToWear = chooser.getClothes(Integer.parseInt(temperature), forMan);

        ListView listOfClothes = findViewById(R.id.listOfClothes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, whatToWear) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView= view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                return view;
            }
            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };

        listOfClothes.setAdapter(adapter);
    }
}
