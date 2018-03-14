package com.lodestarapp.cs491.lodestar.Controllers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlacesToSeeController {

    private static final String TAG = "controller";
    private boolean locationPermissionGiven;
    private String requestFromUrl;
    private String keyword;
    private Location location;


    public PlacesToSeeController(String keyword, boolean locationPermissionGiven, Location location){
        this.keyword = keyword;
        this.locationPermissionGiven = locationPermissionGiven;
        this.location = location;

        if(this.locationPermissionGiven){
            //Server part not implemented
            this.requestFromUrl = "http://lodestarapp.com:3008/?location="+location.getLatitude()+
            ","+location.getLongitude()+"&limit=5";

            Log.i(TAG, this.requestFromUrl);
        }
        else{
            this.requestFromUrl = "something";
        }
    }

    public void getPlacesToSeeInformation(String requestFromUrl, Context context,
                                          final LodeStarServerCallback lodeStarServerCallback){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                requestFromUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                responseFromServer[0] = response;
                lodeStarServerCallback.onSuccess(null, responseFromServer[0]);
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
        requestQueue.add(jsonObjectRequest);
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

    public String getRequestFromUrl() {
        return requestFromUrl;
    }

    public void setRequestFromUrl(String requestFromUrl) {
        this.requestFromUrl = requestFromUrl;
    }

    /*private boolean checkForLocationPermission() {




        return false;
    }*/

}
