package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lodestarapp.cs491.lodestar.Adapters.WeatherInformationAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.WeatherInformationController;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;
import com.lodestarapp.cs491.lodestar.Models.WeatherInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherInformationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<WeatherInformation> weatherInformationList = new ArrayList<>();

    private static final String TAG = "theMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mRecyclerView = findViewById(R.id.my_weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        WeatherInformationController weatherInformationController = new
                WeatherInformationController("London");

        sendRequestToServer(weatherInformationController);

        //Adapter
        mAdapter = new WeatherInformationAdapter(weatherInformationList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void sendRequestToServer(WeatherInformationController weatherInformationController) {
        weatherInformationController.getWeatherInformation(weatherInformationController.getRequestFromTheUrl(),
                getApplicationContext(), new LodeStarServerCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                        parseTheJSONResponse(jsonArray);
                    }
                });
    }

    private void parseTheJSONResponse(JSONArray weatherInformationFromServer) {
        try {
            Log.i(TAG, "adapterWeather");
            Log.i(TAG, weatherInformationFromServer.getJSONObject(0).getString("dt_txt"));
            Log.i(TAG, weatherInformationFromServer.getJSONObject(1).getString("dt_txt"));
            Log.i(TAG, weatherInformationFromServer.getJSONObject(2).getString("dt_txt"));
            Log.i(TAG, weatherInformationFromServer.getJSONObject(3).getString("dt_txt"));
            Log.i(TAG, weatherInformationFromServer.getJSONObject(4).getString("dt_txt"));
            Log.i(TAG, weatherInformationFromServer.getJSONObject(5).getString("dt_txt"));

            Log.i(TAG, weatherInformationFromServer.getJSONObject(0).getJSONObject("main").getString("temp"));

            JSONObject process;
            JSONObject main;
            JSONArray weather;

            SimpleDateFormat simpleInputDate = new SimpleDateFormat("yyyy-mm-dd");
            Date dateToConvertToFullName;
            SimpleDateFormat simpleOutputDate = new SimpleDateFormat("EEEE");

            String date;
            double temp;
            double humidity;
            String weatherDescription;

            for (int i = 0; i < 6; i++) {
                process = weatherInformationFromServer.getJSONObject(i);
                main = process.getJSONObject("main");
                weather = process.getJSONArray("weather");

                date = process.getString("dt_txt");
                temp = main.getDouble("temp");
                humidity = main.getDouble("humidity");

                weatherDescription = weather.getJSONObject(0).getString("description");

                try {
                    dateToConvertToFullName = simpleInputDate.parse(date);
                    date = simpleOutputDate.format(dateToConvertToFullName);
                }catch (ParseException parseException){
                    parseException.printStackTrace();
                }

                weatherInformationList.add(i,
                        new WeatherInformation(date, weatherDescription, temp, humidity));
            }
        }catch (JSONException jsonException){
            Log.e(TAG, "JSON Parsing error");
        }

        mAdapter.notifyDataSetChanged();
        findViewById(R.id.weather_progress_bar).setVisibility(View.GONE);

    }
}
