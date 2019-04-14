package com.example.speedlimitretrofit.network;

import com.example.speedlimitretrofit.model.OverpassModel;
import com.example.speedlimitretrofit.model.QueryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OverpassService {

    @POST("/api/interpreter")
    Call<OverpassModel> getSpeedData(@Body QueryModel query);
}
