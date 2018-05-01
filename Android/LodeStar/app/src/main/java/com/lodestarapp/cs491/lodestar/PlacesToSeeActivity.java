package com.lodestarapp.cs491.lodestar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lodestarapp.cs491.lodestar.Adapters.PlacesToSeeAdapter;
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

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LinearLayout ll1;

    private List<Places> placesList = new ArrayList<>();

    private String[] venueImageURL = new String[5];
    private ArrayList<String> photoReferences = new ArrayList<>();
    private ArrayList<String> iconURL = new ArrayList<>();

    private static final String TAG = "placesToSeeMessage";
    LatLngBounds.Builder builder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_see);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        //Reference: https://developer.android.com/guide/topics/location/strategies.html
        //locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        /*locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLastKnowLocation(true, location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };*/

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
          //  return;
        //}
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        recyclerView = findViewById(R.id.places_to_see_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlacesToSeeAdapter(placesList, this.getApplicationContext());
        recyclerView.setAdapter(adapter);

        ll1 = findViewById(R.id.ll1);
        recyclerView.setVisibility(View.GONE);
        builder= new LatLngBounds.Builder();


        Log.d(TAG, "lol");

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
            getLastKnowLocation(false, null);
        } else {
            Log.d(TAG, "ARE YOU HERE");
            requestPermissionForLocation();
            getLastKnowLocation(false, null);
        }
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
                Log.d(TAG, "not given");
                return;
            }
            Log.d(TAG, "given");
            final Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            final Location[] myLocation = new Location[1];
            final boolean[] done = {false};
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        done[0] = true;
                        myLocation[0] = locationResult.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                myLocation[0].getLatitude(), myLocation[0].getLongitude()), 14));

                        googleMap.addMarker((new MarkerOptions().
                                position(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()))
                                .title("You are Here")));
                        builder.include(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()));

                        placesToSeeController = new PlacesToSeeController(true, myLocation[0]);
                        placesToSeeController.getPlacesToSeeInformation(placesToSeeController.getRequestFromUrl(),
                                getApplicationContext(), new LodeStarServerCallback() {
                                    @Override
                                    public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                                        Log.d(TAG, "cccc");
                                        try {
                                            parseTheJSONObject(jsonArray);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onPlaceImageSuccess(Bitmap bitmap) {
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

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                    myLocation[0].getLatitude(), myLocation[0].getLongitude()), 14));

            googleMap.addMarker((new MarkerOptions().
                    position(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()))
                    .title("You are Here")));
            builder.include(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()));

            placesToSeeController = new PlacesToSeeController(true, myLocation[0]);
            placesToSeeController.getPlacesToSeeInformation(placesToSeeController.getRequestFromUrl(),
                    getApplicationContext(), new LodeStarServerCallback() {
                        @Override
                        public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                            Log.d(TAG, "cccc");
                            try {
                                parseTheJSONObject(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onPlaceImageSuccess(Bitmap bitmap) {
                        }
                    });
        }
    }

    private boolean checkForLocationPermission() {
        int locationPermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        return locationPermissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForLocation() {
        Log.d(TAG, "request");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                Log.d(TAG, "requesting permission");
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
                                myLocation[0] = locationResult.getResult();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                        myLocation[0].getLatitude(), myLocation[0].getLongitude()), 14));

                                googleMap.addMarker((new MarkerOptions().
                                        position(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()))
                                        .title("You are Here")));
                                builder.include(new LatLng(myLocation[0].getLatitude(), myLocation[0].getLongitude()));

                                placesToSeeController = new PlacesToSeeController(true, myLocation[0]);
                                placesToSeeController.getPlacesToSeeInformation(placesToSeeController.getRequestFromUrl(),
                                        getApplicationContext(), new LodeStarServerCallback() {
                                            @Override
                                            public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {
                                                Log.d(TAG, "aaaa");
                                                try {
                                                    parseTheJSONObject(jsonArray);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onPlaceImageSuccess(Bitmap bitmap) {}
                                        });
                            }
                            //complete else
                        }
                    });
                }
                else{
                    placesToSeeController = new PlacesToSeeController(false, null);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void parseTheJSONObject(JSONArray jsonArray) throws JSONException {
        Log.d(TAG, "bbb");
        Log.d(TAG, jsonArray.toString());

        String placeName;
        String placeAddress;
        String placeRating;
        String placeType;
        String placeId;

        JSONObject place;

        JSONObject geometry;
        JSONObject location;
        JSONObject viewport;

        JSONArray photos;
        JSONArray types;

        LatLng placeLatLng;

        //VR Photo???
        LatLng placeViewPort;

        String photoAttribution;

        for (int i = 0; i < 5 ; i++) {
            place = jsonArray.getJSONObject(i);

            geometry = place.getJSONObject("geometry");
            location = geometry.getJSONObject("location");
            viewport = geometry.getJSONObject("viewport");

            photos = place.getJSONArray("photos");
            types = place.getJSONArray("types");

            placeName = place.getString("name");
            placeAddress = place.getString("formatted_address");
            placeRating = Double.parseDouble(place.getString("rating")) * 2 + "";
            placeType = types.getString(0);
            placeId = place.getString("place_id");

            placeLatLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

            iconURL.add(place.getString("icon"));

            photoReferences.add(photos.getJSONObject(0).getString("photo_reference"));

            photoAttribution = photos.getJSONObject(0)
                    .getJSONArray("html_attributions").toString();

            //Add marker to google maps
            googleMap.addMarker(new MarkerOptions()
                    .position(placeLatLng)
                    .title(placeName));
            builder.include(placeLatLng);
            String coords = location.getDouble("lat")+ "," +location.getDouble("lng");
            placesList.add(new Places(null, placeName, placeAddress, placeType,
                    placeRating, null, placeRating, placeId, coords));

        }
        LatLngBounds bounds= builder.build();

        int padding = 100;
        CameraUpdate c = CameraUpdateFactory.newLatLngBounds(bounds,padding);
        googleMap.animateCamera(c);

        adapter.notifyDataSetChanged();

        getPlacesPhotos(photoReferences);
        getPlacesIcons(iconURL);

        /*

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
        String placeRating;

        LatLng placeLatLng;

        String numberOfReviews;

        String photoURL;

        final Bitmap[] imgBitmap = new Bitmap[1];

        for (int i = 0; i < items.length(); i++){
            venue = items.getJSONObject(i).getJSONObject("venue");
            placeName = venue.getString("name");

            numberOfReviews = items.getJSONObject(i).getString("reviewCount");

            location = venue.getJSONObject("location");
            Log.d(TAG, location.toString());

            placeRating = venue.getString("rating");

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

            photoURL = items.getJSONObject(i).getString("venueImage");
            this.venueImageURL[i] = photoURL;


            //for (int j = 0; j < 5; j++) {
                final int finalI = i;
                Log.d(TAG, this.venueImageURL[i]);
            final String finalPlaceName = placeName;
            final String finalPlaceType = placeType;
            final String finalPlaceLocation = placeLocation;
            final String finalNumberOfReviews = numberOfReviews;
            final String finalPlaceRating = placeRating;
            placesToSeeController.getVenueImage(this.venueImageURL[i], getApplicationContext(), new LodeStarServerCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {}

                    @Override
                    public void onPlaceImageSuccess(Bitmap bitmap) {
                        imgBitmap[0] = bitmap;
                        placesList.add(new Places(imgBitmap[0], finalPlaceName, finalPlaceType, finalPlaceLocation, finalNumberOfReviews, finalPlaceRating));
                        adapter.notifyDataSetChanged();
                        //placesList.get(finalI).setPlaceImage(bitmap);
                        //adapter.notifyDataSetChanged();
                    }
                });
            //}

            //placesList.add(new Places(imgBitmap[0], placeName, placeType, placeLocation, numberOfReviews, placeRating));

        }

        //getPlaceImage();


*/
    }

    private void getPlacesIcons(ArrayList<String> iconURL) {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            Log.d(TAG, this.iconURL.get(i));
            placesToSeeController.getPlaceImage(iconURL.get(i), getApplicationContext(),
                    new LodeStarServerCallback() {
                        @Override
                        public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {}

                        @Override
                        public void onPlaceImageSuccess(Bitmap bitmap) {
                            placesList.get(finalI).setPlaceIconImage(bitmap);
                            adapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void getPlacesPhotos(ArrayList<String> photoReferences) {
        StringBuilder requestFromURL = new StringBuilder("");

        for (int i = 0; i < 5; i++){
            final int finalI = i;
            //Log.d(TAG, "Photo");
            //Log.d(TAG, this.photoReferences.get(i));
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            int px = (int) (130 * (metrics.densityDpi / 160f));

            requestFromURL.append("https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxheight="+px+"&photoreference=");

            requestFromURL.append(photoReferences.get(i));
            requestFromURL.append("&key=" + "AIzaSyAKnThPPshmgffk3DNPNkXd2glEQaH1Rlw");

            placesToSeeController.getPlaceImage(requestFromURL.toString(), getApplicationContext(),
                    new LodeStarServerCallback() {
                        @Override
                        public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {}

                        @Override
                        public void onPlaceImageSuccess(Bitmap bitmap) {
                            placesList.get(finalI).setPlaceImage(bitmap);
                            adapter.notifyDataSetChanged();
                            ll1.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    });

            requestFromURL.setLength(0);
        }
    }

    public void tripStart(View view) {
        finish();
    }

    /*private void getPlaceImage() {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            Log.d(TAG, this.venueImageURL[i]);
            placesToSeeController.getVenueImage(this.venueImageURL[i], getApplicationContext(), new LodeStarServerCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray, JSONObject jsonObject) {}

                @Override
                public void onPlaceImageSuccess(Bitmap bitmap) {
                    placesList.get(finalI).setPlaceImage(bitmap);
                    //adapter.notifyDataSetChanged();
                }
            });
        }
    }*/
}
