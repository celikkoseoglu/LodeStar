package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

            TextView view11 = findViewById(R.id.textView11);
            if(flightInfo.getDistance() != 0)
                view11.setText("Direct Distance: " + Math.round(flightInfo.getDistance()* 1.609344) + " km");

            TextView view12 = findViewById(R.id.textView12);
            if(flightInfo.getSpeed() != 0)
                view12.setText("Planned speed: " + Math.round(flightInfo.getSpeed() * 1.852)  + " km/h");

            TextView view14 = findViewById(R.id.textView14);
                view14.setText(flightInfo.getDelay()/(60) + " minutes" );

            TextView view20 = findViewById(R.id.textView20);
            if(flightInfo.getAircraft() != null)
                view20.setText(flightInfo.getAircraft());


            TextView view24 = findViewById(R.id.textView24);
            ImageView weat = findViewById(R.id.weatView);
            if(flightInfo.getWeather() != null) {
                String weather = flightInfo.getWeather();
                view24.setText(weather);
                if(weather.toLowerCase().contains("cloud") && !(weather.toLowerCase().contains("scatter") || weather.toLowerCase().contains("broken")) )
                    weat.setImageResource(R.drawable.cloud);
                else if(weather.toLowerCase().contains("sun") || weather.toLowerCase().contains("clear"))
                    weat.setImageResource(R.drawable.sun);
                else if(weather.toLowerCase().contains("rain"))
                    weat.setImageResource(R.drawable.rain);
                else if(weather.toLowerCase().contains("fog") || weather.toLowerCase().contains("wind") )
                    weat.setImageResource(R.drawable.fog);
                else if(weather.toLowerCase().contains("storm"))
                    weat.setImageResource(R.drawable.storm);
                else if(weather.toLowerCase().contains("snow"))
                    weat.setImageResource(R.drawable.snow);
                else
                    weat.setImageResource(R.drawable.broken_cloud);
            }

            TextView view26 = findViewById(R.id.textView26);
                view26.setText(flightInfo.getTemperature() + "°C");

            TextView view27 = findViewById(R.id.textView27);
            view27.setText( "feels like: " + flightInfo.getFeelsLike()  + "°C" );

            TextView view28 = findViewById(R.id.textView28);
            view28.setText(  flightInfo.getHumidity()+ "%" );





            TextView view25 = findViewById(R.id.textView25);
            if(flightInfo.getDest() != null)
                view25.setText("when you arrive " + flightInfo.getDest());


            TextView view22 = findViewById(R.id.textView22);
            if(flightInfo.getWifi() != null)
                view22.setText(flightInfo.getWifi());

        }

    }
    public void tripStart(View view){
        finish();
    }

    public void openLink(View view) {
        if(flightInfo.getAircraft() != null){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?tbm=isch&q=" + flightInfo.getAircraft()));
            startActivity(browserIntent);
        }

    }
}
