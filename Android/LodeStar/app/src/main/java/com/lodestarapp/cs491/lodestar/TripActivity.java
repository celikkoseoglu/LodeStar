package com.lodestarapp.cs491.lodestar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;
import com.lodestarapp.cs491.lodestar.Controllers.FlightInfoController;
import com.lodestarapp.cs491.lodestar.Controllers.LivingExpensesController;
import com.lodestarapp.cs491.lodestar.Models.FlightInfo;
import com.lodestarapp.cs491.lodestar.Models.QRCodeInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class TripActivity extends AppCompatActivity {
    public ViewFlipper view_flipper;
    public View firstView;
    public View secondView;
    private FlightInfo flightInfo;
    private QRCodeInfo qrCodeInfo;
    private FlightInfoController flc = new FlightInfoController();

    ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip2);
        flightInfo = new FlightInfo();

        findViewById(R.id.flipper).setVisibility(View.GONE);

        Button bt5 = findViewById(R.id.flightinfo);
        bt5.setClickable(false);

        //String requestFromTheUrl = "http://10.0.2.2:3006?dataType=flightInfo";
        //String requestFromTheUrl = "http://10.0.2.2:3006?dataType=flightInfo";
        sendRequest();

        Bundle data = getIntent().getExtras();
        if (data != null) {
            qrCodeInfo = data.getParcelable("QRCodeInfo");
        }

        view_flipper =   (ViewFlipper) findViewById(R.id.flipper);

        CardView card = (CardView) findViewById(R.id.my_card);
        firstView= findViewById(R.id.view1);
        secondView = findViewById(R.id.view2);
        card.setOnTouchListener(new OnSwipeTouchListener(TripActivity.this) {
            public void onSwipeLeft() {
                if (view_flipper.getCurrentView() != secondView){
                    view_flipper.setInAnimation(TripActivity.this, R.anim.left_enter);
                    view_flipper.setOutAnimation(TripActivity.this, R.anim.left_out);
                    view_flipper.showNext();
                }
            }

            public void onSwipeRight() {
                if (view_flipper.getCurrentView() != firstView){
                    view_flipper.setOutAnimation(TripActivity.this, R.anim.right_out);
                    view_flipper.setInAnimation(TripActivity.this, R.anim.right_enter);
                    view_flipper.showPrevious();
                }
            }
        });

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        updateSizeInfo();
    }
    private void updateSizeInfo() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.row3);

        int w = linearLayout.getWidth();


        Button bt = findViewById(R.id.living);
        ViewGroup.LayoutParams params = bt.getLayoutParams();
        //Button new width
        params.height = (w-15)/3;
        bt.setLayoutParams(params);

        Button bt1 = findViewById(R.id.places);
        bt1.setLayoutParams(params);

        Button bt2 = findViewById(R.id.accomodation);
        bt2.setLayoutParams(params);

        Button bt3 = findViewById(R.id.transport);
        bt3.setLayoutParams(params);

        Button bt4 = findViewById(R.id.weather);
        bt4.setLayoutParams(params);

        Button bt5 = findViewById(R.id.flightinfo);
        bt5.setLayoutParams(params);

        Button bt6 = findViewById(R.id.shopping);
        bt6.setLayoutParams(params);

        Button bt7 = findViewById(R.id.lounge);
        bt7.setLayoutParams(params);

        Button bt8 = findViewById(R.id.restaurants);
        bt8.setLayoutParams(params);

    }



    public void weatherStart(View view){
        Intent intent = new Intent(this, WeatherInformationActivity.class);
        startActivity(intent);
    }

    public void flightInfoStart(View view){
        Intent intent = new Intent(this, FlightInfoActivity.class);
        intent.putExtra("FLIGHT_INFO", flightInfo);
        startActivity(intent);
    }

    public void currencyStart(View view){
        Intent intent = new Intent(this, CurrencyActivity.class);
        intent.putExtra("FLIGHT_INFO", flightInfo);
        startActivity(intent);
    }

    public void livingStart(View view){
        Intent intent = new Intent(this, LivingExpensesActivity.class);
        intent.putExtra("FLIGHT_INFO", flightInfo);
        startActivity(intent);
    }


    private static final String TAG = "theMessage";

    public void sendRequest() {
        String flight = "THY26";

        flc.getFlightInfo(flight, this, new FlightInfoController.VolleyCallback2() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String info = result.getString("ident");
                    TextView view = findViewById(R.id.info_text1);

                    //info = qrCodeInfo.getFlightCode();

                    view.setText("You will be boarding " + info + " from");

                    TextView view1 = findViewById(R.id.info_text3);
                    view1.setText("You will be boarding " + info + " to");

                    flightInfo.setLink("https://flightaware.com/live/flight/" + info);

                    JSONObject or = result.getJSONObject("origin");
                    JSONObject des = result.getJSONObject("destination");


                    TextView view2 = findViewById(R.id.info_text5);
                    view2.setText(or.getString("city"));
                    flightInfo.setOrig(or.getString("city"));
                    flightInfo.setOrig_airport(or.getString("airport_name"));

                    TextView view4 = findViewById(R.id.info_text6);
                    view4.setText(des.getString("city"));
                    flightInfo.setDest(des.getString("city"));
                    flightInfo.setDest_airport(des.getString("airport_name"));


                    TextView view5 = findViewById(R.id.info_text2);
                    view5.setText("Swipe right to see information about " + des.getString("city").toString());

                    TextView view3 = findViewById(R.id.info_text4);
                    view3.setText("Swipe right to see information about " + or.getString("city").toString());

                    Log.i(TAG, info.toString());

                    JSONObject depTime = result.getJSONObject("filed_departure_time");
                    flightInfo.setOrig_localtime(depTime.getString("time") + " " + depTime.getString("tz"));
                    flightInfo.setOrig_date(depTime.getString("date"));

                    JSONObject arrTime = result.getJSONObject("filed_arrival_time");
                    flightInfo.setDest_date(arrTime.getString("date"));
                    flightInfo.setDest_localtime(arrTime.getString("time") + " " + arrTime.getString("tz"));

                    String aircarft = result.getString("aircrafttype");
                    flightInfo.setAircraft(aircarft);

                    String distance = result.getString("distance_filed");
                    flightInfo.setDistance(Integer.parseInt(distance));

                    String speed = result.getString("filed_airspeed_kts");
                    flightInfo.setSpeed(Integer.parseInt(speed));

                    String delay = result.getString("arrival_delay");
                    flightInfo.setDelay(Integer.parseInt(delay));


                    findViewById(R.id.ll1).setVisibility(View.GONE);
                    findViewById(R.id.flipper).setVisibility(View.VISIBLE);

                    JSONObject weather = result.getJSONObject("weather");
                    String weatherCond = weather.getString("cloud_friendly");
                    flightInfo.setWeather(weatherCond);

                    String temp = weather.getString("temp_air");
                    flightInfo.setTemperature(Integer.parseInt(temp));

                    String tempFeel = weather.getString("temp_perceived");
                    flightInfo.setFeelsLike(Integer.parseInt(tempFeel));

                    String humidity = weather.getString("temp_relhum");
                    flightInfo.setHumidity(Integer.parseInt(humidity));

                    Button bt5 = findViewById(R.id.flightinfo);
                    bt5.setClickable(true);




                } catch (JSONException jsonException) {
                    Log.e(TAG, "JSON Parsing error");
                }

            }
        });


    }




    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            private static final int SWIPE_DISTANCE_THRESHOLD = 100;

            public boolean onDown(MotionEvent e) {
                return true;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }

}

