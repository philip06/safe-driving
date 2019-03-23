package com.example.speedlimitretrofit.network;

import com.example.speedlimitretrofit.model.RetroSpeed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("/photos")
    Call<List<RetroSpeed>> getAllPhotos();
}
