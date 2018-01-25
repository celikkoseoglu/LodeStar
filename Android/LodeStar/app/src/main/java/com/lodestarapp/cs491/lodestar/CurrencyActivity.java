package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
    }

    public void tripStart(View view){
        Intent intent = new Intent(this, TripActivity.class);
        startActivity(intent);
    }
}
