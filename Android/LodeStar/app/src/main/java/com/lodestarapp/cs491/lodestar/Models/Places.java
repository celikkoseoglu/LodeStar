package com.lodestarapp.cs491.lodestar.Models;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class Places {

    private Bitmap placeImage;
    private String placeName;
    private String placeLocation;
    private String placeType;
    private String rating;
    private Bitmap placeIconImage;
    private String attribution;
    private String placeId;
    private String coords;

    private int numberOfStars;
    private boolean halfStar;
    private String originPage;

    //TODO: places star and picture
    public Places(String originPage, Bitmap placeImage, String placeName, String placeLocation,
                  String placeType,
                  String rating, Bitmap placeIconImage, String attribution, String placeId, String coords){
        this.placeImage = placeImage;
        this.placeName = placeName;
        this.placeLocation = placeLocation;
        this.placeType = placeType;
        this.rating = rating;
        this.placeIconImage = placeIconImage;
        this.attribution = attribution;
        this.placeId = placeId;
        this.coords = coords;
        this.originPage = originPage;

        double r = Double.parseDouble(rating) / 2;
        this.numberOfStars = (int) r ;

        if(r % 1 >= 0.5)
            halfStar = true;
        else
            halfStar=false;
        //halfStar = rate % 2 != 0;
        Log.d("stars", rating+ "");


    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceLocation() {
        return placeLocation;
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

    public Bitmap getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(Bitmap placeImage){
        this.placeImage = placeImage;
    }

    public Bitmap getPlaceIconImage() {
        return placeIconImage;
    }

    public void setPlaceIconImage(Bitmap placeIconImage){
        this.placeIconImage = placeIconImage;
    }

    public String getAttribution() {
        return attribution;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getOriginPage() {
        return originPage;
    }

    public void setOriginPage(String originPage) {
        this.originPage = originPage;
    }
}
