package com.whiteursa.andromate.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.whiteursa.andromate.R;
import com.whiteursa.andromate.agenda.AgendaActivity;
import com.whiteursa.andromate.news.NewsActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    String[] languages = {"af", "ar", "bg", "bn", "ca","cn", "cs","cy", "da", "de", "el", "en",
            "es", "et", "fa", "fi", "fr", "gu", "he", "hi", "hr", "hu", "id", "it", "ja", "kn",
            "ko", "lt", "lv", "mk", "ml", "mr", "ne", "nl", "no", "pa", "pl", "pt", "ro", "ru",
            "sk", "sl", "so", "sq", "sv", "sw", "ta", "te", "th", "tl", "tr","tw", "uk", "ur","vi"};

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
    }

    private void setTexts() {
        TextView city = findViewById(R.id.city);
        TextView currentTemperature = findViewById(R.id.currentTemperature);
        TextView lastUpdated = findViewById(R.id.lastUpdated);
        TextView humidity = findViewById(R.id.humidity);
        TextView windSpeed = findViewById(R.id.windSpeed);
        TextView weatherIcon = findViewById(R.id.weatherIcon);

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

        ArrayList<ArrayList<String>> data = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("data");
        assert data != null;
        city.setText(String.format("%s, %s", data.get(0).get(0), data.get(0).get(1)));

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd, MMMM", Locale.US);
        Date dateNow = new Date();
        String date = formatter.format(dateNow);
        lastUpdated.setText(date);

        currentTemperature.setText(String.format("%s°", data.get(1).get(0)));
        weatherIcon.setText(Html.fromHtml(data.get(1).get(3)));
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

    public void showSettings(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View popupView = inflater.inflate(R.layout.settings_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        Button save = popupView.findViewById(R.id.saveButton);

        View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor myEditor = myPreferences.edit();
                RadioButton male = popupView.findViewById(R.id.radioMan);

                if (male.isChecked()) {
                    myEditor.putString("GENDER", "Male");
                } else {
                    myEditor.putString("GENDER", "Female");
                }

                Spinner spinner = popupView.findViewById(R.id.languagesSpinner);
                myEditor.putInt("languagePosition", spinner.getSelectedItemPosition());
                myEditor.putString("language", languages[spinner.getSelectedItemPosition()]);

                TextView textView = popupView.findViewById(R.id.searchText);
                String textString = textView.getText().toString();

                if (!textString.equals("")) {
                    try {
                        myEditor.putString("searchText", URLEncoder.encode(textString, StandardCharsets.UTF_8.toString()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    textView.setError("Must not be null");
                    return;
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

        Spinner languagesSpinner = popupView.findViewById(R.id.languagesSpinner);

        Drawable spinnerDrawable = Objects.requireNonNull(languagesSpinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        languagesSpinner.setBackground(spinnerDrawable);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, languages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);

        int languagePosition = myPreferences.getInt("languagePosition", -1);
        if (languagePosition != -1) {
            languagesSpinner.setSelection(languagePosition);
        }

        String searchText = myPreferences.getString("searchText", "");
        if (!searchText.equals("")) {
            TextView textView = popupView.findViewById(R.id.searchText);
            try {
                textView.setText(URLDecoder.decode(searchText, StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
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
