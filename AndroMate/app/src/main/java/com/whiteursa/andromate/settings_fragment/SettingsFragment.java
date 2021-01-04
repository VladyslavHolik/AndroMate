package com.whiteursa.andromate.settings_fragment;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.whiteursa.andromate.R;
import com.whiteursa.andromate.weather.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SettingsFragment extends Fragment {
    private MainActivity activity;

    private String[] languages = {"af", "ar", "bg", "bn", "ca","cn", "cs","cy", "da", "de", "el", "en",
            "es", "et", "fa", "fi", "fr", "gu", "he", "hi", "hr", "hu", "id", "it", "ja", "kn",
            "ko", "lt", "lv", "mk", "ml", "mr", "ne", "nl", "no", "pa", "pl", "pt", "ro", "ru",
            "sk", "sl", "so", "sq", "sv", "sw", "ta", "te", "th", "tl", "tr","tw", "uk", "ur","vi"};

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button save = rootView.findViewById(R.id.saveButton);

        View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
                SharedPreferences.Editor myEditor = myPreferences.edit();
                RadioButton male = rootView.findViewById(R.id.radioMan);

                if (male.isChecked()) {
                    myEditor.putString("GENDER", "Male");
                } else {
                    myEditor.putString("GENDER", "Female");
                }

                Spinner spinner = rootView.findViewById(R.id.languagesSpinner);
                myEditor.putInt("languagePosition", spinner.getSelectedItemPosition());
                myEditor.putString("language", languages[spinner.getSelectedItemPosition()]);

                TextView textView = rootView.findViewById(R.id.searchText);
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
                activity.closeFragment();
            }
        };

        save.setOnClickListener(saveListener);

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.closeFragment();
            }
        };

        Button cancel = rootView.findViewById(R.id.cancelSettingsButton);
        cancel.setOnClickListener(cancelListener);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        String gender = myPreferences.getString("GENDER", "Unknown");
        if (!gender.equals("Unknown") ) {
            if (gender.equals("Male") ) {
                RadioButton male = rootView.findViewById(R.id.radioMan);
                male.setChecked(true);
            } else {
                RadioButton female = rootView.findViewById(R.id.radioWoman);
                female.setChecked(true);
            }
        }

        Spinner languagesSpinner = rootView.findViewById(R.id.languagesSpinner);

        Drawable spinnerDrawable = Objects.requireNonNull(languagesSpinner.getBackground().getConstantState()).newDrawable();
        spinnerDrawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        languagesSpinner.setBackground(spinnerDrawable);

        ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.spinner_item, languages);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        languagesSpinner.setAdapter(adapter);

        int languagePosition = myPreferences.getInt("languagePosition", -1);
        if (languagePosition != -1) {
            languagesSpinner.setSelection(languagePosition);
        }

        String searchText = myPreferences.getString("searchText", "");
        if (!searchText.equals("")) {
            TextView textView = rootView.findViewById(R.id.searchText);
            try {
                textView.setText(URLDecoder.decode(searchText, StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
