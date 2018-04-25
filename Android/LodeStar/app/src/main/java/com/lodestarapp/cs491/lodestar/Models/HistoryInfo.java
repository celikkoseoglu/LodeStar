package com.lodestarapp.cs491.lodestar.Models;

public class HistoryInfo {

    private String flightCode;
    private String cityFrom;
    private String cityTo;
    private String fromAirport;
    private String fromAirportIdent;
    private String toAirport;
    private String toAirportIdent;
    private long departureTime;
    private long arrivalTime;
    private String departureDate;
    private String arrivalDate;

    public HistoryInfo(String flightCode, String cityFrom, String cityTo,
                       String fromAirport, String fromAirportIdent,
                       String toAirport, String toAirportIdent,
                       long departureTime, long arrivalTime,
                       String departureDate, String arrivalDate){

        this.flightCode = flightCode;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.fromAirport = fromAirport;
        this.fromAirportIdent = fromAirportIdent;
        this.toAirport = toAirport;
        this.toAirportIdent = toAirportIdent;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getFromAirportIdent() {
        return fromAirportIdent;
    }

    public void setFromAirportIdent(String fromAirportIdent) {
        this.fromAirportIdent = fromAirportIdent;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public String getToAirportIdent() {
        return toAirportIdent;
    }

    public void setToAirportIdent(String toAirportIdent) {
        this.toAirportIdent = toAirportIdent;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
