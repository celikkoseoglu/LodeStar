package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.lodestarapp.cs491.lodestar.Controllers.FlightInfoController;

import org.json.JSONObject;

public class SearchFlightActivity extends AppCompatActivity{

    private FlightInfoController flightInfoController = new FlightInfoController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_search_flight);

        final EditText editText = findViewById(R.id.flight_no_edittext);
        editText.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER){
                    sendRequest(editText.getText().toString());
                }
                return true;
            }
        });
    }

    private void sendRequest(String s) {
        flightInfoController.getFlightInfo("IST", "IST", s,
                getApplicationContext(), new FlightInfoController.VolleyCallback2() {
            @Override
            public void onSuccess(JSONObject result) {
                parseTheJSONObject(result);
            }
        });
    }

    private void parseTheJSONObject(JSONObject result) {
        //parse and bundle data, and start new intent
    }
}
