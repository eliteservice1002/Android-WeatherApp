package com.basicphones.basicweather.weatherHelper;

import com.basicphones.basicweather.model.Forecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherApiHelper {

    private static final String UNITS = "units";
    private static final String IMPERIAL = "us";

    private WeatherService weatherService;
    private Map<String, String> options;
    private String mApiKey;

    public WeatherApiHelper(String apiKey){
        mApiKey = apiKey;
        weatherService = WeatherApiClient.getClient().create(WeatherService.class);
        options = new HashMap<>();
        options.put(UNITS, IMPERIAL);
    }

    private Throwable NoAppIdErrMessage() {
        return new Throwable("UnAuthorized. Please set a valid OpenWeatherMap API KEY.");
    }


    private Throwable NotFoundErrMsg(String str) {
        Throwable throwable = null;
        try {
            JSONObject obj = new JSONObject(str);
            throwable = new Throwable(obj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (throwable == null){
            throwable = new Throwable("An unexpected error occurred.");
        }


        return throwable;
    }

    private void handleCurrentWeatherResponse(Response<Forecast> response, CurrentWeatherCallback callback){
        if (response.code() == HttpURLConnection.HTTP_OK){
            callback.onSuccess(response.body());
        }
        else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
            callback.onFailure(NoAppIdErrMessage());
        }
        else{
            try {
                callback.onFailure(NotFoundErrMsg(response.errorBody().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get current weather by geo coordinate
     * @param latitude of current location
     * @param longitude of current location
     * @param callback result json class
     */
    public void getCurrentWeatherByGeoCoordinates(double latitude, double longitude, final CurrentWeatherCallback callback){
        weatherService.getForecast(mApiKey, latitude, longitude, options).enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(@NonNull Call<Forecast> call, @NonNull Response<Forecast> response) {
                handleCurrentWeatherResponse(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
