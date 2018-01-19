package com.lodestarapp.cs491.lodestar.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherInformation {

    private String date;
    private String description;
    private double feelsLikeTemperature;
    private double humidity;

    public WeatherInformation(String date, String description, double feelsLikeTemperature, double humidity){
        this.date = date;
        this.description = description;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.humidity = humidity;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
