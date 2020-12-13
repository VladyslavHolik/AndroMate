package com.whiteursa.andromate.agenda;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.whiteursa.andromate.MainActivity;
import com.whiteursa.andromate.OnSwipeTouchListener;
import com.whiteursa.andromate.R;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        AsyncTaskForAgenda task = new AsyncTaskForAgenda(this);
        task.execute(0);

        findViewById(R.id.events).setOnTouchListener(new OnSwipeTouchListener(findViewById(R.id.events).getContext()) {
            public void onSwipeLeft() {
                Intent intent = new Intent(AgendaActivity.this,
                        MainActivity.class);
                setIntentProperties(intent);
                startActivity(intent);
                overridePendingTransition(R.anim.rigth_to_center, R.anim.center_to_left);
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