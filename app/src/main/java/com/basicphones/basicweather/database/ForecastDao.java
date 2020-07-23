package com.basicphones.basicweather.database;

import com.basicphones.basicweather.model.Forecast;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ForecastDao {

    @Insert
    void insert(Forecast forecast);

    @Insert
    void insertAll(Forecast... forecasts);

    @Query("DELETE FROM forecast_table")
    void deleteAll();

    @Delete
    void delete(Forecast forecast);

    @Query("SELECT * from forecast_table")
    LiveData<List<Forecast>> getAll();
}
