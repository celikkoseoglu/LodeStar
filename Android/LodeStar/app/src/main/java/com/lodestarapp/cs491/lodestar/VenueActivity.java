package com.lodestarapp.cs491.lodestar;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class VenueActivity extends AppCompatActivity {

    private String placeId;
    private ArrayList<String> photoReferences;
    private ArrayList<String> venueReviews;
    private ArrayList<String> reviewers;
    private ArrayList<Bitmap> venueBitmaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        String placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");
        this.placeId = getIntent().getExtras().getString("placeId");


        photoReferences = new ArrayList<>();
        venueReviews = new ArrayList<>();
        reviewers = new ArrayList<>();
        venueBitmaps = new ArrayList<>();

        ImageView im = findViewById(R.id.close);
        im.setVisibility(View.GONE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        Button b1 = findViewById(R.id.near_me_add_to_favorites);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeIdNearMe = placeId;
                sendPlaceIdToDatabase(placeIdNearMe);
            }
        });


    }

    private void sendPlaceIdToDatabase(String placeIdNearMe) {
        //Database e kaydolcak
    }

    public void nearStart(View view) {
        finish();
    }

    public void goSmall(View view) {
    }

    public void goVr(View view) {
    }

    public void goFullscreen(View view) {
    }
}
