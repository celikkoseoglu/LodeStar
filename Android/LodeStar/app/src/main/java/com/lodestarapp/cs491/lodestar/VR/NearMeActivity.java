package com.lodestarapp.cs491.lodestar.VR;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lodestarapp.cs491.lodestar.Adapters.NearMeAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.NearMeController;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;
import com.lodestarapp.cs491.lodestar.Models.Places;
import com.lodestarapp.cs491.lodestar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearMeActivity extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout ll1;
    private LinearLayout ll2;


    private List<Places> restList = new ArrayList<>();
    NearMeController nmc;
    private String[] venueImageURL = new String[10];
    private ArrayList<String> iconURLs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        recyclerView1 = findViewById(R.id.near_me_recycler1);
        recyclerView1.setHasFixedSize(true);

        recyclerView2 = findViewById(R.id.near_me_recycler2);
        recyclerView2.setHasFixedSize(true);

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);

        recyclerView1.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);

        adapter = new NearMeAdapter(restList, "restaurants",this.getApplicationContext());
        recyclerView1.setAdapter(adapter);

        nmc = new NearMeController("",true,null);
        nmc.getNearMeInformation(this.getApplicationContext(), new NearMeController.VolleyCallback5() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    parseTheJSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void tripStart(View view) {
        finish();
    }

    private void parseTheJSONObject(JSONObject jo) throws JSONException {
        //Log.d(TAG, "bbb");
        //Log.d("near me", jo.toString());

        JSONArray groups = jo.getJSONArray("groups");
        JSONArray items = groups.getJSONObject(0).getJSONArray("items");
        //Log.d("near me", items.toString());

        JSONObject venue; //name
        JSONObject location; //location
        JSONArray categories; // type

        String placeName;
        String placeLocation;
        String placeType;
        String placeRating;
        String placeId;

        LatLng placeLatLng;
        String numberOfReviews;
        String photoURL;
        String iconURL;

        final Bitmap[] imgBitmap = new Bitmap[1];

        for (int i = 0; i < items.length(); i++){
            venue = items.getJSONObject(i).getJSONObject("venue");
            placeName = venue.getString("name");

            numberOfReviews = items.getJSONObject(i).getString("reviewCount");

            location = venue.getJSONObject("location");
            placeId = venue.getString("id");
            //Log.d(TAG, location.toString());

            placeRating = venue.getDouble("rating") + "";
            //Log.d("rating", placeRating);


            placeLatLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

            //Add marker to google maps
            //googleMap.addMarker(new MarkerOptions().position(placeLatLng).title(placeName));

            try{
                placeLocation = location.getString("address");
            }catch (JSONException jsonException){
                placeLocation = "Address not available";
            }

            categories = venue.getJSONArray("categories");
            placeType = categories.getJSONObject(0).getString("name");

            photoURL = items.getJSONObject(i).getString("venueImage");
            this.venueImageURL[i] = photoURL;
            iconURL = categories.getJSONObject(0).getJSONObject("icon").getString("prefix") + "64" +
                    categories.getJSONObject(0).getJSONObject("icon").getString("suffix");
            iconURLs.add(i,iconURL);

            //getPlacesIcons(iconURL);


            final int finalI = i;
            //Log.d("iconn", iconURL);
            final String finalPlaceName = placeName;
            final String finalPlaceType = placeType;
            final String finalPlaceLocation = placeLocation;
            final String finalNumberOfReviews = numberOfReviews;
            final String finalPlaceRating = placeRating;
            final String fplaceId = placeId;
            nmc.getPlaceImage(this.venueImageURL[i], getApplicationContext(), new NearMeController.VolleyCallback6() {
                @Override
                public void onSuccess(Bitmap result) {
                    imgBitmap[0] = result;
                    restList.add(new Places(imgBitmap[0], finalPlaceName, finalPlaceLocation, finalPlaceType, finalPlaceRating, null, finalPlaceRating, fplaceId));
                    adapter.notifyDataSetChanged();

                    ll1.setVisibility(View.GONE);
                    recyclerView1.setVisibility(View.VISIBLE);


                }
                });
        }
    }



}
