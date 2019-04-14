package com.example.speedlimitretrofit;

import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class settingslayout extends Activity {



    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity
        setContentView(R.layout.settingslayout);

    }

    //set new tolerance based on user input
    public void tolerance_set(View tolerance) {

        Intent intent = new Intent(this, display_Tolerance.class);
        startActivity(intent);
    }

    public void notifications_set(View notif)
    {
        Intent intent = new Intent (this, notifications.class);
        startActivity(intent);
    }

    public void permissions_get(View perm)
    {
        Intent intent = new Intent (this, permissions.class);
        startActivity(intent);
    }

    public void metric_switch(View metric)
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
    }
}

