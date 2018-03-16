package com.lodestarapp.cs491.lodestar.Models;

public class Places {

    private String placeName;
    private String placeType;
    private String placeLocation;
    private String placeNumberOfReviews;
    private String rating;

    private int numberOfStars;
    private boolean halfStar;

    //TODO: places star and picture
    public Places(String placeName, String placeType, String placeLocation, String placeNumberOfReviews,
                  String rating){
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeLocation = placeLocation;
        this.placeNumberOfReviews = placeNumberOfReviews;
        this.rating = rating;

        int rate;
        int pointPart = Integer.parseInt(rating.substring(rating.indexOf('.') + 1));

        if(pointPart >= 5)
            rate = Integer.parseInt(rating.substring(0, rating.indexOf('.')) + 1);
        else
            rate = Integer.parseInt(rating.substring(0, rating.indexOf('.')));

        this.numberOfStars = rate / 2;
        halfStar = rate % 2 != 0;

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

    public String getRating() {
        return rating;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public boolean isHalfStar() {
        return halfStar;
    }
}
