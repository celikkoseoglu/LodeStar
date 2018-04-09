package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesToSeeExpandedActivity extends AppCompatActivity {

    private static final String TAG = "expandedMessage";
    private String placeId;
    private ArrayList<String> photoReferences;
    private ArrayList<String> landmarkReviews;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see_expanded);

        String placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");

        this.placeId = getIntent().getExtras().getString("placeId");

        photoReferences = new ArrayList<>();
        landmarkReviews = new ArrayList<>();

        Log.d(TAG, "Value: " + placeName);
        Log.d(TAG, "Value: " + placeLocation);
        Log.d(TAG, "Value: " + placeType);

        TextView t1 = findViewById(R.id.place_name_expanded);
        t1.setText(placeName);

        TextView t2 = findViewById(R.id.place_type_expanded);
        t2.setText(placeType);

        TextView t3 = findViewById(R.id.place_location_expanded);
        t3.setText(placeLocation);

        //retrieveMoreInformationsAndPhotos();

    }

    private void retrieveMoreInformationsAndPhotos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://lodestarapp.com:3010/placeDetails?placeid=" + placeId;

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                responseFromServer[0] = response;
                parseMoreInformationAndPhotos(responseFromServer[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseFromServer[0] = null;
                Log.d(TAG, "Error occured in request");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void parseMoreInformationAndPhotos(JSONObject jsonObject) {
        try {
            String phoneNumber = jsonObject.getString("international_phone_number");

            JSONArray photoR = jsonObject.getJSONArray("photos");
            int size = photoR.length();
            String pRef;

            for (int i = 0; i < size; i++) {
                pRef = photoR.getJSONObject(i).getString("photo_reference");
                this.photoReferences.add(pRef);
            }

            JSONArray reviews = jsonObject.getJSONArray("reviews");
            int size2 = reviews.length();
            String review;

            for (int i = 0; i < size2; i++) {
                review = reviews.getJSONObject(i).getString("text");
                this.landmarkReviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
