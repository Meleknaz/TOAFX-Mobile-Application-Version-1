package com.farmx.model;

import com.farmx.api.WeatherAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static final String BASE_URL = "https://api.openweathermap.org/";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static WeatherAPI instance = retrofit.create(WeatherAPI.class);
}