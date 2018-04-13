package com.lodestarapp.cs491.lodestar.VR;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lodestarapp.cs491.lodestar.Adapters.NearMeAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.NearMeController;
import com.lodestarapp.cs491.lodestar.Controllers.PlacesToSeeController;
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
    private final int REQUEST_LOCATION = 200;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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

        boolean locationPermissionGiven = checkForLocationPermission();

        if (locationPermissionGiven) {
            getLastKnowLocation(false, null);
        } else {
            requestPermissionForLocation();
            getLastKnowLocation(false, null);
        }
    }

    public void tripStart(View view) {
        finish();
    }

    private boolean checkForLocationPermission() {
        int locationPermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        return locationPermissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForLocation() {
        //Log.d(TAG, "request");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    private void getLastKnowLocation(boolean which, Location location) {
        if (!which) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("near me", "not given");
                return;
            }
            Log.d("near me", "given");
            final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            final Location[] myLocation = new Location[1];
            final boolean[] done = {false};
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        done[0] = true;
                        myLocation[0] = locationResult.getResult();
                        nmc = new NearMeController("",true,myLocation[0]);
                        nmc.getNearMeInformation(getApplicationContext(), new NearMeController.VolleyCallback5() {
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
                    //complete else
                }
            });

        }
        else{
            final Location[] myLocation = new Location[1];
            myLocation[0] = location;
            nmc = new NearMeController("",true,myLocation[0]);
            nmc.getNearMeInformation(getApplicationContext(), new NearMeController.VolleyCallback5() {
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
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                Log.d("near me", "requesting permission");
                if (grantResults.length > 0) {
                    //https://developers.google.com/maps/documentation/android-api/current-place-tutorial#get-the-location-of-the-android-device-and-position-the-map
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    final Location[] myLocation = new Location[1];
                    locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                nmc = new NearMeController("",true,null);
                                nmc.getNearMeInformation(getApplicationContext(), new NearMeController.VolleyCallback5() {
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
                            //complete else
                        }
                    });
                }
                else{
                    nmc = new NearMeController("", false, null);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
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
