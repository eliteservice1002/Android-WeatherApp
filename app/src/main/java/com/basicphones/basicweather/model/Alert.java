package com.basicphones.basicweather.model;

import androidx.annotation.NonNull;

public class Alert {
    private String title;
    private int time;
    private int expires;
    private String description;
    private String uri;


    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public int getExpiresTime() {
        return expires;
    }

    public String getDescription() {
        return description;
    }

    public String getUri() {
        return uri;
    }

    @NonNull
    @Override
    public String toString() {
        return "{Alert: " +
                "[Title = " + title +
                "][Time = " + time +
                "][Expires = " + expires +
                "][Description = " + description +
                "][URI = " + uri + "]}";

    }
}
