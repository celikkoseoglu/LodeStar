package com.lodestarapp.cs491.lodestar.Controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VenueController {

    public VenueController(){

    }

    private static final String TAG = "venue cont";

    public void getPanorama(String location, String radius, String query, Context c, final VenueController.VolleyCallback callback) {
        String requestFromTheUrl = "http://lodestarapp.com:3011/?ll="+location+"&radius="+radius+"&resol="+ query;

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);

    }

    public interface VolleyCallback{
        void onSuccess(JSONObject result);
    }
}
