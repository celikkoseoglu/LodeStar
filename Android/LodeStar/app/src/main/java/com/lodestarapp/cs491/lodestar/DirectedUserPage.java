package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.ADDITIONAL_USER;
import com.lodestarapp.cs491.lodestar.Adapters.DirectedUserPageAdapter;
import com.lodestarapp.cs491.lodestar.Adapters.UserPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class DirectedUserPage extends AppCompatActivity {

    String myEmail;


    //Junk Declerations
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;
    String currentUserName;
    TextView tw,tripLogs,lastTrip;
    private TextView tripCounr;
    private EditText posts;
    FirebaseUser user,userMe;
    public FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private ArrayList<String> noteArrayList;

    ADDITIONAL_USER au;
    ADDITIONAL_USER au2;
    DatabaseReference ref;

    private List<String> userInfoWithPosts = new ArrayList<>();
    private List<String> realName = new ArrayList<>();

    private final int IMAGE = 1;
    private View view;


    //end of Junk Declarations


    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_directed_user_page);
        myEmail = "";
        setup(); //Gets the bundle
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        noteArrayList = new ArrayList<String>();

        //Do everything here
        tw = findViewById(R.id.me_realName1);
        tripLogs = findViewById(R.id.me_trip_logs_count1);
        tripCounr = findViewById(R.id.me_trip_count1);
        lastTrip = findViewById(R.id.me_last_trip_city1);

        mRecyclerView = findViewById(R.id.my_user_page_recycler_view1);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DirectedUserPageAdapter(realName, userInfoWithPosts);
        mRecyclerView.setAdapter(mAdapter);


        final FirebaseDatabase databaseg = FirebaseDatabase.getInstance();
        ref = databaseg.getReference();

        ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au = childSnapshot.getValue(ADDITIONAL_USER.class);
                    if(au.getemail() != null && au.getemail().equals(myEmail)) {
                        //  Toast.makeText(this,au.gettrips(),Toast.LENGTH_LONG).show();
                        tw.setText(au.getusername());
                        realName.add(au.getusername());

                        if(au.gettrips() != null) {
                            tripCounr.setText(au.gettrips().split("!").length + "");
                        } else {
                            tripCounr.setText("0");
                        }
                        if(au.gettrips() != null) {
                            String str = au.gettrips();
                            String myTMPARR[];

                            if(str.contains("To: ")) {
                                myTMPARR = str.split("To: ");
                                // Log.i("agam","bune: " + myTMPARR[1]);
                                lastTrip.setText(myTMPARR[1].substring(0,3));
                            }
                            else {
                                lastTrip.setText("N/A");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //userInfoWithPosts = new ArrayList<>();
        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    au2 = childSnapshot.getValue(ADDITIONAL_USER.class);


                   // user = FirebaseAuth.getInstance().getCurrentUser();

                    if(myEmail.equals(au2.getemail())) {
                        String toBeParsed = au2.getposts();

                        if(au2.getposts() != null) {
                            Log.i("agam","en kucuklere");
                            String tmpArray[] = toBeParsed.split("&&&");
                            tripLogs.setText(tmpArray.length + "");
                            for(int i = 0; i < tmpArray.length;i++) {
                                userInfoWithPosts.add(tmpArray[i]);
                                Log.i("agam","bariscim: " + userInfoWithPosts.get(i));
                            }
                        }

                    }
                }


                //Barıs burada userInfoWithPosts Arraylistini databaseden doldurdum, frontenede bu arraylisti kullanarak başlayabilirsin. Thnx efe kankan :)
                mAdapter.notifyDataSetChanged();

                //UserPage.this.onCreate(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void setup() {
        Intent intent = getIntent();
        Bundle myBundle;
        myBundle = intent.getExtras();
        myEmail = myBundle.getString("emailpass");
    }




}
