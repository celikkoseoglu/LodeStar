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

public class LivingExpensesController {

    public LivingExpensesController() {

    }

    private static final String TAG = "Living";

    public void getLivingExpenses(String city, Context c, final VolleyCallback callback) {

        final JSONObject[] result = new JSONObject[1];

        city.replaceAll(" ", "-");

        Context x = c;

        String requestFromTheUrl = "http://lodestarapp.com:3007/?city=" + city;

        RequestQueue requestQueue = Volley.newRequestQueue( c.getApplicationContext() );

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestFromTheUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                responseFromServer[0] = response;


                callback.onSuccess(response);


                //mAdapter = new WeatherInformationAdapter(responseFromServer[0]);
                //mRecyclerView.setAdapter(mAdapter);
            }

        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get living expenses information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
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
