package com.example.speedlimitretrofit.ui.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedlimitretrofit.R;
import com.example.speedlimitretrofit.helpers.GPSTracker;
import com.example.speedlimitretrofit.ui.services.OverpassForegroundService;

public class MainActivity extends AppCompatActivity {

    // request permissions for location
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    int count=0;
    private TextView maxSpeedTextView;
    private boolean colorToggle = true;
    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView CurrentSpeedTV= (TextView) findViewById(R.id.CurrentSpeed);
        final TextView LonAndLatTV=(TextView) findViewById(R.id.LonLat);
        this.maxSpeedTextView = findViewById(R.id.maxSpeed);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, 255);
            }
        }
        Thread t= new Thread(){
           @Override
           public void run(){
               final GPSTracker gps = new GPSTracker(getApplicationContext());
               while(!isInterrupted()){
                   try {
                       Thread.sleep(200);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               float temp= gps.getLocation().getSpeed();
                               CurrentSpeedTV.setText(String.valueOf((int)gps.getLocation().getSpeed()));
                            //   String LonAndLat = "Longitude: "+ String.format("%.4",gps.getLocation().getLongitude())+"\nLatitude: "+ String.format("%.4",gps.getLocation().getLatitude());
                               LonAndLatTV.setText("Longitude: "+ String.format("%.4f",gps.getLocation().getLongitude())+"\nLatitude: "+ String.format("%.4f",gps.getLocation().getLatitude()));
                               //String lon= "Longitude: "+ String.format("%.4",gps.getLocation().getLongitude());

                               //LonAndLatTV.setText("Longitude: "+ gps.getLocation().getLongitude()+ "\nLatitude: "+ gps.getLocation().getLatitude());

                           }
                       });
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
        };
        t.start();
        //navigate to settings menu
        final Button settingsButton = findViewById(R.id.buttonSet);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent set = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(set);
            }
        });

        // initialize broadcast receiver to update maxSpeed textview
        LocalBroadcastManager.getInstance(this).registerReceiver(speedReceiver,
                new IntentFilter("sendSpeed"));
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver speedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String maxSpeed = intent.getStringExtra("maxSpeed");
            maxSpeedTextView.setText(maxSpeed);
        }
    };

    //starts or stops the foreground service
    public void toggleTrip(View view)
    {
        Intent foreground = new Intent(MainActivity.this, OverpassForegroundService.class);

        if(start) {
            Toast.makeText(MainActivity.this, "startTrip", Toast.LENGTH_SHORT).show();
            foreground.setAction("startTrip");
            startService(foreground);
        }
        else{
            Toast.makeText(MainActivity.this, "stopTrip", Toast.LENGTH_SHORT).show();
//            foreground.setAction("stop");
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

    protected TextView getTextViewMaxSpeed()
    {
        return findViewById(R.id.maxSpeed);
    }

    protected TextView getTextViewCurrentSpeed()
    {
        return findViewById(R.id.CurrentSpeed);
    }

    public void setMaxSpeedTextView(TextView maxSpeedTextView) {
        this.maxSpeedTextView = maxSpeedTextView;
    }

    @Override
    protected void onDestroy() {
        // make sure to unregister your receiver after finishing of this activity
        LocalBroadcastManager.getInstance(this).unregisterReceiver(speedReceiver);

        super.onDestroy();
    }
}
