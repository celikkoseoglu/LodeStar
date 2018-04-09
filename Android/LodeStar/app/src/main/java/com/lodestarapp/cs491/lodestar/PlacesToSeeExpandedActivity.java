package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class PlacesToSeeExpandedActivity extends AppCompatActivity {

    private static final String TAG = "expandedMessage";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see_expanded);

        String placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");

        Log.d(TAG, "Value: " + placeName);
        Log.d(TAG, "Value: " + placeLocation);
        Log.d(TAG, "Value: " + placeType);

        TextView t1 = findViewById(R.id.place_name_expanded);
        t1.setText(placeName);

        TextView t2 = findViewById(R.id.place_type_expanded);
        t2.setText(placeType);

        TextView t3 = findViewById(R.id.place_location_expanded);
        t3.setText(placeLocation);
    }
}
