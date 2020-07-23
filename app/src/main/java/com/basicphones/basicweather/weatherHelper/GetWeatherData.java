package com.basicphones.basicweather.weatherHelper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.basicphones.basicweather.MainActivity;
import com.basicphones.basicweather.database.ForecastViewModel;
import com.basicphones.basicweather.database.InsertForecastActivity;
import com.basicphones.basicweather.model.Forecast;
import com.google.gson.Gson;

import java.util.Objects;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class GetWeatherData extends BroadcastReceiver {

    private static final String TAG = "GetWeatherData";

    @Override
    public void onReceive(final Context context, Intent intent) {
        double lat = intent.getDoubleExtra("LATITUDE", 0.0);
        double lon = intent.getDoubleExtra("LONGITUDE", 0.0);
        // get lat and lon of current location
        Log.d(TAG, "Lat: " + lat + " lon: " + lon);

        MainActivity.weatherApiHelper.getCurrentWeatherByGeoCoordinates(lat, lon, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(Forecast currentWeather) {
                Log.d(TAG, "Successful get weather details.");

                Intent insertIntent = new Intent(context, InsertForecastActivity.class);
                Gson gson = new Gson();
                String currentWeatherString = gson.toJson(currentWeather);
                insertIntent.putExtra("forecast", currentWeatherString);
                insertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(insertIntent);
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Log.v(TAG, Objects.requireNonNull(throwable.getMessage()));
                } else {
                    Log.v(TAG, throwable.getMessage());
                }
            }
        });

    }
}
