package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.ADDITIONAL_USER;
import com.lodestarapp.cs491.lodestar.Adapters.HistoryAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.FlightInfoController;
import com.lodestarapp.cs491.lodestar.Controllers.TripController;
import com.lodestarapp.cs491.lodestar.Models.HistoryInfo;
import com.lodestarapp.cs491.lodestar.Models.ImageStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class HistoryFragment extends Fragment {
    private ADDITIONAL_USER au;
    DatabaseReference ref;
    private View myView;
    private FirebaseUser user;

    private ArrayList<String> arrayListOfHistory;
    private ArrayList<String> flightCodeList;

    private FlightInfoController flc = new FlightInfoController();
    private TripController trc = new TripController();

    private ArrayList<HistoryInfo> historyInfos = new ArrayList<>();

    private ArrayList<String> cityFroms;
    private ArrayList<String> cityTos;

    private ArrayList<String> cityFromsComplete;
    private ArrayList<String> cityTosComplete;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    volatile int check = 0;

    public ArrayList<HistoryInfo> getHistoryInfos() {
        return this.historyInfos;
    }

    public HistoryFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        arrayListOfHistory = new ArrayList<>();
        flightCodeList = new ArrayList<>();
        cityFroms = new ArrayList<>();
        cityTos = new ArrayList<>();
        cityFromsComplete = new ArrayList<>();
        cityTosComplete = new ArrayList<>();
        historyInfos = new ArrayList<>();

        myView = inflater.inflate(R.layout.fragment_history, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref = database.getReference();

        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.getEmail().equals(au.getemail())) {
                        if (au.gettrips() == null) {

                        } else {
                            String tmpArrayOfmine[] = au.gettrips().split("!");

                            arrayListOfHistory.addAll(Arrays.asList(tmpArrayOfmine));

                            for (int t = 0; t < arrayListOfHistory.size(); t++) {
                                String s = arrayListOfHistory.get(t);
                                s = s.substring(s.indexOf(":") + 1);
                                s = s.substring(0, s.indexOf(" From"));
                                flightCodeList.add(s.substring(1));
                            }
                            for (int e = 0; e < arrayListOfHistory.size(); e++) {
                                String s = arrayListOfHistory.get(e);
                                s = s.substring(s.indexOf("From: ") + 6);
                                s = s.substring(0, s.indexOf(" To:"));
                                cityFroms.add(s);
                            }
                            for (int e = 0; e < arrayListOfHistory.size(); e++) {
                                String s = arrayListOfHistory.get(e);
                                s = s.substring(s.indexOf("To: ") + 4);
                                s = s.substring(0, s.length());
                                cityTos.add(s);
                            }
                        }
                    }
                }
                if (flightCodeList.size() > 0) {
                    /*myView = inflater.inflate(R.layout.fragment_history, container, false);

                    mRecyclerView = myView.findViewById(R.id.history_recyclerview);
                    mRecyclerView.setHasFixedSize(true);

                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new HistoryAdapter(historyInfos);
                    mRecyclerView.setAdapter(mAdapter);


                    Log.d("history", "2222");*/



                } else {
                    myView = inflater.inflate(R.layout.activity_history_initial, container, false);
                    Log.d("history", "1111");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.d("history", "size2: " + flightCodeList.size());
        //mAdapter.notifyDataSetChanged();

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();

        arrayListOfHistory = new ArrayList<>();
        flightCodeList = new ArrayList<>();
        cityFroms = new ArrayList<>();
        cityTos = new ArrayList<>();
        cityFromsComplete = new ArrayList<>();
        cityTosComplete = new ArrayList<>();
        historyInfos = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        ref = database.getReference();

        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.getEmail().equals(au.getemail())) {
                        if (au.gettrips() == null) {

                        } else {
                            String tmpArrayOfmine[] = au.gettrips().split("!");

                            arrayListOfHistory.addAll(Arrays.asList(tmpArrayOfmine));

                            for (int t = 0; t < arrayListOfHistory.size(); t++) {
                                String s = arrayListOfHistory.get(t);
                                s = s.substring(s.indexOf(":") + 1);
                                s = s.substring(0, s.indexOf(" From"));
                                flightCodeList.add(s.substring(1));
                            }
                            for (int e = 0; e < arrayListOfHistory.size(); e++) {
                                String s = arrayListOfHistory.get(e);
                                s = s.substring(s.indexOf("From: ") + 6);
                                s = s.substring(0, s.indexOf(" To:"));
                                cityFroms.add(s);
                            }
                            for (int e = 0; e < arrayListOfHistory.size(); e++) {
                                String s = arrayListOfHistory.get(e);
                                s = s.substring(s.indexOf("To: ") + 4);
                                s = s.substring(0, s.length());
                                cityTos.add(s);
                            }
                        }
                    }
                }
                if (flightCodeList.size() > 0) {

                    mRecyclerView = myView.findViewById(R.id.history_recyclerview);
                    mRecyclerView.setHasFixedSize(true);

                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new HistoryAdapter(historyInfos);
                    mRecyclerView.setAdapter(mAdapter);

                    sendRequest(flightCodeList);

                    Log.d("history", "2222");
                    
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendRequest(ArrayList<String> flightCodeList) {
        //cityFromsComplete = new ArrayList<>();
        //cityTosComplete = new ArrayList<>();
        //this.historyInfos = new ArrayList<>();

        Log.d("history" , "cityFroms size: " + cityFroms.size());

        int size = flightCodeList.size();

        for (int i = 0; i < size; i++) {
            flc.getFlightInfo(cityFroms.get(i), cityTos.get(i), flightCodeList.get(i), getActivity(), new FlightInfoController.VolleyCallback2() {
                @Override
                public void onSuccess(JSONObject result) {
                    parseTheJSONObject(result);
                }
            });
        }
    }

    private void parseTheJSONObject(JSONObject result) {
        try {
            JSONObject origin = result.getJSONObject("origin");
            JSONObject destination = result.getJSONObject("destination");

            //JSONObject filedDepartureTime = result.getJSONObject("filed_departure_time");
            //JSONObject filedArrivalTime = result.getJSONObject("filed_arrival_time");

            String flightCode = result.getString("ident");

            String cityFrom = origin.getString("city");
            String fromAirport = origin.getString("airport_name");
            String fromAirportIdent = origin.getString("alternate_ident");

            String cityTo = destination.getString("city");
            String toAirport = destination.getString("airport_name");
            String toAirportIdent = destination.getString("alternate_ident");

            //long departureTime = filedDepartureTime.getLong("localtime");
            long departureTime = 0;
            //String departureDate = filedDepartureTime.getString("date");
            String departureDate = "";

            //long arrivalTime = filedArrivalTime.getLong("localtime");
            long arrivalTime;
            arrivalTime = 0;
            //String arrivalDate = filedArrivalTime.getString("date");
            String arrivalDate = "";

            HistoryInfo historyInfo = new HistoryInfo(null, null, flightCode,
                    cityFrom, cityTo, fromAirport, fromAirportIdent, toAirport,
                    toAirportIdent, departureTime, arrivalTime, departureDate, arrivalDate);

            this.historyInfos.add(historyInfo);
            cityFromsComplete.add(cityFrom);
            cityTosComplete.add(cityTo);

        }catch (JSONException e){
            e.printStackTrace();
        }

        getFromAndToCityPhotos(cityFromsComplete, cityTosComplete);
        //mAdapter.notifyDataSetChanged();
        //Log.d("history", "size: " + historyInfos.size());


        //for (int i = 0; i < this.historyInfos.size(); i++) {
        //    mAdapter.notifyItemInserted(i);
        //}
    }

    private void getFromAndToCityPhotos(final ArrayList<String> from, final ArrayList<String> to) {

        int size = from.size();

        for (int i = 0; i < size; i++) {
            final int finalI = i;
            if (ImageStorage.checkIfImageExists(from.get(finalI))) {
                File file = ImageStorage.getImage("/" + from.get(finalI) + ".png");
                assert file != null;
                String p = file.getAbsolutePath();
                Bitmap b = BitmapFactory.decodeFile(p);
                getHistoryInfos().get(finalI).setCityFromBitmap(b);
                //mAdapter.notifyDataSetChanged();


            } else {
                trc.getTripCity(from.get(i), getContext(), new TripController.VolleyCallback4() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        parseTheTripInformation(result, from.get(finalI), finalI, 0);
                        //mAdapter.notifyDataSetChanged();
                    }
                });
            }

            if (ImageStorage.checkIfImageExists(to.get(finalI))) {
                File file = ImageStorage.getImage("/" + to.get(finalI) + ".png");
                assert file != null;
                String p = file.getAbsolutePath();
                Bitmap b = BitmapFactory.decodeFile(p);
                getHistoryInfos().get(finalI).setCityToBitmap(b);
                //mAdapter.notifyDataSetChanged();
            }

            else{
                trc.getTripCity(to.get(i), getContext(), new TripController.VolleyCallback4() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        parseTheTripInformation(result, to.get(finalI), finalI, 1);
                        //mAdapter.notifyDataSetChanged();
                    }
                });
            }

        }
        mAdapter.notifyDataSetChanged();

        //mRecyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
        Log.d("history" , "here?");
    }

    private void parseTheTripInformation(JSONObject result, final String city, final int index, final int which) {
        try {
            JSONArray x = result.getJSONArray("results");
            JSONObject jo = x.getJSONObject(0);

            JSONArray photos = jo.getJSONArray("photos");
            JSONObject photo = photos.getJSONObject(0);
            String cityReference = photo.getString("photo_reference");


            trc.getBackgroundImage(cityReference, 1080, getContext(), new TripController.VolleyCallback5() {
                    @Override
                    public void onSuccess(Bitmap result) {
                        ImageStorage.saveToSdCard(result, city);

                        if(which == 0)
                            getHistoryInfos().get(index).setCityFromBitmap(result);
                        else
                            getHistoryInfos().get(index).setCityToBitmap(result);
                    }});


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}