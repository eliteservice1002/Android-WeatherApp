package com.basicphones.basicweather.weatherHelper;

import com.basicphones.basicweather.model.Forecast;

public interface CurrentWeatherCallback {
    void onSuccess(Forecast currentWeather);
    void onFailure(Throwable throwable);
}
