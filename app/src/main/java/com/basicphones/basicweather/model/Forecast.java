package com.basicphones.basicweather.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forecast_table")
public class Forecast implements Serializable {

    public static final String EXTEND_HOURLY_KEY = "extend";
    public static final String EXTEND_HOURLY_VALUE = "hourly";

    private double latitude;
    private double longitude;
    private String timezone;
    @Deprecated
    private int offset;

    @PrimaryKey
    @NonNull
    @SerializedName("currently")
    private CurrentConditions currentConditions;

    @SerializedName("minutely")
    private MinutelyForecast minutelyForecast;

    @SerializedName("hourly")
    private HourlyForecast hourlyForecast;

    @SerializedName("daily")
    private DailyForecast dailyForecast;

    @SerializedName("alerts")
    private List<Alert> alerts;

    @SerializedName("flags")
    private ForecastFlags flags;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getOffset() {
        return offset;
    }

    public CurrentConditions getCurrentConditions() {
        return currentConditions;
    }

    public MinutelyForecast getMinutelyForecast() {
        return minutelyForecast;
    }

    public HourlyForecast getHourlyForecast() {
        return hourlyForecast;
    }

    public DailyForecast getDailyForecast() {
        return dailyForecast;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public ForecastFlags getFlags() {
        return flags;
    }

    @NonNull
    @Override
    public String toString() {
        return "Forecast:\n" +
                "\t\tLatitude: " + latitude + "\n" +
                "\t\tLongitude: " + longitude + "\n" +
                "\t\tTimezone: " + timezone + "\n" +
                "\t\tOffset: " + offset + "\n" +
                "\t\tCurrently: " + (currentConditions != null ? currentConditions.toString() : "null") + "\n" +
                "\t\tMinutely: " + (minutelyForecast != null ? minutelyForecast.toString() : "null") + "\n" +
                "\t\tHourly: " + (hourlyForecast != null ? hourlyForecast.toString() : "null") + "\n" +
                "\t\tDaily: " + (dailyForecast != null ? dailyForecast.toString() : "null") + "\n" +
                "\t\tAlerts: " + (alerts != null ? alerts.toString() : "null") + "\n" +
                "\t\tFlags: " + (flags != null ? flags.toString() : "null") + "\n";
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setCurrentConditions(CurrentConditions currentConditions) {
        this.currentConditions = currentConditions;
    }

    public void setMinutelyForecast(MinutelyForecast minutelyForecast) {
        this.minutelyForecast = minutelyForecast;
    }

    public void setHourlyForecast(HourlyForecast hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public void setDailyForecast(DailyForecast dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public void setFlags(ForecastFlags flags) {
        this.flags = flags;
    }

    public enum Language {
        ARABIC("ar"),
        AZERBAIJANI("az"),
        BELARUSIAN("be"),
        BOSNIAN("bs"),
        CZECH("cs"),
        GERMAN("de"),
        GREEK("el"),
        ENGLISH("en"),
        SPANISH("es"),
        FRENCH("fr"),
        CROATIAN("hr"),
        HUNGARIAN("hu"),
        INDONESIAN("id"),
        ITALIAN("it"),
        ICELANDIC("is"),
        CORNISH("kw"),
        NORWEGIAN_BOKMAL("nb"),
        DUTCH("nl"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        RUSSIAN("ru"),
        SLOVAK("sk"),
        SERBIAN("sr"),
        SWEDISH("sv"),
        TETUM("tet"),
        TURKISH("tr"),
        UKRAINIAN("uk"),
        PIG_LATIN("x-pig-latin"),
        CHINESE_SIMPLIFIED("zh"),
        CHINESE_TRADITIONAL("zh-tw");

        public static final String HTTP_QUERY_KEY = "lang";
        private String mKey;

        Language(String key) {
            mKey = key;
        }

        @NonNull
        @Override
        public String toString() {
            return mKey;
        }
    }

    public enum Units {
        AUTO("auto"),
        CA("ca"),
        UK2("uk2"),
        US_IMPERIAL("us"),
        SI("si");

        public static final String HTTP_QUERY_KEY = "units";
        private String mKey;

        Units(String key) {
            mKey = key;
        }

        @NonNull
        @Override
        public String toString() {
            return mKey;
        }
    }

    public enum Exclusion {
        CURRENTLY("currently"),
        MINUTELY("minutely"),
        HOURLY("hourly"),
        DAILY("daily"),
        ALERTS("alerts"),
        FLAGS("flags");

        public static final String HTTP_QUERY_KEY = "exclude";
        private String mKey;

        Exclusion(String key) {
            mKey = key;
        }

        @NonNull
        @Override
        public String toString() {
            return mKey;
        }
    }
}
