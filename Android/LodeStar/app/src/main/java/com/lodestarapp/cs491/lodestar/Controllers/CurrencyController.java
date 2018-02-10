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

public class CurrencyController {

    public CurrencyController() {

    }

    private static final String TAG = "Currency";

    public void getCurrencyRates(String local, String fore, Context c, final VolleyCallback1 callback) {

        final JSONObject[] result = new JSONObject[1];
        Context x = c;

        String requestFromTheUrl = "http://lodestarapp.com:3008/?localCurrency="+local+"&foreignCurrency="+fore;

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
                Log.i(TAG, "Failed to get currency information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public interface VolleyCallback1{
        void onSuccess(JSONObject result);
    }



}
