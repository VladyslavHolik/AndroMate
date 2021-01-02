package com.whiteursa.andromate.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whiteursa.andromate.R;
import com.whiteursa.andromate.agenda.AgendaActivity;
import com.whiteursa.andromate.news.NewsActivity;
import com.whiteursa.andromate.settingsFragment.SettingsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public final class MainActivity extends AppCompatActivity {
    private boolean isFragmentDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setViews();

        findViewById(R.id.weather).setOnTouchListener(new OnSwipeTouchListener(findViewById(R.id.weather).getContext()) {
            public void onSwipeLeft() {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
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
        Objects.requireNonNull(getSupportActionBar()).hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void setViews() {
        setFonts();
        setTexts();
        setIcons();
    }

    private void setFonts() {
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "font/webfont.ttf");
        Typeface cityFont = Typeface.createFromAsset(getAssets(), "font/Poppins-Medium.ttf");
        Typeface updateFont = Typeface.createFromAsset(getAssets(), "font/Poppins-Regular.ttf");
        Typeface weekdaysFont = Typeface.createFromAsset(getAssets(), "font/Montserrat-Medium.ttf");
        Typeface temperatureFont = Typeface.createFromAsset(getAssets(), "font/Montserrat-Bold.ttf");

        TextView city = findViewById(R.id.city);
        TextView currentTemperature = findViewById(R.id.currentTemperature);
        TextView lastUpdated = findViewById(R.id.lastUpdated);
        TextView humidity = findViewById(R.id.humidity);
        TextView windSpeed = findViewById(R.id.windSpeed);

        TextView weatherIcon = findViewById(R.id.weatherIcon);
        TextView nowIcon = findViewById(R.id.nowIcon);
        TextView tomorrowIcon = findViewById(R.id.tomorrowIcon);
        TextView afterTomorrowIcon = findViewById(R.id.afterTomorrowIcon);
        TextView aaTomorrowIcon = findViewById(R.id.aaTomorrowIcon);
        TextView aaaTomorrowIcon = findViewById(R.id.aaaTomorrowIcon);

        TextView nowWeekday = findViewById(R.id.nowWeekday);
        TextView tomorrowWeekday = findViewById(R.id.tomorrowWeekday);
        TextView afterTomorrowWeekday = findViewById(R.id.afterTomorrowWeekday);
        TextView aaTomorrowWeekday = findViewById(R.id.aaTomorrowWeekday);
        TextView aaaTomorrowWeekday = findViewById(R.id.aaaTomorrowWeekday);

        TextView nowTemperature = findViewById(R.id.nowTemperature);
        TextView tomorrowTemperature = findViewById(R.id.tomorrowTemperature);
        TextView afterTomorrowTemperature = findViewById(R.id.afterTomorrowTemperature);
        TextView aaTomorrowTemperature = findViewById(R.id.aaTomorrowTemperature);
        TextView aaaTomorrowTemperature = findViewById(R.id.aaaTomorrowTemperature);

        city.setTypeface(cityFont);
        lastUpdated.setTypeface(updateFont);
        currentTemperature.setTypeface(temperatureFont);
        windSpeed.setTypeface(weekdaysFont);
        humidity.setTypeface(weekdaysFont);

        nowWeekday.setTypeface(weekdaysFont);
        tomorrowWeekday.setTypeface(weekdaysFont);
        afterTomorrowWeekday.setTypeface(weekdaysFont);
        aaTomorrowWeekday.setTypeface(weekdaysFont);
        aaaTomorrowWeekday.setTypeface(weekdaysFont);

        nowTemperature.setTypeface(weekdaysFont);
        tomorrowTemperature.setTypeface(weekdaysFont);
        afterTomorrowTemperature.setTypeface(weekdaysFont);
        aaTomorrowTemperature.setTypeface(weekdaysFont);
        aaaTomorrowTemperature.setTypeface(weekdaysFont);

        weatherIcon.setTypeface(weatherFont);
        nowIcon.setTypeface(weatherFont);
        tomorrowIcon.setTypeface(weatherFont);
        afterTomorrowIcon.setTypeface(weatherFont);
        aaTomorrowIcon.setTypeface(weatherFont);
        aaaTomorrowIcon.setTypeface(weatherFont);
    }

    private void setTexts() {
        TextView city = findViewById(R.id.city);
        TextView currentTemperature = findViewById(R.id.currentTemperature);
        TextView lastUpdated = findViewById(R.id.lastUpdated);
        TextView humidity = findViewById(R.id.humidity);
        TextView windSpeed = findViewById(R.id.windSpeed);

        TextView nowWeekday = findViewById(R.id.nowWeekday);
        TextView tomorrowWeekday = findViewById(R.id.tomorrowWeekday);
        TextView afterTomorrowWeekday = findViewById(R.id.afterTomorrowWeekday);
        TextView aaTomorrowWeekday = findViewById(R.id.aaTomorrowWeekday);
        TextView aaaTomorrowWeekday = findViewById(R.id.aaaTomorrowWeekday);

        TextView nowTemperature = findViewById(R.id.nowTemperature);
        TextView tomorrowTemperature = findViewById(R.id.tomorrowTemperature);
        TextView afterTomorrowTemperature = findViewById(R.id.afterTomorrowTemperature);
        TextView aaTomorrowTemperature = findViewById(R.id.aaTomorrowTemperature);
        TextView aaaTomorrowTemperature = findViewById(R.id.aaaTomorrowTemperature);

        @SuppressWarnings (value="unchecked")
        ArrayList<ArrayList<String>> data = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("data");
        assert data != null;
        city.setText(String.format("%s, %s", data.get(0).get(0), data.get(0).get(1)));

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd, MMMM", Locale.US);
        Date dateNow = new Date();
        String date = formatter.format(dateNow);
        lastUpdated.setText(date);

        currentTemperature.setText(String.format("%s°", data.get(1).get(0)));
        humidity.setText(String.format("Humidity\n%s%%", data.get(1).get(1)));
        windSpeed.setText(String.format("Wind\n%s m/s", data.get(1).get(2)));
        nowTemperature.setText(String.format("%s°", data.get(1).get(0)));
        tomorrowTemperature.setText(String.format("%s°", data.get(2).get(0)));
        afterTomorrowTemperature.setText(String.format("%s°", data.get(3).get(0)));
        aaTomorrowTemperature.setText(String.format("%s°", data.get(4).get(0)));
        aaaTomorrowTemperature.setText(String.format("%s°", data.get(5).get(0)));

        formatter = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        String now = formatter.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String tomorrow = formatter.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String afterTomorrow = formatter.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String aaTomorrow = formatter.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String aaaTomorrow = formatter.format(calendar.getTime());

        nowWeekday.setText(now);
        tomorrowWeekday.setText(tomorrow);
        afterTomorrowWeekday.setText(afterTomorrow);
        aaTomorrowWeekday.setText(aaTomorrow);
        aaaTomorrowWeekday.setText(aaaTomorrow);
    }

    private void setIcons() {
        TextView weatherIcon = findViewById(R.id.weatherIcon);
        TextView nowIcon = findViewById(R.id.nowIcon);
        TextView tomorrowIcon = findViewById(R.id.tomorrowIcon);
        TextView afterTomorrowIcon = findViewById(R.id.afterTomorrowIcon);
        TextView aaTomorrowIcon = findViewById(R.id.aaTomorrowIcon);
        TextView aaaTomorrowIcon = findViewById(R.id.aaaTomorrowIcon);

        @SuppressWarnings (value="unchecked")
        ArrayList<ArrayList<String>> data = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("data");
        assert data != null;

        weatherIcon.setText(Html.fromHtml(data.get(1).get(3)));
        nowIcon.setText(Html.fromHtml(data.get(1).get(3)));
        tomorrowIcon.setText(Html.fromHtml(data.get(2).get(3)));
        afterTomorrowIcon.setText(Html.fromHtml(data.get(3).get(3)));
        aaTomorrowIcon.setText(Html.fromHtml(data.get(4).get(3)));
        aaaTomorrowIcon.setText(Html.fromHtml(data.get(5).get(3)));
    }

    public void showSettings(View view) {
        if (!isFragmentDisplayed) {
            displayFragment();
        } else {
            closeFragment();
        }
    }

    private void displayFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        fragment.setActivity(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.down_to_center, R.anim.down_to_center);
        fragmentTransaction.add(R.id.fragment_containter, fragment).addToBackStack(null).commit();
        isFragmentDisplayed = true;
    }

     public void closeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SettingsFragment fragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.fragment_containter);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment).commit();
        }
        isFragmentDisplayed = false;
    }

    private void setIntentProperties(Intent intent) {
        intent.putExtra("data", getIntent().getSerializableExtra("data"));
    }
}
