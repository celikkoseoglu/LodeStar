package com.lodestarapp.cs491.lodestar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Adapters.WeatherInformationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherInformationActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<String> descriptionList = new ArrayList<>();
    private List<Double> temperatureList = new ArrayList<>();
    private List<Double> humidityList = new ArrayList<>();

    private static final String TAG = "theMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mRecyclerView = findViewById(R.id.my_weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Request Weather Information from the Server
        String requestFromTheUrl = "http://10.0.2.2:3001?dataType=weather&city=London";
        sendRequestToServer(requestFromTheUrl);

        Log.i(TAG, "hi");

        for (int i = 0; i < 5; i++) {
            descriptionList.add("");
            temperatureList.add(0.0);
            humidityList.add(0.0);
        }

        //Adapter
        mAdapter = new WeatherInformationAdapter(descriptionList, temperatureList, humidityList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void sendRequestToServer(String requestFromTheUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestFromTheUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                responseFromServer[0] = response;

                parseTheJSONResponse(responseFromServer[0]);

                //mAdapter = new WeatherInformationAdapter(responseFromServer[0]);
                //mRecyclerView.setAdapter(mAdapter);

            }

            private void parseTheJSONResponse(JSONObject weatherInformationFromServer) {
                JSONArray jsonListArray;

                //For now, take the first five

                try {
                    Log.i(TAG, "adapterWeather");
                    Log.i(TAG, weatherInformationFromServer.toString());
                    jsonListArray = weatherInformationFromServer.getJSONArray("list");
                    Log.i(TAG, jsonListArray.toString());

                    JSONObject theList;
                    JSONObject main;
                    JSONArray weather;

                    double temp;
                    double humidity;
                    String weatherDescription;

                    for (int i = 0; i < 1; i++) {
                        theList = jsonListArray.getJSONObject(i);

                        main = theList.getJSONObject("main");
                        temp = main.getDouble("temp");
                        humidity = main.getDouble("humidity");

                        weather = theList.getJSONArray("weather");
                        weatherDescription = weather.getJSONObject(i).getString("description");

                        Log.i(TAG, "adapterWeather");

                        Log.i(TAG, "" + temp);
                        Log.i(TAG, "" + humidity);
                        Log.i(TAG, weatherDescription);

                        temperatureList.add(i, temp);
                        humidityList.add(i, temp);
                        descriptionList.add(i, weatherDescription);

                    }
                }catch (JSONException jsonException){
                    Log.e(TAG, "JSON Parsing error");
                }

                mAdapter.notifyDataSetChanged();
                findViewById(R.id.weather_progress_bar).setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get weather information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
                responseFromServer[0] = null;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
