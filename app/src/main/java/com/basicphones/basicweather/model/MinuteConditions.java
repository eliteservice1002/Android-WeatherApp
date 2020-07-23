package com.basicphones.basicweather.model;

import androidx.annotation.NonNull;

public class MinuteConditions {
    private int time;
    private double precipIntensity;
    private double precipProbability;


    public int getTime() {
        return time;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    @NonNull
    @Override
    public String toString() {
        return "{MinuteConditions: [Time = " + time + "][PrecipIntensity = " + precipIntensity +
                "][PrecipProbability = " + precipProbability + "]}";
    }
}
