package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Controllers.CurrencyController;
import com.lodestarapp.cs491.lodestar.Controllers.LivingExpensesController;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        findViewById(R.id.scroll).setVisibility(View.GONE);
        sendRequest();
    }

    CurrencyController cc = new CurrencyController();

    public void sendRequest() {
        String curr = "";

        TextView tx1 = findViewById(R.id.tx0);

        cc.getCurrencyRates("TRY","EUR", this, new CurrencyController.VolleyCallback1(){
            @Override
            public void onSuccess(JSONObject result){

                //Toast toast =  Toast.makeText(getApplicationContext(), js.toString(), Toast.LENGTH_LONG);
                //toast.show();

                try {
                    String lb1 = result.getString("foreignOverLocal");
                    TextView tx1 = findViewById(R.id.textView2);
                    if(lb1 != null)
                        tx1.setText("1 TL="+lb1 + " EUR");

                    String lb2 = result.getString("localOverForeign");
                    TextView tx2 = findViewById(R.id.textView3);
                    if(lb2 != null)
                        tx2.setText("1 EUR="+lb2 + " TL");


                    findViewById(R.id.ll1).setVisibility(View.GONE);
                    findViewById(R.id.scroll).setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void tripStart(View view){
        finish();
    }
}
