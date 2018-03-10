package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lodestarapp.cs491.lodestar.Adapters.User;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {
    ListView userListView;

    DatabaseReference dref;
    DatabaseReference dref2;
    FirebaseUser userMe;
    private DatabaseReference mDatabase;
    final ArrayList<String> userList = new ArrayList<String>();;
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userListView = findViewById(R.id.myListe);

        arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.activity_search_user, userList);

        userListView.setAdapter(arrayAdapter);



        userMe = FirebaseAuth.getInstance().getCurrentUser();

        if (userMe != null) {
            String name = userMe.getDisplayName();
            String email = userMe.getEmail();
            Toast.makeText(this,userMe.getEmail(),Toast.LENGTH_LONG).show();
        }

        arrayAdapter.add("Eric");

        retrieveDBValues();
        arrayAdapter.notifyDataSetChanged();
    }


    private void retrieveDBValues() {


        mDatabase.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (com.google.firebase.database.DataSnapshot child : dataSnapshot.getChildren()) {
                    for (com.google.firebase.database.DataSnapshot child2 : child.getChildren()) {
                        userList.add((child2.child("username").toString() + " -----" + child2.child("e-mail").toString()));
                        // Log.i("aga", child2.child("e-mail").getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
