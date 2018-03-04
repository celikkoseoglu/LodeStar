package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class SearchUserActivity extends AppCompatActivity {

    ListView userListView;

    DatabaseReference dref;
    DatabaseReference dref2;
    FirebaseUser userMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userMe = FirebaseAuth.getInstance().getCurrentUser();

        if (userMe != null) {
            String name = userMe.getDisplayName();
            String email = userMe.getEmail();
            Toast.makeText(this,userMe.getEmail(),Toast.LENGTH_LONG).show();
        }




        setContentView(R.layout.content_search_user);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        userListView = (ListView) findViewById(R.id.listViewArtists);

        dref = getInstance().getReference();
//        dref2 = dref.child("users");
//
//        dref2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                 @Override
//                                                 public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                 }
//                                                 @Override
//                                                 public void onCancelled(DatabaseError databaseError) {
//
//                                                 }
//                                             });


        //Toast.makeText(this,"Kobe",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
