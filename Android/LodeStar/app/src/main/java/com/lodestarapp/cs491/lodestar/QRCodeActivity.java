package com.lodestarapp.cs491.lodestar;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class QRCodeActivity extends AppCompatActivity{


    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private SurfaceView surfaceView;

    private static final String TAG = "qrCodeMessage";

    private final int REQUEST_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        surfaceView = findViewById(R.id.surfaceView);

        int cameraPermissionCheck = ContextCompat.
                checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            barcodeDetector = new BarcodeDetector.Builder(this).
                    setBarcodeFormats(Barcode.QR_CODE).build();

            cameraSource = new CameraSource.Builder(this, barcodeDetector).build();

            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    /*try {
                        cameraSource.start();
                    }catch (IOException ioException){
                        ioException.printStackTrace();
                    }*/



                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
        }
        else {
            ActivityCompat.requestPermissions(QRCodeActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults){

    }
}
