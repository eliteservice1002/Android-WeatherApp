package com.basicphones.basicweather.weatherHelper;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {
    private static final String BASE_URL = "https://api.darksky.net";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
