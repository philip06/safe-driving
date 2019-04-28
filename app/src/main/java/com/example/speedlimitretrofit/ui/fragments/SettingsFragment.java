package com.example.speedlimitretrofit.ui.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.speedlimitretrofit.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment(){

    }
    @Override public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.settings_preferences,rootKey);
    }
}
