package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.User;

import java.util.ArrayList;
//This class uses some of codes of the tutorial on https://www.codeproject.com/Articles/1170499/Firebase-Realtime-Database-By-Example-with-Android
public class SearchUserActivity extends AppCompatActivity {
    ListView userListView;

    DatabaseReference dref;
    DatabaseReference dref2;
    FirebaseUser userMe;
    private DatabaseReference mDatabase;
     ArrayList<String> userList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    private DatabaseReference journalCloudEndPoint;
    //private Firebase fbase;

    private String[] lolol =
            {"djdoj","dhıjpş","cdxs"};

    private String[] tmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("aga","----------------------------celtics----------------------------");
      //  fbase = new Firebase("https://fir-lodestar.firebaseio.com/users");
        Log.i("aga","----------------------------geldim abiii----------------------------");

        //Toast.makeText(this, "Time",Toast.LENGTH_LONG).show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        journalCloudEndPoint = mDatabase.child("users");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userListView = findViewById(R.id.myListe);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> value;
        if (extras != null) {
            value = extras.getStringArrayList("key");
            //The key argument here must match that used in the other activity
        }
        else {
            value = null;
        }

        //retrieveDBValues();

        //tmp = new String[userList.size()];

        Toast.makeText(this,""+  "forever" + value,Toast.LENGTH_LONG).show();

//        for (int i = 0; i < userList.size(); i++) {
//            tmp[i] = userList.get(i);
//            Toast.makeText(this, "Burda2",Toast.LENGTH_LONG).show();
//            Log.i("a","sup" + tmp[i]);
//        }
        Log.i("aga","zaazaaa");
        retrieveDBValues();

    }


    private void retrieveDBValues() {

//        Toast.makeText(this, "U here",Toast.LENGTH_LONG).show();
//        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
//                    for (com.google.firebase.database.DataSnapshot child2 : child.getChildren()) {
//                        userList.add("Oh yea:  " + child2.child("e-mail").toString());
//                        Log.i("aga","Oh yea2222:  " + child2.child("e-mail").toString());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
//        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//              // list.add("")
//                userList.add("" + dataSnapshot.child("users").getChildrenCount() + 1);
//                Log.i("lol",userList.get(0));
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        journalCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    Log.i("aga","-------ooo yeaaaa--------" + noteSnapshot.getValue());
                    userList.add("kobee");
                    userList.add(noteSnapshot.getValue() + "");

                    Log.i("aga","zaazaaa2");
                    userList.add("ooo");

                    arrayAdapter = new ArrayAdapter<String>
                            (SearchUserActivity.this, android.R.layout.simple_list_item_1, userList);



                    userListView.setAdapter(arrayAdapter);

                    userMe = FirebaseAuth.getInstance().getCurrentUser();

                    if (userMe != null) {
                        String name = userMe.getDisplayName();
                        String email = userMe.getEmail();
                        // Toast.makeText(this,userMe.getEmail(),Toast.LENGTH_LONG).show();
                    }

                    // arrayAdapter.add("Eric");
                    Log.i("aga","kobe");





                    arrayAdapter.notifyDataSetChanged();
                    //JournalEntry note = noteSnapshot.getValue(JournalEntry.class);
                    //mJournalEntries.add(note);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.d(LOG_TAG, databaseError.getMessage());
            }
        });


    }


}
