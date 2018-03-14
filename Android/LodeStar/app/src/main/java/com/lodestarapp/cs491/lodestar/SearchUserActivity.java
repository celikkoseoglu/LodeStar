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

public class SearchUserActivity extends AppCompatActivity {
    ListView userListView;
    FirebaseUser userMe;
    private DatabaseReference mDatabase;
     ArrayList<String> userList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReacher;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReacher = mDatabase.child("users");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userListView = findViewById(R.id.myListe);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> value;
        if (extras != null) {
            value = extras.getStringArrayList("key");
        }
        else {
            value = null;
        }

        Toast.makeText(this,""+  "forever" + value,Toast.LENGTH_LONG).show();

        retrieveDBValues();

    }


    private void retrieveDBValues() {
        databaseReacher.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                    tmp = noteSnapshot.getValue() + "";
                    tmp = tmp.substring(1, tmp.length() - 1);
                    userList.add(tmp);

                }
                arrayAdapter = new ArrayAdapter<String>
                        (SearchUserActivity.this, android.R.layout.simple_list_item_1, userList);

                userListView.setAdapter(arrayAdapter);

                userMe = FirebaseAuth.getInstance().getCurrentUser();

                if (userMe != null) {
                    String name = userMe.getDisplayName();
                    String email = userMe.getEmail();
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.d(LOG_TAG, databaseError.getMessage());
            }
        });


    }


}

//Reference: This class uses some of codes of the tutorial on https://www.codeproject.com/Articles/1170499/Firebase-Realtime-Database-By-Example-with-Android
// and the tutorial of turkcell on https://gelecegiyazanlar.turkcell.com.tr/konu/android/egitim/android-201/listview-kullanimi