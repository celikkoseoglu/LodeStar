package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.LatLng;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.CurrencyController;
import com.lodestarapp.cs491.lodestar.Controllers.LivingExpensesController;
import com.lodestarapp.cs491.lodestar.Models.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CurrencyActivity extends AppCompatActivity {
    String local = "TRY";
    String fore = "EUR";
    String city1;
    String city2;
    String cityName;
    String airport;

    ArrayList<Places> addresses;
    String address1,address2;
    String coords;
    String key = "AIzaSyAZYzllcdxR-9M8R-_-ell4bevtPv5MTQI";   // dont't change
    String TAG = "crrency";

    boolean completed1 = false;
    boolean completed2 = false;

    TextView add1,add2, btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        findViewById(R.id.scroll).setVisibility(View.GONE);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            city1 = data.getString("City1");
            city2 = data.getString("City2");
            cityName = data.getString("City");
            airport =  data.getString("Airport");
        }
        addresses = new ArrayList<>();
        add1 = findViewById(R.id.textView17);
        btn1 = findViewById(R.id.textView172);

        add2 = findViewById(R.id.textView18);
        btn2 = findViewById(R.id.textView182);

        add1.setVisibility(View.GONE);
        add2.setVisibility(View.GONE);
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> list1 = geocoder.getFromLocationName(city1,1);
            String country1 = list1.get(0).getCountryCode();
            local = getCurrencyCode(country1);

            List<Address> list2 = geocoder.getFromLocationName(city2,1);
            String country2 = list2.get(0).getCountryCode();
            fore = getCurrencyCode(country2);

            List<Address> list3 = geocoder.getFromLocationName(airport + "," + cityName,1);
            coords = list3.get(0).getLatitude() + "," + list3.get(0).getLongitude();
            coords = coords + "";
            sendRequest();
            getAddress();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAddress() {
        final JSONObject[] result = new JSONObject[1];

        String requestFromTheUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?location="+coords+"&key="+key+"&query=currency";
        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestFromTheUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                try {
                    String placeName;
                    String placeAddress;
                    String placeRating;
                    String placeType;
                    String placeId;

                    JSONObject place;

                    JSONObject geometry;
                    JSONObject location;

                    JSONArray types;

                    JSONArray results = response.getJSONArray("results");
                    if(results.length() >= 2)
                    {
                        for(int i =0 ;i<2;i++){
                            place = results.getJSONObject(i);

                            geometry = place.getJSONObject("geometry");
                            location = geometry.getJSONObject("location");

                            types = place.getJSONArray("types");

                            placeName = place.getString("name");
                            placeAddress = place.getString("formatted_address");

                            placeRating = Double.parseDouble(place.getString("rating")) * 2 + "";
                            placeType = types.getString(0);
                            placeId = place.getString("place_id");

                            String coords = location.getDouble("lat")+ "," +location.getDouble("lng");
                            addresses.add(new Places("landmark",null, placeName, placeAddress, placeType,
                                    placeRating, null, placeRating, placeId, coords));

                            if(i==0)
                            {
                                address1 = placeName + "\n" + placeAddress;
                                add1.setText( "1. "  + address1);
                                add1.setVisibility(View.VISIBLE);
                                btn1.setVisibility(View.VISIBLE);
                                btn1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        openVenue(1);
                                    }
                                });
                            }
                            if(i==1){
                                address2 = placeName + "\n" + placeAddress;
                                add2.setText( "2. "  + address2);
                                add2.setVisibility(View.VISIBLE);
                                btn2.setVisibility(View.VISIBLE);
                                btn2.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        openVenue(2);
                                    }
                                });
                            }
                            completed1 = true;
                            if(completed1 && completed2)
                            {
                                findViewById(R.id.ll1).setVisibility(View.GONE);
                                findViewById(R.id.scroll).setVisibility(View.VISIBLE);

                            }
                        }
                    }
                    else{
                        for(int i =0 ;i<results.length();i++){
                            place = results.getJSONObject(i);

                            geometry = place.getJSONObject("geometry");
                            location = geometry.getJSONObject("location");

                            types = place.getJSONArray("types");

                            placeName = place.getString("name");
                            placeAddress = place.getString("formatted_address");

                            placeRating = Double.parseDouble(place.getString("rating")) * 2 + "";
                            placeType = types.getString(0);
                            placeId = place.getString("place_id");

                            String coords = location.getDouble("lat")+ "," +location.getDouble("lng");
                            addresses.add(new Places("landmark",null, placeName, placeAddress, placeType,
                                    placeRating, null, placeRating, placeId, coords));

                            if(i==0)
                            {
                                address1 = placeName + "\n" + placeAddress;
                                add1.setText( "1. "  + address1);
                                add1.setVisibility(View.VISIBLE);
                                btn1.setVisibility(View.VISIBLE);
                                btn1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        openVenue(1);
                                    }
                                });
                            }
                            if(i==1){
                                address2 = placeName + "\n" + placeAddress;
                                add2.setText( "2. "  + address2);
                                add2.setVisibility(View.VISIBLE);
                                btn2.setVisibility(View.VISIBLE);
                                btn2.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        openVenue(2);
                                    }
                                });
                            }
                            completed1 = true;
                            if(completed1 && completed2)
                            {
                                findViewById(R.id.ll1).setVisibility(View.GONE);
                                findViewById(R.id.scroll).setVisibility(View.VISIBLE);

                            }


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //mRecyclerView.setAdapter(mAdapter);
            }

        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get currency information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void openVenue(int i) {
        Intent intent = new Intent(this, PlacesToSeeExpandedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("placeName", addresses.get(i-1).getPlaceName());
        bundle.putString("placeLocation", addresses.get(i-1).getPlaceLocation());
        bundle.putString("placeType", addresses.get(i-1).getPlaceType());
        bundle.putString("placeId", addresses.get(i-1).getPlaceId());
        bundle.putString("placeRating", addresses.get(i-1).getRating());
        bundle.putString("placeCoords", addresses.get(i-1).getCoords());

        intent.putExtras(bundle);
        startActivity(intent);
    }

    CurrencyController cc = new CurrencyController();

    public void sendRequest() {
        String curr = "";

        TextView tx1 = findViewById(R.id.tx0);

        cc.getCurrencyRates(local,fore, this, new CurrencyController.VolleyCallback1(){
            @Override
            public void onSuccess(JSONObject result){

                //Toast toast =  Toast.makeText(getApplicationContext(), js.toString(), Toast.LENGTH_LONG);
                //toast.show();

                try {
                    String lb1 = result.getString("foreignOverLocal");
                    TextView tx1 = findViewById(R.id.textView2);
                    if(lb1 != null)
                        tx1.setText("1 "+local+" = "+lb1 + " " + fore);

                    String lb2 = result.getString("localOverForeign");
                    TextView tx2 = findViewById(R.id.textView3);
                    if(lb2 != null)
                        tx2.setText("1 "+fore+" = "+lb2 + " "+ local);

                    String lb3 = result.getString("localCurrencySymbol");
                    TextView tx4 = findViewById(R.id.textView21);
                    if(lb3 != null)
                        tx4.setText(lb3);

                    String lb4 = result.getString("foreignCurrencySymbol");
                    TextView tx5 = findViewById(R.id.textView23);
                    if(lb4 != null)
                        tx5.setText(lb4);

                    TextView tx6 = findViewById(R.id.textView20);
                    tx6.setText("Symbol for " + local);

                    TextView tx7 = findViewById(R.id.textView22);
                    tx7.setText("Symbol for " + fore);

                    TextView tx8 = findViewById(R.id.textView1);
                    tx8.setText("Exchange Rates in " + cityName);

                    TextView tx9 = findViewById(R.id.textView16);
                    tx9.setText("Exchange Options in " + cityName);

                    completed2 = true;
                    if(completed1 && completed2)
                    {
                        findViewById(R.id.ll1).setVisibility(View.GONE);
                        findViewById(R.id.scroll).setVisibility(View.VISIBLE);

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void tripStart(View view){
        finish();
    }

    public static String getCurrencyCode(String countryCode) {
        return Currency.getInstance(new Locale("", countryCode)).getCurrencyCode();
    }
}
