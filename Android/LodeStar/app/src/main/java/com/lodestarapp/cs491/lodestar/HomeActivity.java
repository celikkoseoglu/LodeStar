package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeActivity extends Fragment {

    Button manualSearchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //reference to: https://stackoverflow.com/questions/21192386/android-fragment-onclick-button-method & http://www.androidbie.com/2017/07/tutorial-how-to-move-from-activity-to.html

        View myView = inflater.inflate(R.layout.activity_home, container, false);

        manualSearchButton = (Button) myView.findViewById(R.id.button4);

        manualSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManualDestinationActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return myView;
    }

    public void readQRCode(View view){
        Intent intent = new Intent(getActivity(), QRCodeActivity.class);
        startActivity(intent);
    }



}
