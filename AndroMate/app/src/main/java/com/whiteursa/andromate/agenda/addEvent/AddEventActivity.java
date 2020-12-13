package com.whiteursa.andromate.agenda.addEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.whiteursa.andromate.R;

public class AddEventActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void onAddEventClick(View view) {
        EditText timeEdit = findViewById(R.id.timeInput);
        EditText titleEdit = findViewById(R.id.eventTitle);
        EditText descriptionEdit = findViewById(R.id.eventDescription);

        String time = timeEdit.getText().toString();
        if (!isTimeValid()) {
            timeEdit.setError("hh:mm");
            return;
        }
        if (!isTitleValid()) {
            titleEdit.setError("Must not be empty");
            return;
        }


    }

    private boolean isTimeValid() {
        EditText timeEdit = findViewById(R.id.timeInput);
        String time = timeEdit.getText().toString();

        if (time.length() != 5) return false;

        String hours = time.substring(0,2);
        if (hours.charAt(0) != '0') {
            if (Integer.parseInt(hours) > 23) {
                return false;
            }
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
}
