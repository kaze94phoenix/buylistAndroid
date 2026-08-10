package com.example.buylist.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    /**
     * Sample
     * private static final String BASE_URL = "http://127.0.0.1:8000";
     */
    private static final String BASE_URL = "http://127.0.0.1:8000";

    /**
     * TO-DO: FIND A WAY TO REFRESH AUTOMATICALLY AFTER A API REQUEST SUCH AS MY LOCATIONS AND MAKE A USER PROFILE
     */
    private static Retrofit retrofit;

    private final static Gson gson = new GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create();


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiInterface() {
        return getRetrofitInstance().create(ApiService.class);
    }
}