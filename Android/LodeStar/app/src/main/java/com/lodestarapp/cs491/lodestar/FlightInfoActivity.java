package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Models.FlightInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlightInfoActivity extends AppCompatActivity {
    FlightInfo flightInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            flightInfo = data.getParcelable("FLIGHT_INFO");

            TextView view = findViewById(R.id.textView1);
            if(flightInfo.getOrig() != null)
                view.setText(flightInfo.getOrig());

            TextView view2 = findViewById(R.id.textView2);
            if(flightInfo.getDest() != null)
                view2.setText(flightInfo.getDest());

            TextView view5 = findViewById(R.id.textView5);
            if(flightInfo.getOrig_airport() != null)
                view5.setText(flightInfo.getOrig_airport());

            TextView view6 = findViewById(R.id.textView6);
            if(flightInfo.getDest_airport() != null)
                view6.setText(flightInfo.getDest_airport());


            TextView view7 = findViewById(R.id.textView7);
            if(flightInfo.getOrig_localtime() != null)
                view7.setText(flightInfo.getOrig_localtime());

            TextView view8 = findViewById(R.id.textView8);
            if(flightInfo.getDest_localtime() != null)
                view8.setText(flightInfo.getDest_localtime());

            TextView view9 = findViewById(R.id.textView9);
            if(flightInfo.getOrig_date() != null)
                view9.setText(flightInfo.getOrig_date());

            TextView view10 = findViewById(R.id.textView10);
            if(flightInfo.getDest_date() != null)
                view10.setText(flightInfo.getDest_date());

            TextView view20 = findViewById(R.id.textView20);
            if(flightInfo.getAircraft() != null)
                view20.setText(flightInfo.getAircraft());

            TextView view25 = findViewById(R.id.textView25);
            if(flightInfo.getDest() != null)
                view25.setText("when you arrive " + flightInfo.getDest());

        }

    }
    public void tripStart(View view){
        finish();
    }


}
