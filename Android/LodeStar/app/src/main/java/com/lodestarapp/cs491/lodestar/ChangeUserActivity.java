package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUserActivity extends AppCompatActivity {

    Button changeUsername;
    FirebaseDatabase db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("agam","1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        Log.i("agam","2");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        changeUsername = (Button) findViewById(R.id.usernameChangeButton);

        changeUsername.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Search the database for a specific user value


            }
        });
    }






    public String returnCurrentEmailofTheUserUnparsed() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        return currentUser.getEmail() + "";
    }//Returns the current email of the user

    public String removeLastCh(String mySTR) {
        String tmp = mySTR.substring(0, mySTR.length() - 1);
        return tmp;
    }//Removes the last characther

}
