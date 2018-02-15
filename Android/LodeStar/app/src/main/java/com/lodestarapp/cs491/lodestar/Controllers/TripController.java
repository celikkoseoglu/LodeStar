package com.lodestarapp.cs491.lodestar.Controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class TripController {
    String key= "AIzaSyAKnThPPshmgffk3DNPNkXd2glEQaH1Rlw";
    private static final String TAG = "Trip";

    public TripController(){

    }

    public void getTripCity(String city, Context c, final VolleyCallback4 callback) {

        final JSONObject[] result = new JSONObject[1];

        String requestFromTheUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+city+"&key=" + key;

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
                Log.i(TAG, "Failed to get trip information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void getBackgroundImage(String reference,int width, Context c, final VolleyCallback5 callback){
        RequestQueue requestQueue = Volley.newRequestQueue( c.getApplicationContext() );
        String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+width+"&photoreference="+reference+"&key="+ key;

        ImageRequest imageRequest = new ImageRequest(
                imageUrl, // Image URL,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {

                        callback.onSuccess(response);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Failed to get trip image");
                    }
                }
        );

        requestQueue.add(imageRequest);;
    }

    public interface VolleyCallback4{
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallback5{
        void onSuccess(Bitmap result);
    }



}
