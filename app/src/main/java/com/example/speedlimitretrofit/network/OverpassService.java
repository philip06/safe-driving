package com.example.speedlimitretrofit.network;

import com.example.speedlimitretrofit.model.overpassmodel.OverpassModel;
import com.example.speedlimitretrofit.model.querymodel.QueryModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OverpassService {

    @POST("/api/interpreter")
    Call<OverpassModel> getSpeedData(@Body QueryModel query);
}
