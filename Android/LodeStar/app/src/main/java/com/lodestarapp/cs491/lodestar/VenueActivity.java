package com.lodestarapp.cs491.lodestar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeExpandedAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.TripController;
import com.lodestarapp.cs491.lodestar.Controllers.VenueController;
import com.lodestarapp.cs491.lodestar.Models.ImageStorage;
import com.lodestarapp.cs491.lodestar.VR.PanoramaView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class VenueActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    private String placeId;
    private ArrayList<String> photoReferences;
    private ArrayList<String> venueReviews;
    private ArrayList<String> reviewers;
    private ArrayList<Bitmap> venueBitmaps;
    private ArrayList<Double> ratings;
    private String coords;
    private double rating;

    private Button b1;
    Sensor gameRotatiton;
    SensorManager sManager;
    VenueController vc;
    RelativeLayout relative;
    LinearLayout ll1;
    PanoramaView pv;

    private CardView cv1;
    private CardView cv2;
    private CardView cv3;
    private Toolbar toolbar;
    ImageView[] placeStarImages = new ImageView[5];
    private SliderLayout slider;
    TextView t11;
    String VRBitmap;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String TAG = "venue";
    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");
        this.placeId = getIntent().getExtras().getString("placeId");
        coords = getIntent().getExtras().getString("placeCoords");
        this.rating = Double.parseDouble(getIntent().getExtras().getString("placeRating"));

        pv=findViewById(R.id.layout);
        relative = findViewById(R.id.relative);
        ll1 =findViewById(R.id.ll1);

        pv.setVisibility(View.GONE);
        relative.setVisibility(View.GONE);
        ll1.setVisibility(View.VISIBLE);

        ScrollView sc = findViewById(R.id.scroll_general);
        
        photoReferences = new ArrayList<>();
        venueReviews = new ArrayList<>();
        reviewers = new ArrayList<>();
        ratings = new ArrayList<>();

        ImageView im = findViewById(R.id.close);
        im.setVisibility(View.GONE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        Button b1 = findViewById(R.id.near_me_add_to_favorites);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String placeIdNearMe = placeId;
                sendPlaceIdToDatabase(placeIdNearMe);
            }
        });

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gameRotatiton = sManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        cv1 = findViewById(R.id.place_general);
        cv2 = findViewById(R.id.pano);
        cv3 = findViewById(R.id.reviews);
        toolbar = findViewById(R.id.my_toolbar);

        this.placeStarImages[0] = findViewById(R.id.place_stars_image1);
        this.placeStarImages[1] = findViewById(R.id.place_stars_image2);
        this.placeStarImages[2] = findViewById(R.id.place_stars_image3);
        this.placeStarImages[3] = findViewById(R.id.place_stars_image4);
        this.placeStarImages[4] = findViewById(R.id.place_stars_image5);

        double r = rating / 2;
        int numberOfStars = (int) r ;
        boolean halfStar;
        if(r % 1 >= 0.5)
            halfStar = true;
        else
            halfStar=false;

        for ( int i = 0; i < numberOfStars; i++)
            placeStarImages[i].setImageResource(R.drawable.ic_star_full);

        if(halfStar)
            placeStarImages[numberOfStars].setImageResource(R.drawable.ic_star_half_full);

        slider = findViewById(R.id.slider);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);

        TextView t1 = findViewById(R.id.place_name_expanded);
        t1.setText(placeName);

        t11 = findViewById(R.id.place_name_expanded2);
        t11.setText(placeName);


        TextView t2 = findViewById(R.id.place_type_expanded);
        t2.setText(placeType);

        TextView t3 = findViewById(R.id.place_location_expanded);
        t3.setText(placeLocation);

        recyclerView = findViewById(R.id.place_review_recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlacesToSeeExpandedAdapter(venueReviews, reviewers,ratings);
        recyclerView.setAdapter(adapter);

        retrieveMoreInformationsAndPhotos();
        vc = new VenueController();
        if(isStoragePermissionGranted()) {
            vc.getPanorama(coords, "50", "low", getApplicationContext(), new VenueController.VolleyCallback() {

                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        String encoded = result.getString("lowRes");
                        double pano_deg = result.getJSONObject("Projection").getDouble("pano_yaw_deg") ;

                        double angle = (result.getJSONObject("Location").getDouble("best_view_direction_deg") + 450 - pano_deg) % 360.0;
                        //pv.setAngle((float) angle);
                        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                        pv.setBitmap(decodedString);
                        pv.setVisibility(View.VISIBLE);

                        JSONArray links = result.getJSONArray("Links");
                        JSONObject jo;
                        ArrayList<Float> arrows = new ArrayList<>();
                        ArrayList<String> panoids = new ArrayList<>();
                        for (int i = 0; i < links.length(); i++) {
                            jo = links.getJSONObject(i);
                            float ang = 180f - (float) ((float) (jo.getDouble("yawDeg") + 450- pano_deg) % 360.0);
                            if(ang < 0)
                                arrows.add(360+ang);
                            else
                                arrows.add(ang);
                            panoids.add(jo.getString("panoId"));
                        }
                        pv.setArrows(arrows);
                        pv.setPanos(panoids);
                        pv.setCoords(coords);
                        pv.setLL(ll1);

                        vc.getPanorama(coords, "50", "high", getApplicationContext(), new VenueController.VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    String encoded = result.getString("highRes");
                                    byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                                    pv.setBitmap(decodedString);
                                    VRBitmap = coords;
                                    relative.setVisibility(View.VISIBLE);
                                    ll1.setVisibility(View.GONE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }

    }

    private void sendPlaceIdToDatabase(String placeIdNearMe) {
        //Database e kaydolcak
    }

    public void nearStart(View view) {
        finish();
    }

    public void goSmall(View view) {
        t11.setVisibility(View.VISIBLE);
        cv1.setVisibility(View.VISIBLE);
        cv3.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

        PanoramaView panoramaView =  findViewById(R.id.layout);
        ViewGroup.LayoutParams layoutParams=panoramaView.getLayoutParams();
        float scale = this.getResources().getDisplayMetrics().density;
        layoutParams.height= (int) (200 * scale + 0.5f);
        panoramaView.setLayoutParams(layoutParams);

        layoutParams=cv2.getLayoutParams();
        layoutParams.height= -1;
        cv2.setLayoutParams(layoutParams);

        ImageView im = findViewById(R.id.full);
        im.setVisibility(View.VISIBLE);

        ImageView im1 = findViewById(R.id.close);
        im1.setVisibility(View.GONE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        ImageView im3 = findViewById(R.id.vrSmall);
        im3.setVisibility(View.VISIBLE);

    }

    public void goVr(View view) {
        Intent intent = new Intent(this, VRActivity.class);
        if(pv.getCoords() != null)
            intent.putExtra("BitmapName", pv.getCoords());
        intent.putExtra("VrAngle", 0);

        startActivity(intent);

    }

    public void goFullscreen(View view) {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int screenHeight = point.y;


        t11.setVisibility(View.GONE);
        cv1.setVisibility(View.GONE);
        cv3.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);


        PanoramaView panoramaView = findViewById(R.id.layout);
        ViewGroup.LayoutParams layoutParams=panoramaView.getLayoutParams();
        layoutParams.height= -1;
        panoramaView.setLayoutParams(layoutParams);

        layoutParams=cv2.getLayoutParams();
        layoutParams.height= (int) (screenHeight * 0.90);
        cv2.setLayoutParams(layoutParams);

        ImageView im = findViewById(R.id.full);
        im.setVisibility(View.GONE);


        ImageView im1 = findViewById(R.id.close);
        im1.setVisibility(View.VISIBLE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.VISIBLE);

        ImageView im3 = findViewById(R.id.vrSmall);
        im3.setVisibility(View.GONE);

    }

    protected void onStop() {
        sManager.unregisterListener(pv.pr);
        slider.stopAutoCycle();
        super.onStop();
    }

    protected void onResume() {
        super.onResume();

        sManager.registerListener(pv.pr,gameRotatiton, SensorManager.SENSOR_DELAY_GAME);
    }

    private void retrieveMoreInformationsAndPhotos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://lodestarapp.com:3009/?location="+coords+"&limit=1&query=" +deAccent(placeName).replaceAll(" ","%20");

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    responseFromServer[0] = response.getJSONArray("groups").getJSONObject(0).getJSONArray("items").getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parseMoreInformationAndPhotos(responseFromServer[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseFromServer[0] = null;
                Log.d(TAG, "Error occured in request");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void parseMoreInformationAndPhotos(JSONObject jsonObject) {
        try {
            JSONObject venue = jsonObject.getJSONObject("venue");
            TextView t4 = findViewById(R.id.place_phone_number_expanded);
            if(venue.getJSONObject("contact").has("formattedPhone")){
                String phoneNumber = venue.getJSONObject("contact").getString("formattedPhone");
                t4.setText(phoneNumber);
            }
            else
                t4.setText("Phone Number is not avaliable");

            String imgURL = jsonObject.getString("venueImage");
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int screenWidth = point.x;

            imgURL = imgURL.replace("300x300",screenWidth+"x"+screenWidth);
            this.photoReferences.add(imgURL);
            setSlider();

            JSONArray reviews = jsonObject.getJSONArray("tips");
            int size2 = reviews.length();
            String nameSurname;
            String review;
            Double r;

            for (int i = 0; i < size2; i++) {
                if( reviews.getJSONObject(i).getJSONObject("user").has("lastName"))
                    nameSurname = reviews.getJSONObject(i).getJSONObject("user").getString("firstName") + " " + reviews.getJSONObject(i).getJSONObject("user").getString("lastName") ;
                else
                    nameSurname = reviews.getJSONObject(i).getJSONObject("user").getString("firstName");

                review = reviews.getJSONObject(i).getString("text");
                long l = reviews.getJSONObject(i).getLong("createdAt");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String s = sdf.format(new Date(l* 1000));
                Log.d(TAG, "Review is: " + review);
                Log.d(TAG, "Name surname: " + nameSurname);
                this.venueReviews.add(review );
                this.reviewers.add(nameSurname + " - "+ s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    private void setSlider(){
        HashMap<String,String> url_maps = new HashMap<String, String>();
        for(int i =0;i<photoReferences.size();i++)
            url_maps.put(placeName+ " "+(i+1), photoReferences.get(i));

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }
    }





    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    public String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


}
