package com.lodestarapp.cs491.lodestar;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lodestarapp.cs491.lodestar.VR.PanoramaView;

public class PanoramaActivity extends AppCompatActivity {

    PanoramaView pv;
    SensorManager sManager;
    Sensor rotationVectorSensor;
    Sensor gyroscopeSensor;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        pv = findViewById(R.id.layout);

        ImageView im = findViewById(R.id.close);
        im.setVisibility(View.GONE);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        gyroscopeSensor = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        rotationVectorSensor = sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sManager.registerListener(pv,gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);


    }

    @Override
    protected void onStop() {
        sManager.unregisterListener(pv);

        super.onStop();
    }

    public void goFullscreen(View view) {
        PanoramaView panoramaView = findViewById(R.id.layout);
        ViewGroup.LayoutParams layoutParams=panoramaView.getLayoutParams();
        layoutParams.width=-1;
        layoutParams.height=-1;
        panoramaView.setLayoutParams(layoutParams);

        ImageView im = findViewById(R.id.full);
        im.setVisibility(View.GONE);

        ImageView im1 = findViewById(R.id.close);
        im1.setVisibility(View.VISIBLE);
    }

    public void goSmall(View view){

        PanoramaView panoramaView =  findViewById(R.id.layout);
        ViewGroup.LayoutParams layoutParams=panoramaView.getLayoutParams();
        float scale = this.getResources().getDisplayMetrics().density;
        layoutParams.height= (int) (300 * scale + 0.5f);

        panoramaView.setLayoutParams(layoutParams);

        ImageView im = findViewById(R.id.full);
        im.setVisibility(View.VISIBLE);

        ImageView im1 = findViewById(R.id.close);
        im1.setVisibility(View.GONE);

    }
}