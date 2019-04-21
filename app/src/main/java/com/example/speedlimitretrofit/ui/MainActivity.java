package com.example.speedlimitretrofit.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedlimitretrofit.R;
import com.example.speedlimitretrofit.helpers.ResponseParser;
import com.example.speedlimitretrofit.api.model.querymodel.*;
import com.example.speedlimitretrofit.api.model.overpassmodel.*;
import com.example.speedlimitretrofit.api.network.OverpassService;
import com.example.speedlimitretrofit.api.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView maxSpeedTextView;
    private boolean colorToggle = true;
    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String testQuery = "<osm-script>\n" +
                "  <query type=\"way\">\n" +
                "    <has-kv k=\"maxspeed\"/>\n" +
                "    <bbox-query e=\"7.157\" n=\"50.748\" s=\"50.746\" w=\"7.154\"/>\n" +
                "  </query>\n" +
                "  <union>\n" +
                "    <item/>\n" +
                "    <recurse type=\"down\"/>\n" +
                "  </union>\n" +
                "  <print/>\n" +
                "</osm-script>";

        String radius = "1500";
        String lat = "38.970030";
        String lon = "-77.402170";

        //navigate to settings menu
        final Button settingsButton = findViewById(R.id.buttonSet);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent set = new Intent(MainActivity.this, Settings.class);
                startActivity(set);
            }
        });
    }


    //starts or stops the foreground service
    public void toggleTrip(View view)
    {
        Intent foreground = new Intent(MainActivity.this, OverpassForegroundService.class);

        if(start) {
            Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
            foreground.setAction("start");
            startService(foreground);
        }
        else{
            Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_SHORT).show();
            foreground.setAction("stop");
            stopService(foreground);
        }
        start ^= true;
        setColor();

    }
    //switches trip button color from red to green on click
    public void setColor() {
        Button trip = findViewById(R.id.tripButton);

        if(colorToggle) {
                trip.setBackgroundColor(Color.RED);
        }
        else {
            trip.setBackgroundColor(Color.GREEN);
        }
        colorToggle ^= true;
    }

    public void setMaxSpeedTextView(TextView maxSpeedTextView) {
        this.maxSpeedTextView = maxSpeedTextView;
    }

    //
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String maxSpeed = intent.getStringExtra("maxSpeed");
            maxSpeedTextView.setText(maxSpeed);
            Toast.makeText(context,
                    "Triggered by Service!\n"
                            + "Data passed: " + String.valueOf(maxSpeed),
                    Toast.LENGTH_LONG).show();
        }
    };
}
