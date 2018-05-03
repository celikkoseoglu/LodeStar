package com.lodestarapp.cs491.lodestar.VR;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.DistortionRenderer;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.google.vrtoolkit.cardboard.sensors.SensorEventProvider;
import com.lodestarapp.cs491.lodestar.Controllers.VenueController;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;
import com.lodestarapp.cs491.lodestar.WeatherInformationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.SENSOR_SERVICE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindAttribLocation;
import static android.opengl.GLES20.glBlendEquationSeparate;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class PanoramaView extends GLSurfaceView{
    com.lodestarapp.cs491.lodestar.VR.Renderer renderer;

    private final float[] mCamera = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];

    float[] mIdentity = new float[16];
    private float[] curRotation = new float[16];
    private float[] accRotation = new float[16];
    private float[] temp = new float[16];

    private float CAMERA_Z = 0.5f;
    private float[] mView = new float[16];
    private int mCurrentPhotoPos = 0;
    Context c;
    public PanoRenderer pr;
    private float xBefore;
    private float yBefore;
    private float degBefore;
    String TAG = "pano ";
    HeadTracker ht;


    float[] rotationMatrix = new float[16];
    float[] tempMatrix = new float[16];


    float[] rotMatrix  = new float[16];
    float[] orientations = new float[3];

    float angle = 0.0f;
    ScrollView sc;

    byte[] texture;
    boolean incoming = false;
    PanoramaView pv;
    RelativeLayout relative;
    LinearLayout ll1;
    ArrayList<Float> arrows;
    private ArrayList<String> panoids;
    VenueController vc = new VenueController();
    String coords;
    LinearLayout ll;
    private ProgressDialog progressDialog;


    public PanoramaView(Context context) {
        super(context);
        c = context;
        setEGLContextClientVersion(2);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        ht = HeadTracker.createFromContext(c);
    }

    public PanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
        setEGLContextClientVersion(2);
        //setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        ht = HeadTracker.createFromContext(c);


    }

    float readX=0;
    float readY=0;
    float renderWidth;
    float renderHeight;
    boolean clicked;


    @Override
    public void onPause(){
        super.onPause();
        ht.stopTracking();
    }

    public void setLL(LinearLayout cor){ ll = cor;};

    public LinearLayout getLL(){return ll;}


        public void setAngle(float ang){
        angle = ang;
    }

    public void setArrows(ArrayList<Float> ang){
        arrows = new ArrayList<>(ang);
    }

    public void setCoords(String cor){ coords = cor;};

    public String getCoords(){return coords;}




        @Override
    public void onResume(){
        ht.startTracking();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event != null){
            if(event.getPointerCount()==1)
            {
                float x = event.getX();
                float y = event.getY();

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    readY = y;
                    readX = x;

                    renderHeight = this.getHeight();
                    renderWidth = this.getWidth();
                    clicked = true;

                    ht.getLastHeadView(rotMatrix,0);
                    Log.d("head view:", floatToString(rotMatrix));



                }

                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    if (pr != null)
                    {
                        float deltaX = (x - xBefore) / this.getWidth() * 360;
                        float deltaY = (y - yBefore) / this.getHeight() * 360;


                        pr.mDeltaY += -deltaX*0.2 * (3.14159265 / 180.0);
                        pr.mDeltaX += deltaY*0.2 * (3.14159265 / 180.0);

                        if(pr.mDeltaX > 3.1415f / 2)
                            pr.mDeltaX = 3.1415f / 2;
                        else if(pr.mDeltaX < -3.1415f / 2)
                            pr.mDeltaX = -3.1415f / 2;


                        //Log.d(TAG, "" +pr.mDeltaY );

                    }
                }
                xBefore = x;
                yBefore = y;
            }
            requestRender();

        }
        return true;
    }

    public void setPanos(ArrayList<String> panoid) {
        panoids = new ArrayList<>(panoid);
    }

    private class PanoRenderer implements Renderer, SensorEventListener{
        volatile public float mDeltaX, mDeltaY, mDeltaZ;
        volatile public float roll,pitch,yaw;
        volatile boolean sensorRead= false;

        DistortionRenderer dr;



        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(1f, 1f, 1f, 1f);
            renderer = new com.lodestarapp.cs491.lodestar.VR.Renderer(c, 50, 5f);
            renderer.setArrows(arrows);
            renderer.loadTexture(c, R.drawable.alan1);

            Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            Matrix.setIdentityM(accRotation, 0);

            this.dr = new DistortionRenderer();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {
            glViewport(0, 0, i, i1);

            MatrixCalculator.perspectiveUpdate(mProjectionMatrix, 90, (float) i / (float) i1, 1f, 10f);

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            Matrix.setIdentityM(mIdentity, 0);

            Matrix.setIdentityM(curRotation, 0);

            if(incoming){
                renderer.loadTexture(c,texture);
                incoming = false;
            }

            if(sensorRead)
                Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f,  CAMERA_Z, (float)Math.sin(mDeltaY),  0.2f, (float) -Math.cos(mDeltaY), 0.0f, 1.0f, 0.0f);
            else
                Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f,  CAMERA_Z, (float)Math.sin(mDeltaY),  ((float)Math.sin(mDeltaX)) + 0.2f, (float) -Math.cos(mDeltaY), 0.0f, 1.0f, 0.0f);



            Matrix.multiplyMM(temp, 0, curRotation, 0, accRotation, 0);
            System.arraycopy(temp, 0, accRotation, 0, 16);

            Matrix.multiplyMM(temp, 0, mIdentity, 0, accRotation, 0);
            System.arraycopy(temp, 0, mIdentity, 0, 16);


            //Matrix.rotateM(mIdentity, 0, roll, 1.0f, 0.0f, 0.0f);
            //Matrix.rotateM(mIdentity, 0, pitch, 0.0f, 1.0f, 0.0f);
            //Matrix.rotateM(mIdentity, 0, yaw, 0.0f, 0.0f, 1.0f);
            if(sensorRead){
                Matrix.rotateM(rotationMatrix, 0, 90, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(rotationMatrix, 0, -90, 0.0f, 1.0f, 0.0f);
                Matrix.multiplyMM(mIdentity,0,rotationMatrix, 0, mIdentity,0);

                //sensorRead = false;
            }

            multiplyMM(mView, 0, mIdentity, 0, mCamera, 0);

            multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);

            renderer.draw(mViewProjectionMatrix);


            if(clicked){
                clicked = false;

                for(int i=0;i<arrows.size();i++){
                    int x = renderer.testTouch(renderWidth,renderHeight,readX,readY,mView,mProjectionMatrix, (arrows.get(i)) );
                    if(x>0){
                        Log.i("Check:",i + " touched: " + arrows.get(i));
                        String url = "http://cbk0.google.com/cbk?output=json&panoid=" + panoids.get(i);
                        Log.i("Check:",url);
                        final Activity ac= (Activity) c;
                        renderer.deleteCurrentTexture();
                        ac.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog = new ProgressDialog(c, R.style.Theme_MyDialog);
                                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setTitle("Loading...");
                                    progressDialog.setMessage("Retrieving panorama from the server");
                                    progressDialog.show();
                                }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(c);


                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.i("Check:",response.toString());
                                    String location = response.getJSONObject("Location").getString("lat")+ "," +
                                            response.getJSONObject("Location").getString("lng");
                                    renderer.deleteCurrentTexture();
                                    double pano_deg = response.getJSONObject("Projection").getDouble("pano_yaw_deg") ;
                                    JSONArray links=response.getJSONArray("Links");
                                    JSONObject jo;
                                    arrows = new ArrayList<>();
                                    panoids = new ArrayList<>();
                                    for(int i = 0;i<links.length();i++){
                                        jo = links.getJSONObject(i);
                                        float ang = 180f - (float) ((float) (jo.getDouble("yawDeg") + 450- pano_deg) % 360.0);
                                        if(ang < 0)
                                            arrows.add(360+ang);
                                        else
                                            arrows.add(ang);
                                        panoids.add(jo.getString("panoId"));
                                    }

                                    vc.getPanorama(location,"50","high", c,new VenueController.VolleyCallback(){

                                        @Override
                                        public void onSuccess(JSONObject result) {
                                            try {
                                                String encoded = result.getString("highRes");
                                                byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                                                renderer.setArrows(arrows);
                                                ac.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                                setBitmap(decodedString);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error occured in request");
                            }
                        });
                        requestQueue.add(jsonObjectRequest);
                    }
                }
            }
            //checkGLError("onDrawEye");
        }


        private float[] mLastAccelerometer = new float[3];
        private float[] mLastMagnetometer = new float[3];
        private boolean mLastAccelerometerSet = false;
        private boolean mLastMagnetometerSet = false;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (pr != null) {
                String TAG="pano";

                if (sensorEvent.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
                    sensorRead = true;
                    SensorManager.getRotationMatrixFromVector(rotationMatrix , sensorEvent.values);
                    SensorManager.getOrientation(rotationMatrix, orientations);
                    //Matrix.invertM(rotationMatrix,0,rotationMatrix,0);

                    float theta = (float) (Math.acos(sensorEvent.values[3])*2);
                    float sinv = (float) Math.sin(theta/2);

                    roll = sensorEvent.values[2]/sinv;     //x
                    pitch = sensorEvent.values[1]/sinv;   //y
                    yaw = sensorEvent.values[0]/sinv;     //z
                    requestRender();
                }


                /*if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(sensorEvent.values, 0, mLastAccelerometer, 0, sensorEvent.values.length);
                    mLastAccelerometerSet = true;

                } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(sensorEvent.values, 0, mLastMagnetometer, 0, sensorEvent.values.length);
                    mLastMagnetometerSet = true;
                }
                if (mLastAccelerometerSet && mLastMagnetometerSet) {
                    SensorManager.getRotationMatrix(rotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
                    SensorManager.getOrientation(rotationMatrix, orientations);

                    yaw = (float) (orientations[0] * 180 / Math.PI);
                    pitch = (float) (orientations[1]* 180 / Math.PI);
                    roll = (float) (orientations[2]* 180 / Math.PI);
                }*/


                //Log.i(TAG,Float.toString(roll)+" "+Float.toString(pitch)+" "+Float.toString(yaw));

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    public static String floatToString(float arr[]){
        String s = "";
        for(int i=0;i<arr.length;i++)
            s += arr[i] + " ";

        return s;
    }

    public void setBitmap(byte[] b){
        texture = b;
        if(texture != null)
            incoming = true;
        requestRender();
    }
}