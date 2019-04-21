package com.example.speedlimitretrofit.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.speedlimitretrofit.R;
import com.example.speedlimitretrofit.api.model.overpassmodel.*;
import com.example.speedlimitretrofit.api.model.querymodel.*;
import com.example.speedlimitretrofit.api.network.OverpassService;
import com.example.speedlimitretrofit.api.network.RetrofitClientInstance;
import com.example.speedlimitretrofit.helpers.ResponseParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverpassForegroundService extends Service {
    private static final String LOG_TAG = "OverpassForegroundService";
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // the addAction re-use the same intent to keep the example short
        String action = intent.getAction();

        final String radius = "1500";
        final String lat = "38.970030";
        final String lon = "-77.402170";
        final int interval = 5000; // milliseconds

        if (action.equals("start")) {
            handler = new Handler();
            // initialize a task scheduler
            runnable = new Runnable() {
                @Override
                public void run() {
                    //do something
                    runOverpassCall(radius, lat, lon);
                    handler.postDelayed(this, interval);
                }
            };
            handler.postDelayed(runnable, interval);
        } else if (action.equals("stop")) {
                // cancels scheduled task then terminates foreground service
                handler.removeMessages(0);
                stopForeground(true);
                stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.speedlimitretrofit";
        String channelName = "Overpass Foreground Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Safe Driving")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public void runOverpassCall(String radius, String lat, String lon) {
        String queryType = "way";
        String recurseType = "down";
        String k = "maxspeed";

        // Generate QueryModel by first creating its parameter objects
        HasKV hasKV = new HasKV(k);

        Around around = new Around(radius, lat, lon);

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

                    double userLat = 72.8353241;
                    double userLon = 7.2384237;
                    // where: 'M' is statute miles (default):
                    //        'K' is kilometers
                    //        'N' is nautical miles
                    String measurementUnit = "M";

                    // initialize custom parser to simplify getting required data
                    ResponseParser responseParser = new ResponseParser(overpassModel);

                    // closestNode = ArrayList<nodeId, distance, speedValue>
                    ArrayList<String> closestNode = responseParser.getClosestNode(userLat, userLon, measurementUnit);
                    Intent intent = new Intent();
                    intent.putExtra("maxSpeed", closestNode.get(2));

                    // update maxSpeed textview with a broadcast
                    Toast.makeText(getApplicationContext(), "sending broadcast with: " + closestNode.get(2), Toast.LENGTH_SHORT).show();
                    sendBroadcast(intent);
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
