package com.whiteursa.andromate.agenda;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.whiteursa.andromate.MainActivity;
import com.whiteursa.andromate.OnSwipeTouchListener;
import com.whiteursa.andromate.R;
import com.whiteursa.andromate.agenda.addEvent.AddEventActivity;
import com.whiteursa.andromate.agenda.watchEvent.WatchEventActivity;

import java.util.ArrayList;
import java.util.Objects;

public class AgendaActivity extends AppCompatActivity {

    View.OnClickListener myListener;
    private ArrayList<ArrayList<String>> arrayOfEventsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView events = findViewById(R.id.events);

                int index = events.indexOfChild(v);

                Intent intent = new Intent(
                        AgendaActivity.this,
                        WatchEventActivity.class);
                setIntentProperties(intent);

                intent.putExtra("eventTitle", arrayOfEventsData.get(index).get(0));
                intent.putExtra("eventDatetime", arrayOfEventsData.get(index).get(1));
                intent.putExtra("eventDescription", arrayOfEventsData.get(index).get(2));

                startActivity(intent);
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
            }
        };

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        AsyncTaskForAgenda task = new AsyncTaskForAgenda(this);
        task.execute(0);

        findViewById(R.id.events).setOnTouchListener(new OnSwipeTouchListener(findViewById(R.id.events).getContext()) {
            public void onSwipeLeft() {
                Intent intent = new Intent(AgendaActivity.this,
                        MainActivity.class);
                setIntentProperties(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
            }
        });
    }

     void setIntentProperties(Intent intent) {
        intent.putExtra("city", getIntent().getStringExtra("city"));
        intent.putExtra("details", getIntent().getStringExtra("details"));
        intent.putExtra("currentTemperature", getIntent().getStringExtra("currentTemperature"));
        intent.putExtra("humidity", getIntent().getStringExtra("humidity"));
        intent.putExtra("pressure", getIntent().getStringExtra("pressure"));
        intent.putExtra("lastUpdated", getIntent().getStringExtra("lastUpdated"));
        intent.putExtra("weatherIcon",getIntent().getStringExtra("weatherIcon"));
    }

    public void onNewEventClick(View view) {
        Intent intent = new Intent(AgendaActivity.this,
                AddEventActivity.class);
        setIntentProperties(intent);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
    }

    public void setArrayOfEventsData(ArrayList<ArrayList<String>> arrayOfEventsData) {
        this.arrayOfEventsData = arrayOfEventsData;
    }
}
