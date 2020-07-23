package com.basicphones.basicweather.database;

import com.basicphones.basicweather.model.Alert;
import com.basicphones.basicweather.model.CurrentConditions;
import com.basicphones.basicweather.model.DailyForecast;
import com.basicphones.basicweather.model.ForecastFlags;
import com.basicphones.basicweather.model.HourlyForecast;
import com.basicphones.basicweather.model.MinutelyForecast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;

public class DataTypeConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public String fromCurrentConditionsToString(CurrentConditions value) {
        if (value == null) {
            return null;
        }
        return new Gson().toJson(value);
    }

    @TypeConverter
    public CurrentConditions toCurrentConditions(String data) {
        if (data == null) {
            return null;
        }
        Type type = new TypeToken<CurrentConditions>() {}.getType();
        return new Gson().fromJson(data, type);
    }

    @TypeConverter
    public String fromMinutelyForecastToString(MinutelyForecast value) {
        if (value == null) {
            return null;
        }
        return new Gson().toJson(value);
    }

    @TypeConverter
    public MinutelyForecast toMinutelyForecast(String data) {
        if (data == null) {
            return null;
        }
        Type type = new TypeToken<MinutelyForecast>() {}.getType();
        return new Gson().fromJson(data, type);
    }

    @TypeConverter
    public String fromHourlyForecastToString(HourlyForecast value) {
        if (value == null) {
            return null;
        }
        return new Gson().toJson(value);
    }

    @TypeConverter
    public HourlyForecast toHourlyForecast(String data) {
        if (data == null) {
            return null;
        }
        Type type = new TypeToken<HourlyForecast>() {}.getType();
        return new Gson().fromJson(data, type);
    }


    @TypeConverter
    public String fromDailyForecastToString(DailyForecast value) {
        if (value == null) {
            return null;
        }
        return new Gson().toJson(value);
    }

    @TypeConverter
    public DailyForecast toDailyForecast(String data) {
        if (data == null) {
            return null;
        }
        Type type = new TypeToken<DailyForecast>() {}.getType();
        return new Gson().fromJson(data, type);
    }


    @TypeConverter
    public String fromAlertList(List<Alert> alerts) {
        if (alerts == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Alert>>() {}.getType();
        return gson.toJson(alerts, type);
    }

    @TypeConverter
    public List<Alert> toAlertList(String alertString) {
        if (alertString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Alert>>() {}.getType();
        return gson.fromJson(alertString, type);
    }

    @TypeConverter
    public String fromForecastFlagsToString(ForecastFlags value) {
        if (value == null) {
            return null;
        }
        return new Gson().toJson(value);
    }

    @TypeConverter
    public ForecastFlags toForecastFlags(String data) {
        if (data == null) {
            return null;
        }
        Type type = new TypeToken<ForecastFlags>() {}.getType();
        return new Gson().fromJson(data, type);
    }

}
