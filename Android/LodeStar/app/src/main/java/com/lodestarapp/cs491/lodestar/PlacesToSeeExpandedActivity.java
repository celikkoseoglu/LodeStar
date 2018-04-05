package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PlacesToSeeExpandedActivity extends AppCompatActivity {

    private static final String TAG = "expandedMessage";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see_expanded);

        String value = getIntent().getExtras().getString("place");

        Log.d(TAG, "Value: " + value);
    }
}
