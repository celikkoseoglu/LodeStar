package com.lodestarapp.cs491.lodestar;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeExpandedAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesToSeeExpandedActivity extends AppCompatActivity {

    private static final String TAG = "expandedMessage";
    private String placeId;
    private ArrayList<String> photoReferences;
    private ArrayList<String> landmarkReviews;
    private ArrayList<String> reviewers;
    private ArrayList<Bitmap> landmarkBitmaps;

    private String API = "AIzaSyAKnThPPshmgffk3DNPNkXd2glEQaH1Rlw";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see_expanded);

        String placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");

        this.placeId = getIntent().getExtras().getString("placeId");

        photoReferences = new ArrayList<>();
        landmarkReviews = new ArrayList<>();
        reviewers = new ArrayList<>();
        landmarkBitmaps = new ArrayList<>();

        Log.d(TAG, "Value: " + placeName);
        Log.d(TAG, "Value: " + placeLocation);
        Log.d(TAG, "Value: " + placeType);
        Log.d(TAG, "PLACE id: " + placeId);

        TextView t1 = findViewById(R.id.place_name_expanded);
        t1.setText(placeName);

        TextView t2 = findViewById(R.id.place_type_expanded);
        t2.setText(placeType);

        TextView t3 = findViewById(R.id.place_location_expanded);
        t3.setText(placeLocation);

        recyclerView = findViewById(R.id.place_review_recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlacesToSeeExpandedAdapter(landmarkReviews, reviewers);
        recyclerView.setAdapter(adapter);

        retrieveMoreInformationsAndPhotos();

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

            TextView t4 = findViewById(R.id.place_phone_number_expanded);
            t4.setText(phoneNumber);

            JSONArray photoR = jsonObject.getJSONArray("photos");
            int size = photoR.length();
            String pRef;

            for (int i = 0; i < size; i++) {
                pRef = photoR.getJSONObject(i).getString("photo_reference");
                Log.d(TAG, "i is: " + i);
                Log.d(TAG, "Photo reference: " + pRef);
                this.photoReferences.add(pRef);
            }

            JSONArray reviews = jsonObject.getJSONArray("reviews");
            int size2 = reviews.length();
            String nameSurname;
            String review;

            for (int i = 0; i < size2; i++) {
                nameSurname = reviews.getJSONObject(i).getString("author_name");
                review = reviews.getJSONObject(i).getString("text");
                Log.d(TAG, "Review is: " + review);
                Log.d(TAG, "Name surname: " + nameSurname);
                this.landmarkReviews.add(review);
                this.reviewers.add(nameSurname);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

        getLandmarkPhotos();
    }

    private void getLandmarkPhotos() {

        //REFERENCE:https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest;
        StringBuilder imageURL = new StringBuilder("");

        //bu değişecek
        for (int i = 0; i < 1; i++) {
            imageURL.append("https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=").append(screenWidth).append("&photoreference=");

            imageURL.append(photoReferences.get(i));
            imageURL.append("&key=").append(API);

            final int finalI = i;
            imageRequest = new ImageRequest(imageURL.toString(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if(response != null){
                        landmarkBitmaps.add(response);
                        if(finalI == 0){
                            ImageView placeImageView = findViewById(R.id.imageViewPlace);
                            placeImageView.setImageBitmap(response);
                        }
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d(TAG, "Error on Volley: " + error.toString());
                        }
                    });

            requestQueue.add(imageRequest);
            imageURL.setLength(0);
        }
    }


}
