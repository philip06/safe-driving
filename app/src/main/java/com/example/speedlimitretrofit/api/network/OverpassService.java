package com.example.speedlimitretrofit.api.network;

import com.example.speedlimitretrofit.api.model.overpassmodel.OverpassModel;
import com.example.speedlimitretrofit.api.model.querymodel.QueryModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OverpassService {

    @POST("/api/interpreter")
    Call<OverpassModel> getSpeedData(@Body QueryModel query);
}
