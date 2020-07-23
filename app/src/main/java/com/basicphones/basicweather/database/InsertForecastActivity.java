package com.basicphones.basicweather.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.basicphones.basicweather.model.Forecast;
import com.google.gson.Gson;

public class InsertForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Forecast currentWeather = gson.fromJson(getIntent().getStringExtra("forecast"), Forecast.class);
        ForecastViewModel forecastViewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        forecastViewModel.insert(currentWeather);
        finish();
    }
}
