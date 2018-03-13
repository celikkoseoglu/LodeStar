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
    final ArrayList<String> userList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    private String[] lolol =
            {"djdoj","dhıjpş","cdxs"};

    private String[] tmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //Toast.makeText(this, "Time",Toast.LENGTH_LONG).show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userListView = findViewById(R.id.myListe);

        Bundle extras = getIntent().getExtras();
        String[] value;
        if (extras != null) {
            value = extras.getStringArray("key");
            //The key argument here must match that used in the other activity
        }
        else {
            value = new String[5];
        }

        //retrieveDBValues();

        //tmp = new String[userList.size()];

        Toast.makeText(this,""+  "forever" + value,Toast.LENGTH_LONG).show();

//        for (int i = 0; i < userList.size(); i++) {
//            tmp[i] = userList.get(i);
//            Toast.makeText(this, "Burda2",Toast.LENGTH_LONG).show();
//            Log.i("a","sup" + tmp[i]);
//        }



        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, value);



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


    }


}
