package com.lodestarapp.cs491.lodestar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.lodestarapp.cs491.lodestar.Controllers.LivingExpensesController;

import org.json.JSONException;
import org.json.JSONObject;

public class LivingExpensesActivity extends AppCompatActivity {

    JSONObject js;
    LivingExpensesController lc = new LivingExpensesController();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_expenses);


        findViewById(R.id.sv1).setVisibility(View.GONE);


        sendRequest();


    }


    public void onResume(){
        super.onResume();

        sendRequest();


    }



    public void sendRequest() {
        String city = "Ankara";

        TextView tx1 = findViewById(R.id.tx0);
        if(city != null)
            tx1.setText(" Approximate Living Expenses In "+ city);

        lc.getLivingExpenses(city, this, new LivingExpensesController.VolleyCallback(){
            @Override
            public void onSuccess(JSONObject result){

                js = result;

                //Toast toast =  Toast.makeText(getApplicationContext(), js.toString(), Toast.LENGTH_LONG);
                //toast.show();

                try {
                    String lb1 = js.getString("A single person monthly costs");
                    TextView tx1 = findViewById(R.id.tx1);
                    if(lb1 != null)
                        tx1.setText("   A single person monthly costs: \n   "+ lb1);

                    String lb2 = js.getString("Meal, Inexpensive Restaurant");
                    TextView tx2 = findViewById(R.id.textView3);
                    if(lb2 != null)
                        tx2.setText("   Meal (inexpensive restaurant): \n   "+ lb2);

                    String lb3 = js.getString("Apartment (1 bedroom) in City Centre");
                    TextView tx3 = findViewById(R.id.textView4);
                    if(lb3 != null)
                        tx3.setText("   Apartment rent (1 bedroom, city centre): \n   "+ lb3);

                    String lb4 = js.getString("Monthly Pass");
                    TextView tx4 = findViewById(R.id.textView5);
                    if(lb4 != null)
                        tx4.setText("   Monthly Transport Pass: \n   "+ lb4);

                    String lb5 = js.getString("Coke/Pepsi (0.33 liter bottle)");
                    TextView tx5 = findViewById(R.id.textView6);
                    if(lb5 != null)
                        tx5.setText("   Coke/Pepsi (0.33 liter bottle): \n   "+ lb5);

                    String lb6 = js.getString("McMeal at McDonalds");
                    TextView tx6 = findViewById(R.id.textView7);
                    if(lb6 != null)
                        tx6.setText("   McMeal at McDonalds: \n   "+ lb6);

                    String lb7 = js.getString("Taxi Start");
                    TextView tx7 = findViewById(R.id.textView8);
                    if(lb7 != null)
                        tx7.setText("   Taxi Start: \n   "+ lb7);
                    tx7.invalidate();


                    String lb8 = js.getString("Taxi 1km");
                    TextView tx8 = findViewById(R.id.textView9);
                    if(lb8 != null)
                        tx8.setText("   Taxi 1 km: \n   "+ lb8);

                    findViewById(R.id.ll1).setVisibility(View.GONE);
                    findViewById(R.id.sv1).setVisibility(View.VISIBLE);

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
