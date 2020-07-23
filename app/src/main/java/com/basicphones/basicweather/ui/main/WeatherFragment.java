package com.basicphones.basicweather.ui.main;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basicphones.basicweather.database.ForecastViewModel;
import com.basicphones.basicweather.model.DeviceLocation;
import com.basicphones.basicweather.model.Forecast;
import com.basicphones.basicweather.R;
import com.basicphones.basicweather.Utility;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basicphones.basicweather.MainActivity.PREFERENCES_FILE_NAME;


/**
 * A weather fragment to test OpenWeatherMap API.
 */
public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final double CONST_OF_INCHES_OF_MERCURY = 0.02953;

    private double mLatitude, mLongitude;

    private TextView txtLocation,
            txtTemperature,
            txtMinTemperature,
            txtMaxTemperature,
            txtWindSpeed,
            txtHumidity,
            txtPressure,
            txtWeatherDescription;
    private ImageView weatherIcon;
    private SharedPreferences sharedPreferences;


    public static WeatherFragment newInstance(Double lat, Double lon) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(LATITUDE, lat);
        bundle.putDouble(LONGITUDE, lon);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "On create");

        if (getArguments() != null) {
            mLatitude = getArguments().getDouble(LATITUDE);
            mLongitude = getArguments().getDouble(LONGITUDE);
        }


        // init shared pref
        sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_FILE_NAME,0);

    }
    private void updateWeatherDetails(Forecast currentWeather) {

        Typeface calibriFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/calibri.ttf");

        txtTemperature.setText(String.format("%s °F", Math.round(currentWeather.getCurrentConditions().getTemperature())));
        txtTemperature.setTypeface(calibriFont);

        txtMinTemperature.setText(String.format("%s °F", Math.round(currentWeather.getDailyForecast().getDailyConditionsList().get(0).getTemperatureMin())));
        txtMinTemperature.setTypeface(calibriFont);

        txtMaxTemperature.setText(String.format("%s °F", Math.round(currentWeather.getDailyForecast().getDailyConditionsList().get(0).getTemperatureMax())));
        txtMaxTemperature.setTypeface(calibriFont);


        txtWindSpeed.setText(String.format("%s at %s mph", Utility.windDegreeToTextualDescription(currentWeather.getCurrentConditions().getWindBearing()), currentWeather.getCurrentConditions().getWindSpeed()));
        txtWindSpeed.setTypeface(calibriFont);

        txtHumidity.setText(String.format("%s %%", currentWeather.getCurrentConditions().getHumidity()));
        txtHumidity.setTypeface(calibriFont);

        txtPressure.setText(String.format("%s ''", Math.round(currentWeather.getCurrentConditions().getPressure() * CONST_OF_INCHES_OF_MERCURY) / 100.0));
        txtPressure.setTypeface(calibriFont);

        String desc = currentWeather.getCurrentConditions().getSummary();
        txtWeatherDescription.setText(Utility.capitalize(desc));
        txtWeatherDescription.setTypeface(calibriFont);

        final DeviceLocation deviceLocation = getAddressFromLocation(mLatitude, mLongitude);
        if (deviceLocation.getCity() == null) {
            MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.MAPBOX_SDK_TOKEN))
                    .query(Point.fromLngLat(mLongitude, mLatitude))
                    .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                    .build();

            reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {

                    List<CarmenFeature> results = response.body().features();

                    if (results.size() > 0) {

                        // Log the first results Point.
                        Point firstResultPoint = results.get(0).center();
                        Log.d(TAG, "onResponse: " + firstResultPoint.toString());

                        deviceLocation.setAddressLine1(results.get(0).placeName().split(",")[0]);
                        deviceLocation.setCity(results.get(0).context().get(1).text());
                        deviceLocation.setPostalCode(results.get(0).context().get(0).text());
                        deviceLocation.setState(results.get(0).context().get(2).text());
                        deviceLocation.setCountryCode(results.get(0).context().get(2).shortCode());
                        deviceLocation.setCountryName(results.get(0).context().get(3).text());

                        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                        sharedPrefEditor.putString("CITY", deviceLocation.getCity());
                        sharedPrefEditor.putString("STATE", deviceLocation.getState());
                        sharedPrefEditor.putString("COUNTRY_CODE", deviceLocation.getCountryCode());
                        sharedPrefEditor.apply();

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                        txtLocation.setText(MessageFormat.format("{0}, {1}, {2}  {3}", deviceLocation.getCity(), deviceLocation.getState(), deviceLocation.getCountryCode(), simpleDateFormat.format(Calendar.getInstance().getTime())));
                    } else {

                        // No result for your request were found.
                        Log.d(TAG, "onResponse: No result found");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        txtLocation.setText(MessageFormat.format("{0}, {1} {2} {3}",
                sharedPreferences.getString("CITY", ""),
                sharedPreferences.getString("STATE", ""),
                sharedPreferences.getString("COUNTRY_CODE", ""),
                simpleDateFormat.format(Calendar.getInstance().getTime())));
        txtLocation.setTypeface(calibriFont);

        int resourceIdentifier = getResources().getIdentifier(currentWeather.getCurrentConditions().getIconString().replace("-", "_"), "drawable", getContext().getPackageName());
        weatherIcon.setImageResource(resourceIdentifier);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        ForecastViewModel forecastViewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        forecastViewModel.getAll().observe(this, new Observer<List<Forecast>>() {
            @Override
            public void onChanged(@Nullable final List<Forecast> forecastList) {
                Log.d(TAG, "Forecast list: " + forecastList);
                if (!forecastList.isEmpty() && forecastList.size() > 0) {
                    updateWeatherDetails(forecastList.get(forecastList.size() - 1));
                } else {
                    Log.e(TAG, getString(R.string.no_cache_data));
                    Toast.makeText(getContext(), getString(R.string.no_cache_data), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });

        txtLocation = root.findViewById(R.id.txtLocation);
        txtTemperature = root.findViewById(R.id.txtTemperature);
        txtMinTemperature = root.findViewById(R.id.txtMinTemperature);
        txtMaxTemperature = root.findViewById(R.id.txtMaxTemperature);
        txtWindSpeed = root.findViewById(R.id.txtWindSpeed);
        txtHumidity = root.findViewById(R.id.txtHumidity);
        txtPressure = root.findViewById(R.id.txtPressure);
        txtWeatherDescription = root.findViewById(R.id.txtWeatherDescription);
        weatherIcon = root.findViewById(R.id.weatherIcon);

        return root;
    }

    /**
     * Function to get correct address from lonitude and latitude
     * @param latitude of device
     * @param longitude of device
     * @return DeviceLocation structure
     */
    private DeviceLocation getAddressFromLocation(final double latitude, final double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        DeviceLocation addressInfo = new DeviceLocation();
        try {
            final List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                addressInfo.setAddressLine1(address.getAddressLine(0));
                addressInfo.setCity(address.getLocality());
                addressInfo.setPostalCode(address.getPostalCode());
                addressInfo.setState(address.getAdminArea());
                addressInfo.setCountryCode(address.getCountryCode());
                addressInfo.setCountryName(address.getCountryName());
                addressInfo.setLatitude(latitude);
                addressInfo.setLongitude(longitude);

                SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                sharedPrefEditor.putString("CITY", address.getLocality());
                sharedPrefEditor.putString("STATE", address.getAdminArea());
                sharedPrefEditor.putString("COUNTRY_CODE", address.getCountryCode());
                sharedPrefEditor.apply();

                return addressInfo;
            }
        } catch (IllegalArgumentException | IOException e) {
            Log.e(TAG, "Unable connect to Geocoder " + e.getMessage());
        }
        return addressInfo;
    }
}