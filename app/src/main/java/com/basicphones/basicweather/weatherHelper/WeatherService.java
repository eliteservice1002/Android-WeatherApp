package com.basicphones.basicweather.weatherHelper;

import com.basicphones.basicweather.model.Forecast;

import java.util.Map;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

interface WeatherService {
    String FORECAST = "/forecast/";

    @GET(FORECAST + "{apiKey}/{lat},{long}")
    Call<Forecast> getForecast(@Path("apiKey") String apiKey,
                               @Path("lat") double latitude,
                               @Path("long") double longitude,
                               @Nullable @QueryMap Map<String, String> options);

    @GET("/forecast/{apiKey}/{lat},{long},{time}")
    Call<Forecast> getTimeMachineForecast(@Path("apiKey") String apiKey,
                                          @Path("lat") double latitude,
                                          @Path("long") double longitude,
                                          @Path("time") double timestamp,
                                          @Nullable @QueryMap Map<String, String> options);
}
