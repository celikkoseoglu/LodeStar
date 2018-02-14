package com.lodestarapp.cs491.lodestar.Controllers;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;

import org.json.JSONArray;

public class WeatherInformationController {

    private String city;
    private String requestFromTheUrl;

    private String TAG = "Weather_Controller";

    public WeatherInformationController(String city){
        this.city = city;
        this.requestFromTheUrl = "http://lodestarapp.com:3005/?city=" + getCity();
    }

    public void getWeatherInformation(String requestFromTheUrl, Context context,
                                      final LodeStarServerCallback lodeStarServerCallback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final JSONArray[] responseFromServer = new JSONArray[1];

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                requestFromTheUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());
                responseFromServer[0] = response;
                lodeStarServerCallback.onSuccess(responseFromServer[0], null);

                //parseTheJSONResponse(responseFromServer[0]);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get weather information of the city " + getCity());
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
                responseFromServer[0] = null;
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    private String getCity() {
        return city;
    }

    public String getRequestFromTheUrl() {
        return requestFromTheUrl;
    }
}
