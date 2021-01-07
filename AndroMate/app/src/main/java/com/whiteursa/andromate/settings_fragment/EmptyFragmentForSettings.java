package com.whiteursa.andromate.settings_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whiteursa.andromate.R;

public class EmptyFragmentForSettings extends Fragment {

    public static EmptyFragmentForSettings newInstance() {
        return new EmptyFragmentForSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_for_settings, container, false);
    }
}
