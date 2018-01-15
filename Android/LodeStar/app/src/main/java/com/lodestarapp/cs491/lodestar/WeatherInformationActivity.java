package com.lodestarapp.cs491.lodestar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class WeatherInformationActivity extends Activity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_weather_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Adapter

        String requestFromTheUrl = "http://localhost:3001?dataType=weather&city=London";


        sendRequestToServer(requestFromTheUrl);

    }

    private void sendRequestToServer(String requestFromTheUrl) {

    }

}
