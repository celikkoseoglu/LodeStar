package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryActivity extends Fragment {

    private FirebaseAuth mAuth;


    private DatabaseReference mDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //TODO:Check the state - whether there is item in history or not from database
        super.onCreate(savedInstanceState);

        //if ....
            //setContentView(R.layout.fragment_history);
        //else ...
        //setContentView(R.layout.activity_history_initial);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //TODO:Check the state - whether there is item in history or not from database
        //if ....
        //  View view = inflater.inflate(R.layout.fragment_history, container, false)
        //else
        //  View view = inflater.inflate(R.layout.activity_history_initial, container, false);

        View view = inflater.inflate(R.layout.activity_history_initial, container, false);
        return view;
    }

    /*@Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }*/
}
