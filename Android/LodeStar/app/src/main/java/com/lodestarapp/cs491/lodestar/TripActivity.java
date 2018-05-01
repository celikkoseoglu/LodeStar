package com.lodestarapp.cs491.lodestar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lodestarapp.cs491.lodestar.Controllers.FlightInfoController;
import com.lodestarapp.cs491.lodestar.Controllers.TripController;
import com.lodestarapp.cs491.lodestar.Interfaces.MyOnFocusListenable;
import com.lodestarapp.cs491.lodestar.Models.FlightInfo;
import com.lodestarapp.cs491.lodestar.Models.ImageStorage;
import com.lodestarapp.cs491.lodestar.Models.QRCodeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class TripActivity extends Fragment implements MyOnFocusListenable {
    public ViewFlipper view_flipper;
    public View firstView;
    public View secondView;
    private FlightInfo flightInfo = new FlightInfo();
    private QRCodeInfo qrCodeInfo;
    private String origCity;
    private String destCity;
    private FlightInfoController flc = new FlightInfoController();
    private TripController trp = new TripController();
    private String photoReferenceOrig;
    private String photoReferenceDest;

    private int backgroundImageWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trip2, container, false);

        view.findViewById(R.id.flipper).setVisibility(View.GONE);

        //String requestFromTheUrl = "http://10.0.2.2:3006?dataType=flightInfo";
        if (isStoragePermissionGranted())
            sendRequest();

        Bundle data = getActivity().getIntent().getExtras();
        if (data != null) {
            qrCodeInfo = data.getParcelable("QRCodeInfo");
        }

        Button bt = view.findViewById(R.id.flightinfo);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flightInfoStart(v);
            }
        });
        bt.setClickable(false);

        Button weather = view.findViewById(R.id.weather);
        weather.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                weatherStart(v);
            }
        });
        weather.setClickable(false);


        view_flipper = view.findViewById(R.id.flipper);

        CardView card = view.findViewById(R.id.my_card);
        firstView = view.findViewById(R.id.view1);
        secondView = view.findViewById(R.id.view2);

        card.setOnTouchListener(new OnSwipeTouchListener(this.getActivity()) {
            public void onSwipeLeft() {
                if (view_flipper.getCurrentView() != secondView) {
                    view_flipper.setInAnimation(TripActivity.this.getActivity(), R.anim.left_enter);
                    view_flipper.setOutAnimation(TripActivity.this.getActivity(), R.anim.left_out);
                    view_flipper.showNext();
                }
            }

            public void onSwipeRight() {
                if (view_flipper.getCurrentView() != firstView) {
                    view_flipper.setOutAnimation(TripActivity.this.getActivity(), R.anim.right_out);
                    view_flipper.setInAnimation(TripActivity.this.getActivity(), R.anim.right_enter);
                    view_flipper.showPrevious();
                }
            }
        });

        return view;
    }

    public void onWindowFocusChanged(boolean hasFocus, int w) {
        updateSizeInfo(w);
    }

    public void updateSizeInfo(int width) {
        if (getView() != null) {

            backgroundImageWidth = width;
            //Toast toast =  Toast.makeText(getApplicationContext(), backgroundImageWidth + "", Toast.LENGTH_LONG);
            //toast.show();

            Button bt = getView().findViewById(R.id.living);
            ViewGroup.LayoutParams params = bt.getLayoutParams();
            //Button new width
            params.height = (width - 15) / 3;
            bt.setLayoutParams(params);

            Button bt1 = getView().findViewById(R.id.places);
            bt1.setLayoutParams(params);

            Button bt2 = getView().findViewById(R.id.accomodation);
            bt2.setLayoutParams(params);

            Button bt3 = getView().findViewById(R.id.transport);
            bt3.setLayoutParams(params);

            Button bt4 = getView().findViewById(R.id.weather);
            bt4.setLayoutParams(params);

            Button bt5 = getView().findViewById(R.id.flightinfo);
            bt5.setLayoutParams(params);

            Button bt6 = getView().findViewById(R.id.shopping);
            bt6.setLayoutParams(params);

            Button bt7 = getView().findViewById(R.id.lounge);
            bt7.setLayoutParams(params);

            Button bt8 = getView().findViewById(R.id.restaurants);
            bt8.setLayoutParams(params);

            Button bt9 = getView().findViewById(R.id.landmarks);
            bt9.setLayoutParams(params);

            Button bt10 = getView().findViewById(R.id.sim);
            bt10.setLayoutParams(params);

            Button bt11 = getView().findViewById(R.id.na);
            bt11.setLayoutParams(params);
        }
    }


    public void flightInfoStart(View view) {
        Intent intent = new Intent(getActivity(), FlightInfoActivity.class);
        intent.putExtra("FLIGHT_INFO", flightInfo);
        startActivity(intent);
    }

    public void weatherStart(View view){
        Intent intent = new Intent(getActivity(), WeatherInformationActivity.class);
        if (view_flipper.getCurrentView() == firstView)
            intent.putExtra("City", origCity);
        else
            intent.putExtra("City", destCity);

        startActivity(intent);
    }

    private static final String TAG = "theMessage";

    public void sendRequest() {
        String flight = "THY26"; //bunun binis kartından alınması gerekmiyo mu??????

        flc.getFlightInfo("IST", "PVG", flight, getActivity(), new FlightInfoController.VolleyCallback2() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String info = result.getString("ident");
                    TextView view = getView().findViewById(R.id.info_text1);

                    //info = qrCodeInfo.getFlightCode();

                    view.setText("You will be boarding " + info + " from");

                    TextView view1 = getView().findViewById(R.id.info_text3);
                    view1.setText("You will be boarding " + info + " to");

                    flightInfo.setLink("https://flightaware.com/live/flight/" + info);

                    JSONObject origin = result.getJSONObject("origin");
                    JSONObject des = result.getJSONObject("destination");


                    TextView view2 = getView().findViewById(R.id.info_text5);
                    view2.setText(origin.getString("city"));
                    flightInfo.setOrig(origin.getString("city"));
                    flightInfo.setOrig_airport(origin.getString("airport_name"));

                    origCity = origin.getString("city");
                    destCity = des.getString("city");

                    TextView view4 = getView().findViewById(R.id.info_text6);
                    view4.setText(des.getString("city"));
                    flightInfo.setDest(des.getString("city"));
                    flightInfo.setDest_airport(des.getString("airport_name"));


                    TextView view5 = getView().findViewById(R.id.info_text2);
                    view5.setText("Swipe left to see information about " + des.getString("city").toString());

                    TextView view3 = getView().findViewById(R.id.info_text4);
                    view3.setText("Swipe right to see information about " + origin.getString("city").toString());

                    Log.i(TAG, info);

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


                    JSONObject weather = result.getJSONObject("weather");
                    String weatherCond = weather.getString("cloud_friendly");
                    flightInfo.setWeather(weatherCond);

                    String temp = weather.getString("temp_air");
                    flightInfo.setTemperature(Integer.parseInt(temp));

                    String tempFeel = weather.getString("temp_perceived");
                    flightInfo.setFeelsLike(Integer.parseInt(tempFeel));

                    String humidity = weather.getString("temp_relhum");
                    flightInfo.setHumidity(Integer.parseInt(humidity));

                    boolean wifi = result.getBoolean("adhoc");
                    flightInfo.setWifi(Boolean.toString(wifi));

                    Button bt5 = getView().findViewById(R.id.flightinfo);
                    bt5.setClickable(true);

                    Button bt2 = getView().findViewById(R.id.weather);
                    bt2.setClickable(true);


                    sendImageRequestOrig();
                    sendImageRequestDest();


                } catch (JSONException jsonException) {
                    Log.e(TAG, "JSON Parsing error");
                }

            }
        });


    }

    public void sendImageRequestOrig() {
        final Context c = getActivity();
        if (getView() != null) {
            trp.getTripCity(origCity, c, new TripController.VolleyCallback4() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        JSONArray x = result.getJSONArray("results");
                        JSONObject jo = x.getJSONObject(0);

                        JSONArray photos = jo.getJSONArray("photos");
                        JSONObject photo = photos.getJSONObject(0);
                        photoReferenceOrig = photo.getString("photo_reference");

                        //Toast toast =  Toast.makeText(getApplicationContext(), photoReference, Toast.LENGTH_LONG);
                        //toast.show();

                        final ImageView orig = getView().findViewById(R.id.orig);

                        if (ImageStorage.checkIfImageExists(origCity)) {
                            File file = ImageStorage.getImage("/" + origCity + ".png");
                            assert file != null;
                            String p = file.getAbsolutePath();
                            Bitmap b = BitmapFactory.decodeFile(p);
                            orig.setImageBitmap(b);

                            //Toast toast =  Toast.makeText(c, backgroundImageWidth, Toast.LENGTH_LONG);
                            //toast.show();
                            getView().findViewById(R.id.ll1).setVisibility(View.GONE);
                            getView().findViewById(R.id.flipper).setVisibility(View.VISIBLE);

                        } else {
                            trp.getBackgroundImage(photoReferenceOrig, backgroundImageWidth, c, new TripController.VolleyCallback5() {
                                @Override
                                public void onSuccess(Bitmap result) {
                                    orig.setImageBitmap(result);
                                    ImageStorage.saveToSdCard(result, origCity);

                                    getView().findViewById(R.id.ll1).setVisibility(View.GONE);
                                    getView().findViewById(R.id.flipper).setVisibility(View.VISIBLE);
                                }
                            });
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    public void sendImageRequestDest() {
        final Context c = getActivity();
        trp.getTripCity(destCity, c, new TripController.VolleyCallback4() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray x = result.getJSONArray("results");
                    JSONObject jo = x.getJSONObject(0);

                    JSONArray photos = jo.getJSONArray("photos");
                    JSONObject photo = photos.getJSONObject(0);
                    photoReferenceDest = photo.getString("photo_reference");

                    //Toast toast =  Toast.makeText(getApplicationContext(), photoReference, Toast.LENGTH_LONG);
                    //toast.show();
                    final ImageView dest = getView().findViewById(R.id.dest);

                    if (ImageStorage.checkIfImageExists(destCity)) {
                        File file = ImageStorage.getImage("/" + destCity + ".png");
                        assert file != null;
                        String p = file.getAbsolutePath();
                        Bitmap b = BitmapFactory.decodeFile(p);
                        dest.setImageBitmap(b);

                        //Toast toast =  Toast.makeText(c, "image found", Toast.LENGTH_LONG);
                        //toast.show();

                        getView().findViewById(R.id.ll1).setVisibility(View.GONE);
                        getView().findViewById(R.id.flipper).setVisibility(View.VISIBLE);

                    } else {
                        trp.getBackgroundImage(photoReferenceDest, backgroundImageWidth, c, new TripController.VolleyCallback5() {
                            @Override
                            public void onSuccess(Bitmap result) {
                                dest.setImageBitmap(result);
                                ImageStorage.saveToSdCard(result, destCity);

                                getView().findViewById(R.id.ll1).setVisibility(View.GONE);
                                getView().findViewById(R.id.flipper).setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


}



