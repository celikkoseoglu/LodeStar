package com.lodestarapp.cs491.lodestar.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherInformation {

    private String city;
    private String date;
    private String description;
    private double temperature;
    private double feelsLikeTemperature;
    private double humidity;
    private int weatherId;

    public WeatherInformation(String city, String date, String description, double temperature,
                              double feelsLikeTemperature, double humidity, int weatherId){
        this.city = city;
        this.date = date;
        this.description = description;
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.humidity = humidity;
        this.weatherId = weatherId;
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

    public double getTemperature() {
        return temperature;
    }

    public String getCity() {
        return city;
    }

    public int getWeatherId() {
        return weatherId;
    }
}
