package com.lodestarapp.cs491.lodestar;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private ArrayList<String> cityFromsComplete;
    private ArrayList<String> cityTosComplete;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<HistoryInfo> getHistoryInfos() {
        return this.historyInfos;
    }

    public ArrayList<String> getCityFromsComplete() {
        return this.cityFromsComplete;
    }

    public ArrayList<String> getCityTosComplete() {
        return this.cityTosComplete;
    }


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
        cityFroms = new ArrayList<>();
        cityTos = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

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
                            String fromArray[] = tmpArrayOfmine;

                            for (int i = 0; i < tmpArrayOfmine.length; i++) {
                                arrayListOfHistory.add(tmpArrayOfmine[i]);
                            }

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
                    myView = inflater.inflate(R.layout.fragment_history, container, false);

                    mRecyclerView = myView.findViewById(R.id.history_recyclerview);
                    mRecyclerView.setHasFixedSize(true);

                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new HistoryAdapter(historyInfos);
                    mRecyclerView.setAdapter(mAdapter);

                    sendRequest(flightCodeList);
                } else {
                    myView = inflater.inflate(R.layout.activity_history_initial, container, false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return myView;
    }

    public void sendRequest(ArrayList<String> flightCodeList) {
        cityFromsComplete = new ArrayList<>();
        cityTosComplete = new ArrayList<>();

        int size = flightCodeList.size();

        for (int i = 0; i < size; i++) {
            final int finalI = i;
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

            JSONObject filedDepartureTime = result.getJSONObject("filed_departure_time");
            JSONObject filedArrivalTime = result.getJSONObject("filed_arrival_time");

            String flightCode = result.getString("ident");

            String cityFrom = origin.getString("city");
            String fromAirport = origin.getString("airport_name");
            String fromAirportIdent = origin.getString("alternate_ident");

            String cityTo = destination.getString("city");
            String toAirport = destination.getString("airport_name");
            String toAirportIdent = destination.getString("alternate_ident");

            long departureTime = filedDepartureTime.getLong("localtime");
            departureTime = 0;
            String departureDate = filedDepartureTime.getString("date");

            long arrivalTime = filedArrivalTime.getLong("localtime");
            arrivalTime = 0;
            String arrivalDate = filedArrivalTime.getString("date");

            HistoryInfo historyInfo = new HistoryInfo(null, null, flightCode,
                    cityFrom, cityTo, fromAirport, fromAirportIdent, toAirport,
                    toAirportIdent, departureTime, arrivalTime, departureDate, arrivalDate);

            this.historyInfos.add(historyInfo);
            cityFromsComplete.add(cityFrom);
            cityTosComplete.add(cityTo);


        }catch (JSONException e){
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
        getFromAndToCityPhotos(cityFromsComplete, cityTosComplete);
    }

    private void getFromAndToCityPhotos(final ArrayList<String> from, final ArrayList<String> to) {

        int size = from.size();

        for (int i = 0; i < size; i++) {
            final int finalI = i;
            trc.getTripCity(from.get(i), getContext(), new TripController.VolleyCallback4() {
                @Override
                public void onSuccess(JSONObject result) {
                    parseTheTripInformation(result, from.get(finalI), finalI, 0);
                    mAdapter.notifyDataSetChanged();
                }
            });

            trc.getTripCity(to.get(i), getContext(), new TripController.VolleyCallback4() {
                @Override
                public void onSuccess(JSONObject result) {
                    parseTheTripInformation(result, to.get(finalI), finalI, 1);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void parseTheTripInformation(JSONObject result, final String city, final int index, final int which) {
        try {
            JSONArray x = result.getJSONArray("results");
            JSONObject jo = x.getJSONObject(0);

            JSONArray photos = jo.getJSONArray("photos");
            JSONObject photo = photos.getJSONObject(0);
            String cityReference = photo.getString("photo_reference");

            if (ImageStorage.checkIfImageExists(city)) {
                File file = ImageStorage.getImage("/" + city + ".png");
                assert file != null;
                String p = file.getAbsolutePath();
                Bitmap b = BitmapFactory.decodeFile(p);

                if(which == 0)
                    getHistoryInfos().get(index).setCityFromBitmap(b);
                else
                    getHistoryInfos().get(index).setCityToBitmap(b);

            } else {
                trc.getBackgroundImage(cityReference, 1080, getContext(), new TripController.VolleyCallback5() {
                    @Override
                    public void onSuccess(Bitmap result) {
                        ImageStorage.saveToSdCard(result, city);

                        if(which == 0)
                            getHistoryInfos().get(index).setCityFromBitmap(result);
                        else
                            getHistoryInfos().get(index).setCityToBitmap(result);
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}