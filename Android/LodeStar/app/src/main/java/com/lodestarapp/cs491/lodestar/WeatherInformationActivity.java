package com.lodestarapp.cs491.lodestar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WeatherInformationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private WeatherInformationController weatherInformationController;

    private List<WeatherInformation> weatherInformationList = new ArrayList<>();

    private SparseArray weatherIconMap;

    private static final String TAG = "theMessage";

    private ProgressDialog progressDialog;
    String city = "Ankara";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        findViewById(R.id.weather_progress_bar).setVisibility(View.GONE);

        progressDialog = new ProgressDialog(WeatherInformationActivity.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Retrieving weather information from the server");
        progressDialog.show();

        weatherIconMap = new SparseArray();
        weatherIconMap.append(2, "storm");
        weatherIconMap.append(5, "rain");
        weatherIconMap.append(6, "snow");
        weatherIconMap.append(7, "fog");
        
        weatherIconMap.append(8, "broken_cloud");

        mRecyclerView = findViewById(R.id.my_weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        weatherInformationController = new
                WeatherInformationController(city);

        sendRequestToServer(weatherInformationController);

        //Adapter
        mAdapter = new WeatherInformationAdapter(weatherIconMap, this.getApplicationContext() ,weatherInformationList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void sendRequestToServer(WeatherInformationController weatherInformationController) {
        weatherInformationController.getWeatherInformation(weatherInformationController.getRequestFromTheUrl(),
                getApplicationContext(), new LodeStarServerCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                        parseTheJSONResponse(jsonArray);
                    }

                    @Override
                    public void onPlaceImageSuccess(Bitmap bitmap) {}
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
            //Log.i(TAG, weatherInformationFromServer.getJSONObject(5).getString("dt_txt"));

            Log.i(TAG, weatherInformationFromServer.getJSONObject(0).getJSONObject("main").getString("temp"));

            JSONObject process;
            JSONObject main;
            JSONArray weather;

            SimpleDateFormat simpleInputDate = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
            Date dateToConvertToFullName;
            SimpleDateFormat simpleOutputDate = new SimpleDateFormat("EEEE", Locale.US);

            String date;
            double temp;
            double humidity;
            String weatherDescription;
            int weatherID;
            double feelsLike;

            for (int i = 0; i < 5; i++) {
                process = weatherInformationFromServer.getJSONObject(i);
                main = process.getJSONObject("main");
                weather = process.getJSONArray("weather");

                date = process.getString("dt_txt");
                temp = main.getDouble("temp");
                humidity = main.getDouble("humidity");
                feelsLike = main.getDouble("perceived_temp");

                weatherDescription = weather.getJSONObject(0).getString("description");
                String wth = "";
                for(int j = 0; j< weatherDescription.length();j++)
                    if(j == 0)
                        wth += Character.toUpperCase(weatherDescription.charAt(0));
                    else if(weatherDescription.charAt(j-1) == ' ')
                        wth +=  Character.toUpperCase(weatherDescription.charAt(j));
                    else
                        wth += weatherDescription.charAt(j);

                weatherID = Integer.parseInt(weather.getJSONObject(0).getString("id"));

                Log.d(TAG, "........ " + weatherID);

                try {
                    dateToConvertToFullName = simpleInputDate.parse(date);
                    date = simpleOutputDate.format(dateToConvertToFullName);
                }catch (ParseException parseException){
                    parseException.printStackTrace();
                }

                weatherInformationList.add(i,
                        new WeatherInformation(weatherInformationController.getCity(),
                                date, wth, temp, feelsLike, humidity, weatherID));
            }
        }catch (JSONException jsonException){
            Log.e(TAG, "JSON Parsing error");
        }

        mAdapter.notifyDataSetChanged();
        findViewById(R.id.weather_progress_bar).setVisibility(View.GONE);

        progressDialog.dismiss();

    }

    public void tripBack(View view) {
        finish();
    }
}
