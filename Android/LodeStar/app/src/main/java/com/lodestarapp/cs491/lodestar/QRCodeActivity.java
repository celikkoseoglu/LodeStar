package com.lodestarapp.cs491.lodestar;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.pdf417.encoder.PDF417;
import com.lodestarapp.cs491.lodestar.Models.QRCodeInfo;

import java.io.IOException;


public class QRCodeActivity extends AppCompatActivity{


    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private SurfaceView surfaceView;

    private static final String TAG = "qrCodeMessage";

    private final int REQUEST_CAMERA = 100;

    private boolean read = false;

    private QRCodeInfo qrCodeInfo;

    private AlertDialog.Builder diyalogOlusturucu;

    private FirebaseAuth mAuth;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        surfaceView = findViewById(R.id.surfaceView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();


        qrCodeInfo = new QRCodeInfo();



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

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1600,1024)
                .setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if(ContextCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    try {

                        cameraSource.start(surfaceView.getHolder());
                        Toast toast =  Toast.makeText(getApplicationContext(), "camera source started", Toast.LENGTH_LONG);
                        toast.show();
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeData = detections.getDetectedItems();
                final FirebaseUser currentUser;
                currentUser = mAuth.getCurrentUser();


                diyalogOlusturucu =
                        new AlertDialog.Builder(QRCodeActivity.this);

                QRCodeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if(barcodeData.size()>0) {
                            read = true;
                            Log.d(TAG, "23423423");

                            Toast.makeText(QRCodeActivity.this, barcodeData.valueAt(0).displayValue , Toast.LENGTH_SHORT).show();

                            diyalogOlusturucu.setMessage("Would you like to add this flight to your History page?")
                                    .setCancelable(true)
                                    .setNegativeButton("NO :(", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //Log.i("agam","laalalal" + userListView.getSelectedItem());
                                            dialog.dismiss();

                                            Log.d(TAG, "asdfsadf");

                                            String[] parts = barcodeData.valueAt(0).displayValue.split(" +");

                                            qrCodeInfo.setFrom(parts[2].substring(0, 3));
                                            qrCodeInfo.setTo(parts[2].substring(3, 6));
                                            qrCodeInfo.setFlightCode(parts[2].substring(6).concat(parts[3]));

                                            Log.d(TAG, "99999999999");

                                            cameraSource.stop();

                                            returnToTripActivity();

                                        }
                                    })
                                    .setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String[] parts = barcodeData.valueAt(0).displayValue.split(" +");

                                            qrCodeInfo.setFrom(parts[2].substring(0, 3));
                                            qrCodeInfo.setTo(parts[2].substring(3, 6));
                                            qrCodeInfo.setFlightCode(parts[2].substring(6).concat(parts[3]));

                                            Log.d(TAG, "99999999999");

                                            FirebaseDatabase.getInstance().getReference().child("users")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            FirebaseDatabase.getInstance().getReference().child("users")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                String str = snapshot.getValue() + "";
                                                                                // Log.i("ii",str);
                                                                                if (currentUser != null) {
                                                                                    String userEmail = currentUser.getEmail();

                                                                                    //Parse the data
                                                                                    String tmpArr[] = str.split(",");
                                                                                    String myTMPSTR = tmpArr[1].substring(7,tmpArr[1].length()-1);
                                                                                    Log.i("ii",myTMPSTR + " vs " + userEmail);
                                                                                    if(userEmail.equals(myTMPSTR)) {

                                                                                        mDatabase.child("users").child(snapshot.getKey()).child("trips").setValue("Flight Code: " + qrCodeInfo.getFlightCode()+ " From: " + qrCodeInfo.getFrom() + " To: " + qrCodeInfo.getTo());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }


                                                                    });

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }


                                                    });



                                            Intent intent = new Intent(QRCodeActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });


                            diyalogOlusturucu.create().show();


                        }
                    }
                });

                Log.d("q", "Size: " + barcodeData.size());

                for (int i = 0; i < barcodeData.size(); i++){
                    Log.d(TAG, "At i: " + i + " have: " + barcodeData.valueAt(i).displayValue);
                }
            }
        });

    }

    private void returnToTripActivity() {

        Log.d(TAG, "3333");

        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra("QRCodeInfo", qrCodeInfo);
        startActivity(intent);
        Log.d(TAG, "4444");
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
                    Intent intent = new Intent(this, TabActivity.class);
                    startActivity(intent);
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }
}