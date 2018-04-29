package com.lodestarapp.cs491.lodestar.Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;

import org.json.JSONArray;
import org.json.JSONException;

public class PlacesToSeeController {

    private static final String TAG = "controller";
    private boolean locationPermissionGiven;
    private String requestFromUrl;
    private Location location;

    public PlacesToSeeController(boolean locationPermissionGiven, Location location){
        this.locationPermissionGiven = locationPermissionGiven;
        this.location = location;

        if(this.locationPermissionGiven){
            //Server part not implemented

            this.requestFromUrl = "http://lodestarapp.com:3010/?city=ankara";

            // this.requestFromUrl = "http://lodestarapp.com:3009/?location="+location.getLatitude()+
            //","+location.getLongitude()+"&limit=5";

            Log.i(TAG, this.requestFromUrl);
        }
        else{
            this.requestFromUrl = "something";
        }
    }

    public void getPlacesToSeeInformation(String requestFromUrl, Context context,
                                          final LodeStarServerCallback lodeStarServerCallback){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final JSONArray[] responseFromServer = new JSONArray[1];

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                requestFromUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                responseFromServer[0] = response;
                try {
                    lodeStarServerCallback.onSuccess(responseFromServer[0], null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i(TAG, "Failed to get weather information of the city " + getCity());
                //Log.i(TAG, error.getMessage());
                //Log.i(TAG, error.getLocalizedMessage());
                //Log.i(TAG, error.toString());
                responseFromServer[0] = null;
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getPlaceImage(String imageURL, Context context, final LodeStarServerCallback lodeStarServerCallback){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final Bitmap[] imageMap = new Bitmap[1];

        ImageRequest imageRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (response != null) {
                    imageMap[0] = response;
                    lodeStarServerCallback.onPlaceImageSuccess(imageMap[0]);
                }

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageMap[0] = null;
                        Log.i(TAG, "Failed to get venue image");
                    }
                });

        requestQueue.add(imageRequest);

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRequestFromUrl() {
        return requestFromUrl;
    }

}
