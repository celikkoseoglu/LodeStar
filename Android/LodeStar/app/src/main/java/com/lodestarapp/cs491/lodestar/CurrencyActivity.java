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
        final String local = "TRY";
        final String fore = "EUR";

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
