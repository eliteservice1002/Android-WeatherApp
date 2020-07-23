package com.basicphones.basicweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

public class MinutelyForecast {
    private String summary;

    @SerializedName("icon")
    private String iconString;

    @SerializedName("data")
    private List<MinuteConditions> minutelyConditionsList;


    public String getSummary() {
        return summary;
    }

    public String getIconString() {
        return iconString;
    }

    public List<MinuteConditions> getMinutelyConditionsList() {
        return minutelyConditionsList;
    }

    @NonNull
    @Override
    public String toString() {
        return "{MinutelyForecast: [Summary = " + summary + "][IconString = " + iconString +
                "][MinutelyConditionsList = " + minutelyConditionsList.toString() + "]}";
    }
}
