package com.example.speedlimitretrofit.network;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

// TODO: implement logging https://futurestud.io/tutorials/retrofit-2-log-requests-and-responses
// centralizes retrofit creation for performance enhancing purposes
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://overpass.kumi.systems/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        getRetrofitInstance();
        return retrofit.create(serviceClass);
    }
}
