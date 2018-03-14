package com.lodestarapp.cs491.lodestar;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.WeatherInformationAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.PlacesToSeeController;
import com.lodestarapp.cs491.lodestar.Interfaces.LodeStarServerCallback;
import com.lodestarapp.cs491.lodestar.Models.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesToSeeActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION = 200;
    private GoogleMap googleMap;
    private PlacesToSeeController placesToSeeController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Places> placesList = new ArrayList<>();

    private static final String TAG = "placesToSeeMessage";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        recyclerView = findViewById(R.id.places_to_see_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlacesToSeeAdapter(placesList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //googleMap.addMarker(new MarkerOptions()
        //    .position(new LatLng(0,0))
        //    .title("Marker"));

        boolean locationPermissionGiven = checkForLocationPermission();

        if (locationPermissionGiven) {
            this.googleMap.setMyLocationEnabled(true);
            getLastKnowLocation();
        } else {
            requestPermissionForLocation();
        }
    }

    private void getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
        final Location[] myLocation = new Location[1];
        final boolean[] done = {false};
        locationResult.addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    done[0] = true;
                    myLocation[0] = locationResult.getResult();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            myLocation[0].getLatitude(), myLocation[0].getLongitude()), 14));

                    googleMap.addMarker((new MarkerOptions().
                            position(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()))
                            .title("Marker")));

                    placesToSeeController = new PlacesToSeeController("", true, myLocation[0]);
                    placesToSeeController.getPlacesToSeeInformation(placesToSeeController.getRequestFromUrl(),
                            getApplicationContext(), new LodeStarServerCallback() {
                                @Override
                                public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                                    Log.d(TAG, "cccc");
                                    try {
                                        parseTheJSONObject(jsonObject);
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

    private boolean checkForLocationPermission() {
        int locationPermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return locationPermissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForLocation() {
        ActivityCompat.requestPermissions(PlacesToSeeActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0) {
                    //https://developers.google.com/maps/documentation/android-api/current-place-tutorial#get-the-location-of-the-android-device-and-position-the-map
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    final Location[] myLocation = new Location[1];
                    locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                myLocation[0] = locationResult.getResult();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                        myLocation[0].getLatitude(), myLocation[0].getLongitude()), 14));

                                placesToSeeController = new PlacesToSeeController("", true, myLocation[0]);
                                placesToSeeController.getPlacesToSeeInformation(placesToSeeController.getRequestFromUrl(),
                                        getApplicationContext(), new LodeStarServerCallback() {
                                            @Override
                                            public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                                                Log.d(TAG, "aaaa");
                                                try {
                                                    parseTheJSONObject(jsonObject);
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
                    placesToSeeController = new PlacesToSeeController("", false, null);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void parseTheJSONObject(JSONObject jsonObject) throws JSONException {
        //Log.d(TAG, "bbb");
        Log.d(TAG, jsonObject.toString());


        JSONArray groups = jsonObject.getJSONArray("groups");
        JSONArray items = groups.getJSONObject(0).getJSONArray("items");
        //JSONArray items = groups.getJSONArray("items")

        //Log.d(TAG, items.toString());


        JSONObject venue; //name
        JSONObject location; //location
        JSONArray categories; // type

        String placeName;
        String placeLocation;
        String placeType;

        LatLng placeLatLng;

        for (int i = 0; i < items.length(); i++){
            venue = items.getJSONObject(i).getJSONObject("venue");
            placeName = venue.getString("name");

            location = venue.getJSONObject("location");
            Log.d(TAG, location.toString());

            placeLatLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

            //Add marker to google maps
            googleMap.addMarker(new MarkerOptions()
                .position(placeLatLng)
                    .title(placeName));

            try{
                placeLocation = location.getString("address");
            }catch (JSONException jsonException){
                placeLocation = "Address not available";
            }

            categories = venue.getJSONArray("categories");
            placeType = categories.getJSONObject(0).getString("name");

            placesList.add(new Places(placeName, placeType, placeLocation, ""));

        }
        adapter.notifyDataSetChanged();
    }
}
