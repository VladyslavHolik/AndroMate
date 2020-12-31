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

import java.util.Objects;

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
        currentTemperature.setText(String.format("%s°",getIntent().getStringExtra("currentTemperature")));
        humidity.setText(getIntent().getStringExtra("humidity"));
        pressure.setText(getIntent().getStringExtra("pressure"));
        lastUpdated.setText(getIntent().getStringExtra("lastUpdated"));
        weatherIcon.setText(Html.fromHtml(getIntent().getStringExtra("weatherIcon")));
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

        String[] languages = {"af", "ar", "bg", "bn", "ca","cn", "cs","cy", "da", "de", "el", "en",
                "es", "et", "fa", "fi", "fr", "gu", "he", "hi", "hr", "hu", "id", "it", "ja", "kn",
                "ko", "lt", "lv", "mk", "ml", "mr", "ne", "nl", "no", "pa", "pl", "pt", "ro", "ru",
                "sk", "sl", "so", "sq", "sv", "sw", "ta", "te", "th", "tl", "tr","tw", "uk", "ur","vi"};

        Spinner languagesSpinner = popupView.findViewById(R.id.languagesSpinner);

        Drawable spinnerDrawable = Objects.requireNonNull(languagesSpinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        languagesSpinner.setBackground(spinnerDrawable);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, languages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);

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
