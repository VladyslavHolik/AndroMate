package com.whiteursa.andromate.agenda;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.whiteursa.andromate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class AsyncTaskForAgenda extends AsyncTask<Integer, Void, ArrayList<ArrayList<String>>> {
    @SuppressLint("StaticFieldLeak")
    private AgendaActivity activity;
    private String nowString;

    AsyncTaskForAgenda(AgendaActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Integer... params) {
        SQLiteDatabase AgendaDB = activity.openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);
        AgendaDB.execSQL("CREATE TABLE IF NOT EXISTS events (datetime text, title VARCHAR(60), description VARCHAR(1000))");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date dateNow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNow);
        cal.add(Calendar.DATE, params[0]);
        nowString = formatter.format(cal.getTime());

        Cursor myCursor = AgendaDB.rawQuery(
                String.format("SELECT * FROM events WHERE datetime BETWEEN DATETIME('%s 00:00:00.000') AND DATETIME('%s 23:59:59.000') ORDER BY DATETIME(datetime);", nowString, nowString),
               null);

        ArrayList<ArrayList<String>> arrayOfEventsData = new ArrayList<>();

        while (myCursor.moveToNext()) {
            ArrayList<String> eventData = new ArrayList<>();

            eventData.add(myCursor.getString(0));
            eventData.add(myCursor.getString(1));
            eventData.add(myCursor.getString(2));

            arrayOfEventsData.add(eventData);
        }
        myCursor.close();
        AgendaDB.close();

        return arrayOfEventsData;
    }

    @Override
    protected void onPostExecute(final ArrayList<ArrayList<String>> arrayOfEventsData) {
        super.onPostExecute(arrayOfEventsData);

        TextView date = activity.findViewById(R.id.datetime);
        date.setText(nowString);

        final ArrayList<String> events = new ArrayList<>();

        for (ArrayList<String> eventData : arrayOfEventsData) {
            String event = eventData.get(0).substring(11,16);
            event += String.format("    %s", eventData.get(1));

            events.add(event);
        }

        ListView listOfEvents = activity.findViewById(R.id.events);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, events) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                return view;
            }

        };

        listOfEvents.setAdapter(adapter);
        activity.setArrayOfEventsData(arrayOfEventsData);

        TextView text = activity.findViewById(R.id.noEventsText);

        if (events.size() == 0) {
            text.setText(String.format("%s %s",activity.getString(R.string.noEvents), nowString));
        } else {
            text.setText("");
        }
    }
}
