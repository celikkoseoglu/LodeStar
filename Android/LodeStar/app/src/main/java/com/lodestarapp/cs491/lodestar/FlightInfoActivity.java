package com.lodestarapp.cs491.lodestar;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlightInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_info);

        String requestFromTheUrl = "http://10.0.2.2:3001?dataType=flightInfo";
        sendRequestToServer(requestFromTheUrl);

    }
    private static final String TAG = "theMessage";

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

            private void parseTheJSONResponse(JSONObject flightInformationFromServer) {
                String info;

                try {
                    Log.i(TAG, flightInformationFromServer.toString());
                    info = flightInformationFromServer.getString("ident");
                    Log.i(TAG, info.toString());
                    TextView view =  findViewById(R.id.textView1);
                    view.setText(info.toString());

                }catch (JSONException jsonException){
                    Log.e(TAG, "JSON Parsing error");
                }



                //findViewById(R.id.weather_progress_bar).setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Failed to get weather information");
                Log.i(TAG, error.getMessage());
                Log.i(TAG, error.getLocalizedMessage());
                Log.i(TAG, error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
