package com.lodestarapp.cs491.lodestar.Models;

public class WeatherInformation {

    private double feelsLikeTemperature;
    private double humidity;
    private String date;

    public WeatherInformation(String date, double feelsLikeTemperature, double humidity){
        this.date = date;
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

    public double convertKelvinTo(String type, double temp){
        if(type.equals("C"))
            return temp - 273.15;
        return temp * (9/5) - 459.67;
    }
}
