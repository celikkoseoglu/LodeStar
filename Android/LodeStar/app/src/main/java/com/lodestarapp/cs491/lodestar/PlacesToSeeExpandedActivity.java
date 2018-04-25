package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.ADDITIONAL_USER;
import com.lodestarapp.cs491.lodestar.Adapters.NearMeAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeExpandedAdapter;
import com.lodestarapp.cs491.lodestar.VR.PanoramaView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PlacesToSeeExpandedActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    private static final String TAG = "expandedMessage";
    private String placeId;
    private ArrayList<String> photoReferences;
    private ArrayList<String> landmarkReviews;
    private ArrayList<String> reviewers;
    private ArrayList<Double> ratings;
    private ArrayList<Bitmap> landmarkBitmaps;
    ADDITIONAL_USER au;
    DatabaseReference mDatabase;
    PanoramaView pv;

    private String API = "AIzaSyAKnThPPshmgffk3DNPNkXd2glEQaH1Rlw";

    private CardView cv1;
    private CardView cv2;
    private CardView cv3;

    DatabaseReference ref;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private SliderLayout slider;
    private String placeName;
    private double rating;
    ImageView[] placeStarImages = new ImageView[5];
    TextView t11;

    private Button b1;



    Sensor gameRotatiton;
    SensorManager sManager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see_expanded);
        pv = findViewById(R.id.layout);
        ScrollView sc = findViewById(R.id.scroll_general);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        /*sc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    Rect r = new Rect();
                    pv.getDrawingRect(r);
                    if(r.contains((int)motionEvent.getX(),(int)motionEvent.getY()))
                        return true;
                }
                return false;
            }
        });*/

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gameRotatiton = sManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        ImageView im = findViewById(R.id.close);
        im.setVisibility(View.GONE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        cv1 = findViewById(R.id.place_general);
        cv2 = findViewById(R.id.pano);
        cv3 = findViewById(R.id.reviews);

        toolbar = findViewById(R.id.my_toolbar);

        this.placeName = getIntent().getExtras().getString("placeName");
        String placeLocation = getIntent().getExtras().getString("placeLocation");
        String placeType = getIntent().getExtras().getString("placeType");

        this.placeId = getIntent().getExtras().getString("placeId");
        this.rating = Double.parseDouble(getIntent().getExtras().getString("placeRating"));

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

        photoReferences = new ArrayList<>();
        landmarkReviews = new ArrayList<>();
        reviewers = new ArrayList<>();
        landmarkBitmaps = new ArrayList<>();
        ratings = new ArrayList<>();

        Log.d(TAG, "Value: " + placeName);
        Log.d(TAG, "Value: " + placeLocation);
        Log.d(TAG, "Value: " + placeType);
        Log.d(TAG, "PLACE id: " + placeId);

        slider = findViewById(R.id.slider);
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
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

        //b1 = findViewById(R.id.landmarks_add_to_favorites);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String placeIdToDatabase = placeId;
//
//                sendLandmarkPlaceIdsToDatabase(placeIdToDatabase);
//            }
//        });

        recyclerView = findViewById(R.id.place_review_recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlacesToSeeExpandedAdapter(landmarkReviews, reviewers,ratings);
        recyclerView.setAdapter(adapter);

        retrieveMoreInformationsAndPhotos();
    }

    private void sendLandmarkPlaceIdsToDatabase(final String placeIdToDatabase) {
        //Database e burada atabilirsin

        //Olur abi :)
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    FirebaseUser userMe;
                    userMe = FirebaseAuth.getInstance().getCurrentUser();
                    Log.i("agam",userMe.getEmail() + " vs " + au.getemail());
                    if(userMe.getEmail().equals(au.getemail())) {
                        String str = "";
                        if(au.getfavorites() != null) {
                            str = au.getfavorites();
                            mDatabase.child("users").child(childSnapshot.getKey()).child("favorites").setValue(placeIdToDatabase + "!" + au.getfavorites() );
                        } else {
                            mDatabase.child("users").child(childSnapshot.getKey()).child("favorites").setValue(placeIdToDatabase);
                        }



                        Log.i("agam","placeID: " + placeIdToDatabase);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//



    }

    protected void onResume() {
        super.onResume();

        sManager.registerListener(pv.pr,gameRotatiton, SensorManager.SENSOR_DELAY_GAME);
        //sManager.registerListener(pv.pr,magnSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    private void retrieveMoreInformationsAndPhotos() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://lodestarapp.com:3010/placeDetails?placeid=" + placeId;

        final JSONObject[] responseFromServer = new JSONObject[1];

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                responseFromServer[0] = response;
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
            TextView t4 = findViewById(R.id.place_phone_number_expanded);
            if(jsonObject.has("international_phone_number")){
                String phoneNumber = jsonObject.getString("international_phone_number");
                t4.setText(phoneNumber);
            }
            else
                t4.setText("Phone Number is not avaliable");


            JSONArray photoR = jsonObject.getJSONArray("photos");
            int size = photoR.length();
            if(size >5)
                size =5;
            String pRef;

            for (int i = 0; i < size; i++) {
                pRef = photoR.getJSONObject(i).getString("photo_reference");
                Log.d(TAG, "i is: " + i);
                Log.d(TAG, "Photo reference: " + pRef);
                this.photoReferences.add(pRef);
            }
            setSlider();

            JSONArray reviews = jsonObject.getJSONArray("reviews");
            int size2 = reviews.length();
            String nameSurname;
            String review;
            Double r;

            for (int i = 0; i < size2; i++) {
                nameSurname = reviews.getJSONObject(i).getString("author_name");
                review = reviews.getJSONObject(i).getString("text");
                r = Double.valueOf(reviews.getJSONObject(i).getInt("rating"));
                long l = reviews.getJSONObject(i).getLong("time");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String s = sdf.format(new Date(l* 1000));

                Log.d(TAG, "Review is: " + review);
                Log.d(TAG, "Name surname: " + nameSurname);
                this.landmarkReviews.add(review );
                this.reviewers.add(nameSurname + " - "+ s);
                this.ratings.add(r);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    private void setSlider(){
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;

        //Log.d(TAG,photoReferences.toString());

        HashMap<String,String> url_maps = new HashMap<String, String>();
        for(int i =0;i<photoReferences.size();i++)
            url_maps.put(placeName+ " "+(i+1), "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+screenWidth+"&photoreference="+photoReferences.get(i)+"&key="+API);

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

    private void getLandmarkPhotos() {

        //REFERENCE:https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int screenWidth = point.x;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest;
        StringBuilder imageURL = new StringBuilder("");

        //bu değişecek
        for (int i = 0; i < 1; i++) {
            imageURL.append("https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=").append(screenWidth).append("&photoreference=");

            imageURL.append(photoReferences.get(i));
            imageURL.append("&key=").append(API);

            final int finalI = i;
            imageRequest = new ImageRequest(imageURL.toString(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    if(response != null){
                        landmarkBitmaps.add(response);
                        if(finalI == 0){
                            //ImageView placeImageView = findViewById(R.id.imageViewPlace);
                            //placeImageView.setImageBitmap(response);
                        }
                    }
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Log.d(TAG, "Error on Volley: " + error.toString());
                        }
                    });

            requestQueue.add(imageRequest);
            imageURL.setLength(0);
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

    protected void onStop() {
        sManager.unregisterListener(pv.pr);
        slider.stopAutoCycle();
        super.onStop();
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

    public void goSmall(View view){
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
        intent.putExtra("VrAngle", 0);

        startActivity(intent);

    }

}
