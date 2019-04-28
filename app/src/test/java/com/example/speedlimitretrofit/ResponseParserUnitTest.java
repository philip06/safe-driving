package com.example.speedlimitretrofit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.speedlimitretrofit.api.model.overpassmodel.OverpassModel;
import com.example.speedlimitretrofit.api.model.querymodel.Around;
import com.example.speedlimitretrofit.api.model.querymodel.HasKV;
import com.example.speedlimitretrofit.api.model.querymodel.Query;
import com.example.speedlimitretrofit.api.model.querymodel.QueryModel;
import com.example.speedlimitretrofit.api.model.querymodel.Recurse;
import com.example.speedlimitretrofit.api.model.querymodel.Union;
import com.example.speedlimitretrofit.api.network.OverpassService;
import com.example.speedlimitretrofit.api.network.RetrofitClientInstance;
import com.example.speedlimitretrofit.helpers.ResponseParser;
import com.example.speedlimitretrofit.ui.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

import static com.google.common.truth.Truth.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ResponseParserUnitTest {

    @Test
    public void onFetchNoData() throws IOException {
        String radius = "1000";
        // coordinates with no data
        double userLat = 10.0;
        double userLon = 10.0;
        Call<OverpassModel> call = getOverpassCall(radius, userLat, userLon);

        Response<OverpassModel> response = call.execute();
        assertThat(response.isSuccessful()).isTrue();

        if (response.isSuccessful()) {
            OverpassModel overpassModel = response.body();

            // where: 'M' is statute miles (default):
            //        'K' is kilometers
            //        'N' is nautical miles
            String measurementUnit = "M";

            // initialize custom parser to simplify getting required data
            ResponseParser responseParser = new ResponseParser(overpassModel);

            // test for empty fields
            assertThat(responseParser.getNodeCoordsHash()).isEmpty();
            assertThat(responseParser.getWayIdSpeedHash()).isEmpty();
            assertThat(responseParser.getWayNdIdHash()).isEmpty();
            assertThat(responseParser.getClosestNode(0.0, 0.0, measurementUnit)).isEmpty();
        }
    }

    @Test
    public void onFetchSpeedData() throws IOException {
        String radius = "1000";
        // coordinates with speed data
        double userLat = 38.970030;
        double userLon = -77.402170;
        Call<OverpassModel> call = getOverpassCall(radius, userLat, userLon);

        Response<OverpassModel> response = call.execute();
        assertThat(response.isSuccessful()).isTrue();

        if (response.isSuccessful()) {
            OverpassModel overpassModel = response.body();

            // where: 'M' is statute miles (default):
            //        'K' is kilometers
            //        'N' is nautical miles
            String measurementUnit = "M";

            // initialize custom parser to simplify getting required data
            ResponseParser responseParser = new ResponseParser(overpassModel);

            // test for empty fields
            assertThat(responseParser.getWayIdSpeedHash()).isNotEmpty();
            assertThat(responseParser.getClosestNode(0.0, 0.0, measurementUnit)).isNotEmpty();
        }
    }

    public Call<OverpassModel> getOverpassCall(String radius, double userLat, double userLon) {
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

        return call;
    }
}