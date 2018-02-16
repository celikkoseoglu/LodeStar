package com.lodestarapp.cs491.lodestar.Controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by HÜSEYİN on 10.2.2018.
 */

public class FlightInfoController {

    public FlightInfoController(){

    }

    private static final String TAG = "Flight";

    public void getFlightInfo(String city, Context c, final VolleyCallback2 callback) {

        final JSONObject[] result = new JSONObject[1];

        String requestFromTheUrl = "http://lodestarapp.com:3006/?originAirportCode=IST&destinationAirportCode=PVG&flightNumber=THY26";

        RequestQueue requestQueue = Volley.newRequestQueue( c.getApplicationContext() );


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestFromTheUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());

                callback.onSuccess(response);

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

    public interface VolleyCallback2{
        void onSuccess(JSONObject result);
    }

}
