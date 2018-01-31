package com.lodestarapp.cs491.lodestar;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
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

        checkForCameraPermission();
    }

    private void checkForCameraPermission() {
        int cameraPermissionCheck = ContextCompat.
                checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED){
            openCamera();
        }
        else if(cameraPermissionCheck == PackageManager.PERMISSION_DENIED){
            requestPermissionForCamera();
        }

    }

    private void openCamera() {
        Log.d(TAG, "openCamera function started");
        barcodeDetector = new BarcodeDetector.Builder(this).
                setBarcodeFormats(Barcode.PDF417).build();

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray barcodeData = detections.getDetectedItems();

                Toast toast =  Toast.makeText(getApplicationContext(), barcodeData.valueAt(0).toString(), Toast.LENGTH_LONG);
                toast.show();

                for (int i = 0; i < barcodeData.size(); i++){
                    Log.d(TAG, barcodeData.valueAt(i).toString());
                }
            }
        });

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if(ContextCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    try {

                        cameraSource.start(surfaceView.getHolder());
                        Log.d(TAG, "camera source started");
                    }catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });


    }

    private void requestPermissionForCamera() {
        ActivityCompat.requestPermissions(QRCodeActivity.this,
                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults){

        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "Call to openCamera");
                    openCamera();
                }
                else{
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}
