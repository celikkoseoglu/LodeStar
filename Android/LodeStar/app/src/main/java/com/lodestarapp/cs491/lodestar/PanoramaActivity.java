package com.lodestarapp.cs491.lodestar;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.vrtoolkit.cardboard.sensors.DeviceSensorLooper;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.google.vrtoolkit.cardboard.sensors.SystemClock;
import com.lodestarapp.cs491.lodestar.VR.PanoramaView;

public class PanoramaActivity extends AppCompatActivity {

    PanoramaView pv;
    SensorManager sManager;
    Sensor rotationVectorSensor;
    Sensor gyroscopeSensor;
    Sensor gameRotatiton;

    Sensor accelSensor;
    Sensor magnSensor;
    HeadTracker ht;
    float angle = 80.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        pv = findViewById(R.id.layout);
        //pv.setAngle(angle);

        ImageView im = findViewById(R.id.close);
        im.setVisibility(View.GONE);

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        gyroscopeSensor = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnSensor = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        rotationVectorSensor = sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        gameRotatiton = sManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //sManager.registerListener(pv.pr,gameRotatiton, SensorManager.SENSOR_DELAY_GAME);
        //sManager.registerListener(pv.pr,magnSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onStop() {
        sManager.unregisterListener(pv.pr);

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

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.VISIBLE);

        ImageView im3 = findViewById(R.id.vrSmall);
        im3.setVisibility(View.GONE);
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

        ImageView im2 = findViewById(R.id.vrBig);
        im2.setVisibility(View.GONE);

        ImageView im3 = findViewById(R.id.vrSmall);
        im3.setVisibility(View.VISIBLE);

    }

    public void goVr(View view) {
        Intent intent = new Intent(this, VRActivity.class);
        intent.putExtra("VrAngle", angle);

        startActivity(intent);

    }
}