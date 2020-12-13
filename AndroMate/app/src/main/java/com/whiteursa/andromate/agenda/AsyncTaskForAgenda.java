package com.whiteursa.andromate.agenda;

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
    private AgendaActivity activity;

    AsyncTaskForAgenda(AgendaActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Integer... params) {
        SQLiteDatabase AgendaDB = activity.openOrCreateDatabase("AgendaDB.db", Context.MODE_PRIVATE, null);
        AgendaDB.execSQL("CREATE TABLE IF NOT EXISTS events (datetime text, title VARCHAR(30), description VARCHAR(200))");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date dateNow = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNow);
        cal.add(Calendar.DATE, params[0]);
        String nowString = formatter.format(cal.getTime());

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
    protected void onPostExecute(ArrayList<ArrayList<String>> arrayOfEventsData) {
        super.onPostExecute(arrayOfEventsData);

        ArrayList<String> events = new ArrayList<>();

        for (ArrayList<String> eventData : arrayOfEventsData) {
            String event = eventData.get(0).substring(11,16);
            event += String.format("    %s", eventData.get(1));

            events.add(event);
        }

        if (events.size() != 0) {
            ListView listOfEvents = activity.findViewById(R.id.events);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, events) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.WHITE);

                    return view;
                }

            };

            listOfEvents.setAdapter(adapter);
        } else {
            TextView text = activity.findViewById(R.id.noEventsText);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date dateNow = new Date();
            String nowString = formatter.format(dateNow);
            text.setText(String.format("%s %s",activity.getString(R.string.noEvents), nowString));
        }
    }
}
