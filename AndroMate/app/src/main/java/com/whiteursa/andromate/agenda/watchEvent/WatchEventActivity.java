package com.whiteursa.andromate.agenda.watchEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.whiteursa.andromate.R;
import com.whiteursa.andromate.agenda.AgendaActivity;

import java.util.Objects;

public class WatchEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_event);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextView title = findViewById(R.id.watchEventTitle);
        TextView datetime = findViewById(R.id.watchEventDateTime);
        TextView description = findViewById(R.id.watchEventDescription);

        title.setText(String.format("%s", getIntent().getStringExtra("eventTitle")));
        datetime.setText(String.format("%s",
                Objects.requireNonNull(getIntent().getStringExtra("eventDatetime")).substring(0, 16)));
        description.setText(getIntent().getStringExtra("eventDescription"));
    }

    public void onOkClick(View view) {
        goToAgenda();
    }

    public void onDeleteClick(View view) {
        String datetime = getIntent().getStringExtra("eventDatetime");
        String title = getIntent().getStringExtra("eventTitle");

        SQLiteDatabase AgendaDB = openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);

        AgendaDB.delete("events", "datetime=? and title=?", new String[]{datetime, title});
        AgendaDB.close();

        goToAgenda();
    }

    private void goToAgenda() {
        Intent intent = new Intent(WatchEventActivity.this,
                AgendaActivity.class);
        setIntentProperties(intent);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
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
