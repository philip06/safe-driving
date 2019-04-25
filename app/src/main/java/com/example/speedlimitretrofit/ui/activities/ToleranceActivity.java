package com.example.speedlimitretrofit.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedlimitretrofit.R;
import com.example.speedlimitretrofit.helpers.GPSTracker;

public class ToleranceActivity extends AppCompatActivity {

    private SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        double tolerance = Double.parseDouble(spref.getString("Speed_Tolerance", "10"));

        if(tolerance > 20.0 || tolerance < 0.0 )
        {
            //Notification for invalid tol, tol set to default of 10
            Context context = getApplicationContext();
            CharSequence text = "Invalid tolerance entered. Tolerance should be between 1 and 20 mph";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        else
        {
            spref.edit().putString("Speed_Tolerance", String.valueOf(tolerance)).apply();
        }

    }


}
