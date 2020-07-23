package com.basicphones.basicweather;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.basicphones.basicweather.database.ForecastViewModel;
import com.basicphones.basicweather.model.Forecast;
import com.basicphones.basicweather.weatherHelper.CurrentWeatherCallback;
import com.basicphones.basicweather.weatherHelper.GetWeatherData;
import com.basicphones.basicweather.weatherHelper.WeatherApiHelper;
import com.basicphones.basicweather.ui.main.DailyWeatherFragment;
import com.basicphones.basicweather.ui.main.HourlyWeatherFragment;
import com.basicphones.basicweather.ui.main.SectionsPagerAdapter;
import com.basicphones.basicweather.ui.main.WeatherFragment;
import com.basicphones.basicweather.ui.main.WeatherMapFragment;
import com.google.android.material.tabs.TabLayout;
import com.mapbox.mapboxsdk.Mapbox;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private final int LOCATION_REQUEST = 1;
    private static final String TAG = "MainActivity";
    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private static final int SERVICE_REQUEST_CODE = 12;

    public static WeatherApiHelper weatherApiHelper;

    private ForecastViewModel mForecastViewModel;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    public TabLayout tabs;
    public SectionsPagerAdapter sectionsPagerAdapter;
    public double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init shared pref
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE_NAME,0);

        // check permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        }

        // instatiate OpenWeatherMapApi class with API key
        weatherApiHelper = new WeatherApiHelper(getString(R.string.api_key));
        // init mapbox sdk
        Mapbox.getInstance(this, getString(R.string.MAPBOX_SDK_TOKEN));

        // check network connection
        if (!Utility.isConnected(getBaseContext())) {
            if (mForecastViewModel != null) {
                if (mForecastViewModel.getAll().getValue().size() == 0) {
                    Log.e(TAG, getString(R.string.no_cache_data));
                    Toast.makeText(this, getString(R.string.no_cache_data), Toast.LENGTH_LONG).show();
                    finish();
                }
                Log.e(TAG, getString(R.string.no_cache_data));
                Toast.makeText(this, getString(R.string.no_cache_data), Toast.LENGTH_LONG).show();
                finish();
            }
            Toast.makeText(this, getString(R.string.no_connection_message), Toast.LENGTH_LONG).show();
        }

        if(!PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d(TAG, "Access Fine Location permission is not granted");
            Toast.makeText(this, getString(R.string.location_permission_not_granted), Toast.LENGTH_LONG).show();
            // close app, location permission is denied
            finish();
        } else {
            // find location
            GPSLocation gpsLocation = new GPSLocation(this);

            // async task to get location changes
            Double[] latlon = new Double[0];
            try {
                latlon = gpsLocation.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            // setup tabs
            sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            mViewPager = findViewById(R.id.view_pager);


            if (latlon.length != 0) {
                SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                sharedPrefEditor.putLong("LATITUDE", Double.doubleToRawLongBits(latlon[0]));
                sharedPrefEditor.putLong("LONGITUDE", Double.doubleToRawLongBits(latlon[1]));
                latitude = latlon[0];
                longitude = latlon[1];
                sharedPrefEditor.apply();
            } else {
                Log.i(TAG, "Using last known location");
                Toast.makeText(this, getString(R.string.last_known_location), Toast.LENGTH_LONG).show();
                latitude = Double.longBitsToDouble(sharedPreferences.getLong("LATITUDE", Double.doubleToLongBits(0.0)));
                longitude = Double.longBitsToDouble(sharedPreferences.getLong("LONGITUDE", Double.doubleToLongBits(0.0)));
            }
            if (latitude == 0.0 || longitude == 0.0) {
                Log.i(TAG, "Location is not accessible");
                Toast.makeText(this, getString(R.string.location_not_accessible), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Intent intent = new Intent(this, GetWeatherData.class);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);

                mPendingIntent = PendingIntent.getBroadcast(this, SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                // set to be executed every half hour
                if (mAlarmManager != null) {
                    mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_HOUR, mPendingIntent);
                }

                // Get a new or existing ViewModel from the ViewModelProvider.
                mForecastViewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
                getCurrentLocationWeather(latitude, longitude, mForecastViewModel);

                // add fragments
                WeatherFragment weatherFragment = WeatherFragment.newInstance(latitude, longitude);
                sectionsPagerAdapter.addFragment(weatherFragment);
                HourlyWeatherFragment hourlyWeatherDayFragment = HourlyWeatherFragment.newInstance();
                sectionsPagerAdapter.addFragment(hourlyWeatherDayFragment);
                DailyWeatherFragment dailyWeatherDayFragment = DailyWeatherFragment.newInstance();
                sectionsPagerAdapter.addFragment(dailyWeatherDayFragment);
                WeatherMapFragment weatherMapFragment = WeatherMapFragment.newInstance(latitude, longitude);
                sectionsPagerAdapter.addFragment(weatherMapFragment);
            }

            mViewPager.setAdapter(sectionsPagerAdapter);
            tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(mViewPager);
            tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition() == 3){
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        Fragment a = sectionsPagerAdapter.getItem(3);
                        WeatherMapFragment weatherMapFragment = WeatherMapFragment.newInstance(latitude, longitude);
//                        ft.detach(a).attach(a).commit();
                        ft.replace(mViewPager.getId(), weatherMapFragment);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    /**
     * Change tabs with keyboard
     * @param keyCode numbers 1,2,3,4
     * @param keyEvent switch tabs
     * @return key action
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                Log.d(TAG, "Key code 1");
                mViewPager.setCurrentItem(0);
                return true;
            case KeyEvent.KEYCODE_2:
                Log.d(TAG, "Key code 2");
                mViewPager.setCurrentItem(1);
                return true;
            case KeyEvent.KEYCODE_3:
                Log.d(TAG, "Key code 3");
                mViewPager.setCurrentItem(2);
                return true;
            case KeyEvent.KEYCODE_4:
                Log.d(TAG, "Key code 4");
                mViewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    private void getCurrentLocationWeather(Double lat, Double lon, final ForecastViewModel forecastViewModel) {

        // get lat and lon of current location
        Log.d(TAG, "Lat: " + lat + " lon: " + lon);

        MainActivity.weatherApiHelper.getCurrentWeatherByGeoCoordinates(lat, lon, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(Forecast currentWeather) {
                Log.d(TAG, "Successful get weather details.");
                forecastViewModel.insert(currentWeather);
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

    @Override
    protected void onDestroy() {

        Log.d(TAG, "on Destroy");

        // stop pending intent
        if (mPendingIntent != null) {
            mPendingIntent.cancel();
            if (mAlarmManager != null) {
                mAlarmManager.cancel(mPendingIntent);
            }
        }

        if (mForecastViewModel != null) {
            if (mForecastViewModel.getAll().getValue() != null) {
                for (int i = 0; i < mForecastViewModel.getAll().getValue().size() - 1; i++) {
                    Log.d(TAG, "Deleting: " + mForecastViewModel.getAll().getValue().get(i));
                    mForecastViewModel.delete(mForecastViewModel.getAll().getValue().get(i));
                }
            }
        }

        super.onDestroy();
    }
}
