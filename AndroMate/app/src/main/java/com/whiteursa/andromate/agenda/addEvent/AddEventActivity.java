package com.whiteursa.andromate.agenda.addEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.whiteursa.andromate.R;
import com.whiteursa.andromate.agenda.AgendaActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity  {
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date dateNow = new Date();
        selectedDate = formatter.format(dateNow);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        CalendarView calendar = findViewById(R.id.dateInput);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String monthString;
                String dayString;

                if (++month < 10) {
                    monthString = "0" + month;
                } else {
                    monthString = String.valueOf(month);
                }
                if (dayOfMonth < 10) {
                    dayString = "0" + dayOfMonth;
                } else {
                    dayString = String.valueOf(dayOfMonth);
                }
                 selectedDate = String.format("%s-%s-%s", year, monthString, dayString);
            }
        });
    }

    public void onAddEventClick(View view) {
        EditText timeEdit = findViewById(R.id.timeInput);
        EditText titleEdit = findViewById(R.id.eventTitle);
        EditText descriptionEdit = findViewById(R.id.eventDescription);

        if (!isTimeValid()) {
            timeEdit.setError("hh:mm");
            return;
        }
        if (!isTitleValid()) {
            titleEdit.setError("Must not be empty");
            return;
        }

        String time = timeEdit.getText().toString();
        String title = titleEdit.getText().toString();

        if (doesEventExist(time, title)) {
            Toast.makeText(getApplicationContext(),
                    "Event already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase AgendaDB = openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);

        String description = descriptionEdit.getText().toString();

        ContentValues data = new ContentValues();
        data.put("datetime", String.format("%s %s:00.000",selectedDate, time));
        data.put("title", title);
        data.put("description", description);

        AgendaDB.insert("events", null, data);
        AgendaDB.close();

        Intent intent = new Intent(AddEventActivity.this,
                AgendaActivity.class);
        setIntentProperties(intent);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
    }

    private boolean doesEventExist(String time, String title) {
        SQLiteDatabase AgendaDB = openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);

        Cursor myCursor = AgendaDB.rawQuery(
                String.format("SELECT * FROM events WHERE datetime = '%s %s:00.000' AND title = '%s';", selectedDate, time, title),
                null);

        boolean result = myCursor.moveToNext();
        myCursor.close();
        AgendaDB.close();

        return result;
    }
    private boolean isTimeValid() {
        EditText timeEdit = findViewById(R.id.timeInput);
        String time = timeEdit.getText().toString();

        if (time.length() != 5) return false;

        String hours = time.substring(0,2);
        if ((hours.charAt(0) != '0') && (Integer.parseInt(hours) > 23)) {
                return false;
        }

        String minutes = time.substring(3);
        if (minutes.charAt(0) != '0') {
            return Integer.parseInt(minutes) <= 59;
        }
        return true;
    }

    private boolean isTitleValid() {
        EditText titleEdit = findViewById(R.id.eventTitle);
        String title = titleEdit.getText().toString();

        if (title.equals("")) return false;
        for (char c : title.toCharArray()) {
            if (c != ' ') return true;
        }
        return false;
    }

    private void setIntentProperties(Intent intent) {
        intent.putExtra("data", getIntent().getSerializableExtra("data"));
    }

    public void onCancelClick(View view) {
        Intent intent = new Intent(AddEventActivity.this, AgendaActivity.class);
        setIntentProperties(intent);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
    }
}
