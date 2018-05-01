package com.lodestarapp.cs491.lodestar;

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
    private RecyclerView recyclerView3;

    private final int REQUEST_LOCATION = 200;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter2;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter3;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private RecyclerView.LayoutManager layoutManager3;

    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;

    private List<Places> restList = new ArrayList<>();
    private List<Places> parkList = new ArrayList<>();
    private List<Places> museumList = new ArrayList<>();

    NearMeController nmc;
    private String[] venueImageURL = new String[10];
    private ArrayList<String> iconURLs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        recyclerView1 = findViewById(R.id.near_me_recycler1);
        recyclerView1.setHasFixedSize(false);

        recyclerView2 = findViewById(R.id.near_me_recycler2);
        recyclerView2.setHasFixedSize(false);

        recyclerView3 = findViewById(R.id.near_me_recycler3);
        recyclerView3.setHasFixedSize(false);


        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);

        recyclerView1.setVisibility(View.GONE);
        recyclerView2.setVisibility(View.GONE);
        recyclerView3.setVisibility(View.GONE);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
        adapter = new NearMeAdapter(restList, "restaurant",this.getApplicationContext());
        recyclerView1.setAdapter(adapter);

        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new NearMeAdapter(parkList, "park",this.getApplicationContext());
        recyclerView2.setAdapter(adapter2);

        layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager3);
        adapter3 = new NearMeAdapter(museumList, "museum",this.getApplicationContext());
        recyclerView3.setAdapter(adapter3);

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
        int locationPermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

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
                        nmc.getNearMeInformation(getApplicationContext(),"restaurant", new NearMeController.VolleyCallback5() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    parseTheJSONObject(result,1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        nmc.getNearMeInformation(getApplicationContext(),"park", new NearMeController.VolleyCallback5() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    parseTheJSONObject(result,2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        nmc.getNearMeInformation(getApplicationContext(),"museum", new NearMeController.VolleyCallback5() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                try {
                                    parseTheJSONObject(result,3);
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
            nmc.getNearMeInformation(getApplicationContext(), "restaurant", new NearMeController.VolleyCallback5() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        parseTheJSONObject(result,1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            nmc.getNearMeInformation(getApplicationContext(), "park", new NearMeController.VolleyCallback5() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        parseTheJSONObject(result,2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            nmc.getNearMeInformation(getApplicationContext(),"museum", new NearMeController.VolleyCallback5() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        parseTheJSONObject(result,3);
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
                                nmc.getNearMeInformation(getApplicationContext(), "restaurant",new NearMeController.VolleyCallback5() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            parseTheJSONObject(result,1);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                nmc.getNearMeInformation(getApplicationContext(), "park",new NearMeController.VolleyCallback5() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            parseTheJSONObject(result,2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                nmc.getNearMeInformation(getApplicationContext(),"museum", new NearMeController.VolleyCallback5() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            parseTheJSONObject(result,3);
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

    private void parseTheJSONObject(JSONObject jo, int qtype) throws JSONException {
        //Log.d(TAG, "bbb");
        //Log.d("near me", jo.toString());

        JSONArray groups = jo.getJSONArray("groups");
        JSONArray items = groups.getJSONObject(0).getJSONArray("items");
        Log.d("near me", items.toString());

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

            if(venue.has("rating")){
                placeRating = venue.getDouble("rating") + "";
            }
            else
                placeRating = "0";
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
            iconURL = categories.getJSONObject(0).getJSONObject("icon").getString("prefix") + "bg_64" +
                    categories.getJSONObject(0).getJSONObject("icon").getString("suffix");
            iconURLs.add(iconURL);

            //getPlacesIcons(iconURL);
            final String coords = location.getDouble("lat")+ "," +location.getDouble("lng");



            final int finalI = i;
            //Log.d("iconn", iconURL);
            final String finalPlaceName = placeName;
            final String finalPlaceType = placeType;
            final String finalPlaceLocation = placeLocation;
            final String finalNumberOfReviews = numberOfReviews;
            final String finalPlaceRating = placeRating;
            final String fplaceId = placeId;
            final int fqtype = qtype;
            final String finalIconURL = iconURL;
            nmc.getPlaceImage(this.venueImageURL[i], getApplicationContext(), new NearMeController.VolleyCallback6() {
                @Override
                public void onSuccess(Bitmap result) {
                    imgBitmap[0] = result;
                    if( fqtype == 1){
                        Places pl = new Places(imgBitmap[0], finalPlaceName, finalPlaceLocation, finalPlaceType, finalPlaceRating, null, finalPlaceRating, fplaceId, coords);
                        restList.add(pl);
                        ll1.setVisibility(View.GONE);
                        recyclerView1.setVisibility(View.VISIBLE);
                        adapter.notifyItemInserted(restList.indexOf(pl));
                        getPlacesIcon(restList.indexOf(pl), finalIconURL,"restaurant");
                    }
                    if( fqtype == 2){
                        Places pl = new Places(imgBitmap[0], finalPlaceName, finalPlaceLocation, finalPlaceType, finalPlaceRating, null, finalPlaceRating, fplaceId,coords);
                        parkList.add(pl);
                        ll2.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.VISIBLE);
                        adapter2.notifyItemInserted(parkList.indexOf(pl));
                        getPlacesIcon(parkList.indexOf(pl), finalIconURL,"park");
                    }
                    if( fqtype == 3){
                        Places pl = new Places(imgBitmap[0], finalPlaceName, finalPlaceLocation, finalPlaceType, finalPlaceRating, null, finalPlaceRating, fplaceId, coords);
                        museumList.add(pl);
                        ll3.setVisibility(View.GONE);
                        recyclerView3.setVisibility(View.VISIBLE);
                        adapter3.notifyItemInserted(museumList.indexOf(pl));
                        getPlacesIcon(museumList.indexOf(pl), finalIconURL,"museum");
                    }

                }
                });
        }
    }

    private void getPlacesIcon(final int i,String iconURL, final String criter) {
        nmc.getPlaceImage(iconURL, getApplicationContext(),
                new NearMeController.VolleyCallback6() {
                @Override
                public void onSuccess(Bitmap result) {
                    if(criter.equals("restaurant")){
                        restList.get(i).setPlaceIconImage(result);
                        adapter.notifyItemChanged(i);

                    }
                    if(criter.equals("park")){
                        parkList.get(i).setPlaceIconImage(result);
                        adapter2.notifyItemChanged(i);
                    }
                    if(criter.equals("museum")){
                        museumList.get(i).setPlaceIconImage(result);
                        adapter3.notifyItemChanged(i);
                    }
                }
        });
    }



}
