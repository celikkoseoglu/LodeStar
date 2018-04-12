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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NearMeController {

    private static final String TAG = "controller";
    private boolean locationPermissionGiven;
    private String requestFromUrl;
    private String keyword;
    private Location location;


    public NearMeController(String keyword, boolean locationPermissionGiven, Location location){
        this.keyword = keyword;
        this.locationPermissionGiven = locationPermissionGiven;
        this.location = location;

        if(this.locationPermissionGiven){
            this.requestFromUrl = "http://lodestarapp.com:3009/?location=39.9098703,32.86061219999999&limit=10&query=park";

            //Log.i(TAG, this.requestFromUrl);
        }
        else{
            this.requestFromUrl = "something";
        }
    }

    public void getNearMeInformation( Context context,
                                          final VolleyCallback5 lodeStarServerCallback){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final JSONArray[] responseFromServer = new JSONArray[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestFromUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());

                lodeStarServerCallback.onSuccess(response);

                //mAdapter = new WeatherInformationAdapter(responseFromServer[0]);
                //mRecyclerView.setAdapter(mAdapter);
            }

        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get flight information");
                //Log.i(TAG, error.getMessage());
                //Log.i(TAG, error.getLocalizedMessage());
                //Log.i(TAG, error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void getPlaceImage(String imageURL, Context context, final VolleyCallback6 lodeStarServerCallback){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final Bitmap[] imageMap = new Bitmap[1];

        ImageRequest imageRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (response != null) {
                    imageMap[0] = response;
                    lodeStarServerCallback.onSuccess(imageMap[0]);
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getRequestFromUrl() {return requestFromUrl;
    }

    public void setRequestFromUrl(String requestFromUrl) {
        this.requestFromUrl = requestFromUrl;
    }

    public interface VolleyCallback5{
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallback6{
        void onSuccess(Bitmap result);
    }


}
