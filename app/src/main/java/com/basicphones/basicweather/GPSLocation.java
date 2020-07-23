package com.basicphones.basicweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class GPSLocation extends AsyncTask<Void, Void, Double[]> implements LocationListener  {

    private static final String TAG = "GPSLocation";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 28800; // 8 hours


    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Double longitude;
    private Double latitude;

    @SuppressLint("MissingPermission")
    public GPSLocation(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    private Double[] getLocationLatLong() {
        Double[] latlong = new Double[2];
        latlong[0] = 0.0;
        latlong[1] = 0.0;

        try {
            // Get the location manager
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                // getting GPS status
                boolean isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                // ——–Gps provider—
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.getMainLooper());

                if (isGPSEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (location != null) {
                        latlong[0] = location.getLatitude();
                        latlong[1] = location.getLongitude();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        locationManager.removeUpdates(this);
                        return latlong;
                    }
                }

                if (isNetworkEnabled) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (location != null) {
                        latlong[0] = location.getLatitude();
                        latlong[1] = location.getLongitude();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        locationManager.removeUpdates(this);
                        return latlong;
                    }
                }

                // use passive provider
                Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (location != null) {
                    latlong[0] = location.getLatitude();
                    latlong[1] = location.getLongitude();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    locationManager.removeUpdates(this);
                    return latlong;
                }
            } else {
                Log.e(TAG, "Location manager is null");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return latlong;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG,  "Lat: " + location.getLatitude());
        Log.d(TAG,  "Long: " + location.getLongitude());

        // only latitude longitude to set up
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        getLocationLatLong();
    }

    @Override
    protected Double[] doInBackground(Void... voids) {
        if (getLatitude() != null && getLongitude() != null) {
            return new Double[] {getLatitude(), getLongitude()};
        }
        return new Double[0];
    }
}
