package com.example.speedlimitretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speedlimitretrofit.model.OverpassModel;
import com.example.speedlimitretrofit.network.OverpassService;
import com.example.speedlimitretrofit.network.RetrofitClientInstance;

import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private TextView maxSpeedTextView;

    // TODO: create model for query
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maxSpeedTextView = findViewById(R.id.max_speed);

        // implements OverpassService using retrofit client
        OverpassService overpassService = RetrofitClientInstance.createService(OverpassService.class);

        // runs speed limit data api request
        // returns OverpassModel to call object
        Call<OverpassModel> call = overpassService.getSpeedData(testQuery);

        // runs call
        call.enqueue(new Callback<OverpassModel>() {

            // TODO: create a parser for response object
            // run when api request is successful
            // response will be the object used to populate the textview
            @Override
            public void onResponse(Call<OverpassModel> call, Response<OverpassModel> response) {
                String max_speed = "max speed not found";
                OverpassModel overpassModel = response.body();

                List<OverpassModel.Tag> tagList = overpassModel.getWayList().get(0).getTagList();
                ListIterator<OverpassModel.Tag> listIterator = tagList.listIterator();

                while (listIterator.hasNext()) {
                    OverpassModel.Tag tag = listIterator.next();
                    if (tag.getKey() == "maxspeed") {
                        max_speed = tag.getValue();
                    }
                }

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
