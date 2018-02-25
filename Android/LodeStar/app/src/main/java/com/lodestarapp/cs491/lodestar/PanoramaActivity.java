package com.lodestarapp.cs491.lodestar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.lodestarapp.cs491.lodestar.VR.PanoramaView;

public class PanoramaActivity extends AppCompatActivity {

    private PanoramaView pView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pView = new PanoramaView(this);
        setContentView(pView);
    }

}