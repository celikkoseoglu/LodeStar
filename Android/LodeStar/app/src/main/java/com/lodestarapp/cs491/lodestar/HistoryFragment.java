package com.lodestarapp.cs491.lodestar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


        //Write History Activity Here

        // tripListView = myView.findViewById(R.id.olalala);




        super.onCreate(savedInstanceState);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
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
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        
//
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ADDITIONAL_USER au = dataSnapshot.getValue(ADDITIONAL_USER.class);
//                Log.i("agam",au.email);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO:Check the state - whether there is item in history or not from database
        //if ....
        //  View view = inflater.inflate(R.layout.fragment_history, container, false)
        //else
        //  View view = inflater.inflate(R.layout.activity_history_initial, container, false);

        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.activity_history_initial, container, false);

        TextView t1 = myView.findViewById(R.id.textview33);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                startActivity(intent);
            }
        });
        return myView;
    }



}