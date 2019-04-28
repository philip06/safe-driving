package com.example.speedlimitretrofit.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

import com.example.speedlimitretrofit.ui.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {



    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity

        getSupportFragmentManager().beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        /*boolean test1 = settings.getBoolean("Audio_Preference", false);
        boolean test2 = settings.getBoolean("Vibration_Preference", false);
        String test3 = settings.getString("Speed_Tolerance", "10");
        */

    }

    //set new tolerance based on user input
    /*public void metric_switch(View metric)
    {
        ToggleButton metricTog = findViewById(R.id.metricToggle);

        Boolean kmh = metricTog.isChecked();

        if(kmh)
        {
            //convert speed to kilometers
        }
        else
        {
            //convert speed to miles
        }
    }*/
}

