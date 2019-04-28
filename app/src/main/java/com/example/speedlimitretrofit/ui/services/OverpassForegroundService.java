package com.example.speedlimitretrofit.ui.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.speedlimitretrofit.R;
import com.example.speedlimitretrofit.api.model.overpassmodel.*;
import com.example.speedlimitretrofit.api.model.querymodel.*;
import com.example.speedlimitretrofit.api.network.OverpassService;
import com.example.speedlimitretrofit.api.network.RetrofitClientInstance;
import com.example.speedlimitretrofit.helpers.GPSTracker;
import com.example.speedlimitretrofit.helpers.ResponseParser;
import com.example.speedlimitretrofit.ui.activities.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverpassForegroundService extends Service {
    private static final String LOG_TAG = "OverpassForegroundService";

    // made static so the task is schedules can be stopped in another call to OverpassForegroundService
    private static Handler handler;
    private static Runnable runnable;
    private Intent intentField;

//    private static double userLat;
//    private static double userLon;
//    private static double userSpeed;

    private static NotificationCompat.Builder notificationBuilder;
    private static NotificationManager notificationManager;

    private TextToSpeech mTTS;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize foreground in different ways depending on build version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("notifications error", "Language not supported");
                    }
                }
                else {
                    Log.e("notifications error", "Initialization failed");
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // the addAction re-use the same intent to keep the example short
        String action = intent.getAction();

        this.intentField = intent;

        String radius = "1000";
        int interval = 10000; // milliseconds

        // if MainActivity sends a start trip command
        if (action.equals("startTrip")) {
            // initialize a task scheduler
            this.handler = new Handler();
            this.runnable = new Runnable() {
                String radius;

                int interval;

                @Override
                public void run() {
                    handler.postDelayed(this, this.interval - SystemClock.elapsedRealtime()%1000);
                    // run api call
                    runOverpassCall(this.radius);
                }

                public Runnable init(String radius, int interval) {
                    this.radius=radius;
                    this.interval=interval;

                    return(this);
                }
            }.init(radius, interval);

            // start runnable with handler
            this.handler.postDelayed(this.runnable, interval - SystemClock.elapsedRealtime()%1000);
        // if MainActivity sends a stop trip command
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // cancels scheduled task then terminates foreground service
        if (this.handler != null) {
            this.handler.removeCallbacks(runnable);
        }

        this.mTTS.shutdown();

        stopForeground(true);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    // create foreground service in versions Oreo and up
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.speedlimitretrofit";
        String channelName = "Overpass Foreground Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert this.notificationManager != null;
        this.notificationManager.createNotificationChannel(chan);

        this.notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Safe Driving")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public void updateForegroundSpeed(String maxSpeed, String userSpeed, String status) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // get notification settings from user
        boolean audioPreference = settings.getBoolean("Audio_Preference", false);
        boolean vibrationPreference = settings.getBoolean("Vibration_Preference", false);

        // alpha, red, green, blue [0-255]
        int A, R, G, B, color;

        if (status.equals("safe")) {
            A = 1;
            R = 0;
            G = 255;
            B = 0;
            color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
            this.notificationBuilder.setContentText(userSpeed + "/" + maxSpeed);
            this.notificationBuilder.setColor(color);
            this.notificationManager.notify(2, this.notificationBuilder.build());
        } else if (status.equals("warning")) {
            A = 1;
            R = 255;
            G = 0;
            B = 0;
            color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
            this.notificationBuilder.setContentText(userSpeed + "/" + maxSpeed);
            this.notificationBuilder.setColor(color);
            this.notificationManager.notify(2, this.notificationBuilder.build());

            if (vibrationPreference) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(750);
            }

            String textWarning = "Please slow down.";

            if (audioPreference) {
                mTTS.speak(textWarning, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public void runOverpassCall(String radius) {
        Location loc = new GPSTracker(getApplicationContext()).getLocation();
        final double userLat = loc.getLatitude();
        final double userLon = loc.getLongitude();
        final double userSpeed = loc.getSpeed();

        // String lat = "38.970030";
        // String lon = "-77.402170";

        // get speedTolerance from user preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final double speedTolerance = Double.parseDouble(settings.getString("Speed_Tolerance", "10"));

        String queryType = "way";
        String recurseType = "down";
        String k = "maxspeed";

        // Generate QueryModel by first creating its parameter objects
        HasKV hasKV = new HasKV(k);

        Around around = new Around(radius, userLat, userLon);

        Query query = new Query(queryType, hasKV, around);

        Recurse recurse = new Recurse(recurseType);

        Union union = new Union("", recurse);

        // Use other objects to create QueryModel object which is passed to retrofit
        QueryModel overpassQuery = new QueryModel(query, union, "");

        // implements OverpassService using retrofit client
        OverpassService overpassService = RetrofitClientInstance.createService(OverpassService.class);

        // runs speed limit data api request
        // returns OverpassModel to call object
        Call<OverpassModel> call = overpassService.getSpeedData(overpassQuery);

        // runs call
        call.enqueue(new Callback<OverpassModel>() {

            // run when api request is successful
            // response will be the object used to populate the textview
            @Override
            public void onResponse(Call<OverpassModel> call, Response<OverpassModel> response) {
                // protects us from returned errors
                if (response.isSuccessful()) {
                    OverpassModel overpassModel = response.body();

                    // where: 'M' is statute miles (default):
                    //        'K' is kilometers
                    //        'N' is nautical miles
                    String measurementUnit = "M";

                    // initialize custom parser to simplify getting required data
                    ResponseParser responseParser = new ResponseParser(overpassModel);

                    // closestNode = ArrayList<nodeId, distance, speedValue>
                    ArrayList<String> closestNode = responseParser.getClosestNode(userLat, userLon, measurementUnit);
                    String maxSpeed = closestNode.get(2);
                    Intent intent = new Intent();
                    intent.setAction("sendSpeed");
                    intent.putExtra("maxSpeed", maxSpeed);

                    String status = "safe";

                    // if userSpeed is higher than the maxSpeed + speedTolerance then send a speed notification
                    if (userSpeed  > Double.parseDouble(maxSpeed) + speedTolerance) {
                        status = "warning";
                    }

                    // update maxSpeed on foreground service notification bar
                    updateForegroundSpeed(maxSpeed, String.valueOf(userSpeed), status);

                    // update maxSpeed textview with a broadcast
                    Toast.makeText(getApplicationContext(), "sending broadcast with: " + maxSpeed, Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                    maxSpeedTextView.setText(closestNode.get(2));
                    // runs on error being returned from server
                }
                else {
                    try {
                        Toast.makeText(getApplicationContext(), "server returned an error " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // TODO: implement proper error handling
            // TODO: write test cases for error handling
            // run on network failure
            @Override
            public void onFailure(Call<OverpassModel> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(getApplicationContext(), "network failure", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                }
                else {
                    // TODO: Make ErrorHandlingActivity
                    Toast.makeText(getApplicationContext(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // TODO: log to some central bug tracking service
                }
            }
        });
    }
}
