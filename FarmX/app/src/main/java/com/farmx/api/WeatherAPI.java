package com.farmx.api;

import com.farmx.model.WeatherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.farmx.util.Constants.API_KEY;

public interface WeatherAPI {
    @GET("data/2.5/weather?&units=metric&appid=" + API_KEY + "&lang=tr")
    Call<WeatherApi> makeCall(
            @Query("lat")
                    int latitude,
            @Query("lon")
                    int longitude
    );
}
