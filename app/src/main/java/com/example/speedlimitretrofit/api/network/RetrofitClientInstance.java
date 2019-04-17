package com.example.speedlimitretrofit.api.network;

import com.example.speedlimitretrofit.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

// centralizes retrofit creation for performance enhancing purposes and modularity
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://overpass.kumi.systems/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Uses builder to build retrofit instance if it doesn't already exist

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            if (BuildConfig.DEBUG) {
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            }

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging);

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        // builds retrofit field variable
        getRetrofitInstance();
        // creates a service based on the type generic serviceClass
        return retrofit.create(serviceClass);
    }
}
