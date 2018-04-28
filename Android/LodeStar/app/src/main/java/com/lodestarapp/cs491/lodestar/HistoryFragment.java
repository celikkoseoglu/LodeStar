package com.lodestarapp.cs491.lodestar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.ADDITIONAL_USER;
import com.lodestarapp.cs491.lodestar.Adapters.HistoryAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.UserPageAdapter;
import com.lodestarapp.cs491.lodestar.Controllers.FlightInfoController;
import com.lodestarapp.cs491.lodestar.Controllers.TripController;
import com.lodestarapp.cs491.lodestar.Models.HistoryInfo;
import com.lodestarapp.cs491.lodestar.Models.ImageStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ADDITIONAL_USER au;
    DatabaseReference ref;
    private ListView tripListView;
    View myView;
    ArrayAdapter<String> arrayAdapter;
    FirebaseUser user;

    ArrayList<String> arrayListOfHistory;


    ArrayList<String> flightCodeList;

    private FlightInfoController flc = new FlightInfoController();
    private TripController trc = new TripController();

    private ArrayList<HistoryInfo> historyInfos;

    private ArrayList<String> cityFroms;
    private ArrayList<String> cityTos;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HistoryFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayListOfHistory = new ArrayList<>();

        flightCodeList = new ArrayList<>();

        historyInfos = new ArrayList<>();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //   ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
        // Log.i("agam",au.username);
        ref = database.getReference();
        Toast.makeText(this.getActivity(),"ww",Toast.LENGTH_LONG).show();
        Log.i("agam", "fffff");

        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.getEmail().equals(au.getemail())) {

                        Log.i("agam", "whatever it takes" + au.gettrips());

                        if(au.gettrips() == null) {

                        }else {

                            String tmpArrayOfmine[] = au.gettrips().split("!");
                            for(int i = 0; i < tmpArrayOfmine.length; i++) {
                                arrayListOfHistory.add(tmpArrayOfmine[i]);
                                Log.i("agam",i + ":" + tmpArrayOfmine[i]);
                                for(int t = 0; t < arrayListOfHistory.size();t++) {
                                    String s = arrayListOfHistory.get(t);
                                    s = s.substring(s.indexOf(":") + 1);
                                    s = s.substring(0, s.indexOf(" From"));
                                    Log.i("agam", "ooyea :" + s);
                                    flightCodeList.add(s);

                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //   ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
        // Log.i("agam",au.username);
        /*ref = database.getReference();

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);

                    if(au.gettrips() != null)
                        Log.i("agam",au.gettrips());

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.getEmail().equals(au.getemail())) {
                      //  Toast.makeText(this,au.gettrips(),Toast.LENGTH_LONG).show();
=======
>>>>>>> a1fe68cfb90da8378d830f56f014b57b89f1b80c
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(this.flightCodeList.size() > 0){
            myView = inflater.inflate(R.layout.fragment_history, container, false);

            mRecyclerView = myView.findViewById(R.id.history_recyclerview);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new HistoryAdapter(historyInfos);
            mRecyclerView.setAdapter(mAdapter);

            sendRequest(this.flightCodeList);
        }
        else {
            myView = inflater.inflate(R.layout.activity_history_initial, container, false);
        }

        //TODO:Check the state - whether there is item in history or not from database
        //if ....
        //  View view = inflater.inflate(R.layout.fragment_history, container, false)
        //else
        //  View view = inflater.inflate(R.layout.activity_history_initial, container, false);

        // Inflate the layout for this fragment
        //initial'ı tekrardan bağla
        //myView = inflater.inflate(R.layout.activity_history_initial, container, false);



        /*TextView t1 = myView.findViewById(R.id.textview33);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                startActivity(intent);
            }
        });*/



        return myView;
    }

    public void sendRequest(ArrayList<String> flightCodeList) {
        historyInfos = new ArrayList<>();
        cityFroms = new ArrayList<>();
        cityTos = new ArrayList<>();

        int size = flightCodeList.size();

        final String[] city1 = new String[1];
        final String[] city2 = new String[1];

        for (int i = 0; i < size; i++) {
            flc.getFlightInfo(flightCodeList.get(i), getActivity(), new FlightInfoController.VolleyCallback2() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {

                        JSONObject origin = result.getJSONObject("origin");
                        JSONObject destination = result.getJSONObject("destination");

                        JSONObject filedDepartureTime = result.getJSONObject("filed_departure_time");
                        JSONObject filedArrivalTime = result.getJSONObject("filed_arrival_time");

                        String flightCode = result.getString("ident");

                        String cityFrom = origin.getString("city");
                        city1[0] = cityFrom;
                        String fromAirport = origin.getString("airport_name");
                        String fromAirportIdent = origin.getString("alternate_ident");

                        String cityTo = destination.getString("city");
                        city2[0] = cityTo;
                        String toAirport = destination.getString("airport_name");
                        String toAirportIdent = destination.getString("alternate_ident");

                        long departureTime = filedDepartureTime.getLong("localtime");
                        String departureDate = filedDepartureTime.getString("date");

                        long arrivalTime = filedArrivalTime.getLong("localtime");
                        String arrivalDate = filedArrivalTime.getString("date");

                        HistoryInfo historyInfo = new HistoryInfo(null, null, flightCode,
                                cityFrom, cityTo, fromAirport, fromAirportIdent, toAirport,
                                toAirportIdent, departureTime, arrivalTime, departureDate, arrivalDate);

                        historyInfos.add(historyInfo);
                        cityFroms.add(city1[0]);
                        cityTos.add(city2[0]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        mAdapter.notifyDataSetChanged();

        getFromAndToCityPhotos(cityFroms, cityTos);
    }

    private void getFromAndToCityPhotos(final ArrayList<String> cityFroms, final ArrayList<String> cityTos) {
        final String[] cityFromReference = new String[1];
        final String[] cityToReference = new String[1];

        int size = cityFroms.size();

        for (int i = 0; i < size; i++) {
            final int finalI = i;
            trc.getTripCity(cityFroms.get(i), getContext(), new TripController.VolleyCallback4() {
                @Override
                public void onSuccess(JSONObject result) {
                    try{
                        JSONArray x = result.getJSONArray("results");
                        JSONObject jo = x.getJSONObject(0);

                        JSONArray photos = jo.getJSONArray("photos");
                        JSONObject photo = photos.getJSONObject(0);
                        cityFromReference[0] = photo.getString("photo_reference");

                        if (ImageStorage.checkIfImageExists(cityFroms.get(finalI))) {
                            File file = ImageStorage.getImage("/" + cityFroms.get(finalI) + ".png");
                            assert file != null;
                            String p = file.getAbsolutePath();
                            Bitmap b = BitmapFactory.decodeFile(p);

                            historyInfos.get(finalI).setCityFromBitmap(b);
                            mAdapter.notifyDataSetChanged();

                        } else {
                            trc.getBackgroundImage(cityFromReference[0], 1080, getContext(), new TripController.VolleyCallback5() {
                                @Override
                                public void onSuccess(Bitmap result) {
                                    ImageStorage.saveToSdCard(result, cityFroms.get(finalI));

            //                        getView().findViewById(R.id.ll1).setVisibility(View.GONE);
            //                        getView().findViewById(R.id.flipper).setVisibility(View.VISIBLE);

                                    historyInfos.get(finalI).setCityFromBitmap(result);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });

            trc.getTripCity(cityTos.get(i), getContext(), new TripController.VolleyCallback4() {
                @Override
                public void onSuccess(JSONObject result) {
                    try{
                        JSONArray x = result.getJSONArray("results");
                        JSONObject jo = x.getJSONObject(0);

                        JSONArray photos = jo.getJSONArray("photos");
                        JSONObject photo = photos.getJSONObject(0);
                        cityToReference[0] = photo.getString("photo_reference");

                        if (ImageStorage.checkIfImageExists(cityTos.get(finalI))) {
                            File file = ImageStorage.getImage("/" + cityTos.get(finalI) + ".png");
                            assert file != null;
                            String p = file.getAbsolutePath();
                            Bitmap b = BitmapFactory.decodeFile(p);

                            historyInfos.get(finalI).setCityToBitmap(b);
                            mAdapter.notifyDataSetChanged();

                        } else {
                            trc.getBackgroundImage(cityToReference[0], 1080, getContext(), new TripController.VolleyCallback5() {
                                @Override
                                public void onSuccess(Bitmap result) {
                                    ImageStorage.saveToSdCard(result, cityTos.get(finalI));

                                    historyInfos.get(finalI).setCityToBitmap(result);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}