package com.lodestarapp.cs491.lodestar.Models;

public class Places {

    private String placeName;
    private String placeType;
    private String placeLocation;
    private String placeNumberOfReviews;

    //TODO: places star and picture
    public Places(String placeName, String placeType, String placeLocation, String placeNumberOfReviews){
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeLocation = placeLocation;
        this.placeNumberOfReviews = placeNumberOfReviews;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceType() {
        return placeType;
    }

    public String getPlaceLocation() {
        return placeLocation;
    }

    public String getPlaceNumberOfReviews() {
        return placeNumberOfReviews;
    }
}
