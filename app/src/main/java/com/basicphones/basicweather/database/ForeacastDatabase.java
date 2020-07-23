package com.basicphones.basicweather.database;

import android.content.Context;

import com.basicphones.basicweather.model.Forecast;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Forecast.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class ForeacastDatabase extends RoomDatabase {
    public abstract ForecastDao forecastDao();

    private static volatile ForeacastDatabase INSTANCE;

    static ForeacastDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ForeacastDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ForeacastDatabase.class, "forecast_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
