package com.lodestarapp.cs491.lodestar;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeActivity extends Fragment {

    Button manualSearchButton,historyButton,vRRedirectButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //reference to: https://stackoverflow.com/questions/21192386/android-fragment-onclick-button-method & http://www.androidbie.com/2017/07/tutorial-how-to-move-from-activity-to.html

        View myView = inflater.inflate(R.layout.activity_home, container, false);

        manualSearchButton = (Button) myView.findViewById(R.id.button4);

        vRRedirectButton = (Button) myView.findViewById(R.id.button6);

        //Referece for redirecting a web page: https://stackoverflow.com/questions/2236413/how-to-redirect-to-particular-url-while-clicking-on-button-in-android

        vRRedirectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://vr.google.com/cardboard/"));
                startActivity(viewIntent);
            }
        });



        manualSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchFlightActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return myView;
    }
}
