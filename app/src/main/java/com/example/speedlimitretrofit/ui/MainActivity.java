package com.example.speedlimitretrofit.ui;

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

        String east = "7.157";
        String north = "50.748";
        String south = "50.746";
        String west = "7.154";

        String queryType = "way";
        String recurseType = "down";
        String k = "maxspeed";

        // Generate QueryModel by first creating its parameter objects
        HasKV hasKV = new HasKV(k);

        BBoxQuery bBoxQuery = new BBoxQuery(east, north, south, west);

        Query query = new Query(queryType, hasKV, bBoxQuery);

        Recurse recurse = new Recurse(recurseType);

        Union union = new Union("", recurse);

        // Use other objects to create QueryModel object which is passed to retrofit
        QueryModel overpassQuery = new QueryModel(query, union, "");

        // TextView which we write the data to
        maxSpeedTextView = findViewById(R.id.maxSpeed);

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
                OverpassModel overpassModel = response.body();

                double userLat = 72.8353241;
                double userLon = 7.2384237;
                String measurementUnit = "M";

                // initialize custom parser to simplify getting required data
                ResponseParser responseParser = new ResponseParser(overpassModel);

                ArrayList<String> closestNodes = responseParser.getClosestNode(userLat, userLon, measurementUnit);

                maxSpeedTextView.setText(closestNodes.get(2));
            }

            // TODO: implement proper error handling
            // TODO: write test cases for error handling
            // run on failure. does error handling
            @Override
            public void onFailure(Call<OverpassModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });

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
        Intent foreground = new Intent(MainActivity.this, ForegroundTest.class);

        if(start) {
            foreground.setAction("test");
            startService(foreground);
        }
        else{
            foreground.setAction("test");
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
}
