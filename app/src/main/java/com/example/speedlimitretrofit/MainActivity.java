package com.example.speedlimitretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedlimitretrofit.controller.ResponseParser;
import com.example.speedlimitretrofit.model.querymodel.*;
import com.example.speedlimitretrofit.model.overpassmodel.*;
import com.example.speedlimitretrofit.network.OverpassService;
import com.example.speedlimitretrofit.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView maxSpeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                String max_speed = "max speed not found";
                OverpassModel overpassModel = response.body();

                // initialize custom parser to simplify getting required data
                ResponseParser responseParser = new ResponseParser(overpassModel);

                // populates variable with HashMap<nodeid, maxspeed> from data in overpassmodel
                HashMap<String, String> nodeIdSpeedHash = responseParser.getNodeIdSpeedHash();
                // populates variable with HashMap<nodeid, ArrayList<lat, lon>> from data in overpassmodel
                HashMap<String, ArrayList<String>> nodeCoordsHash = responseParser.getNodeCoordsHash();

                maxSpeedTextView.setText(max_speed);
            }

            // TODO: implement proper error handling
            // TODO: write test cases for error handling
            // run on failure. does error handling
            @Override
            public void onFailure(Call<OverpassModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
