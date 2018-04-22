package com.lodestarapp.cs491.lodestar.VR;

import android.content.Context;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.DistortionRenderer;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.google.vrtoolkit.cardboard.sensors.SensorEventProvider;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.SENSOR_SERVICE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
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

    float angle = 80.0f;
    ScrollView sc;

    public PanoramaView(Context context) {
        super(context);
        c = context;
        setEGLContextClientVersion(2);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        ht = HeadTracker.createFromContext(c);
    }

    public PanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
        setEGLContextClientVersion(2);
        //setEGLConfigChooser(8 , 8, 8, 8, 16, 0);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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

    public void setAngle(float ang){
        angle = ang;
    }

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

    private class PanoRenderer implements Renderer, SensorEventListener{
        volatile public float mDeltaX, mDeltaY, mDeltaZ;
        volatile public float roll,pitch,yaw;
        volatile boolean sensorRead= false;

        DistortionRenderer dr;



        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(1f, 1f, 1f, 1f);
            renderer = new com.lodestarapp.cs491.lodestar.VR.Renderer(c, 50, 5f);
            renderer.loadTexture(c, R.drawable.alan1);

            Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            Matrix.setIdentityM(accRotation, 0);

            this.dr = new DistortionRenderer();
            renderer.setAngle(angle);
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
                int x = renderer.testTouch(renderWidth,renderHeight,readX,readY,mView,mProjectionMatrix,angle);
                Log.i("Check:",x + "");
                if(x>0){
                    if(angle  == 80){
                        angle = -100;
                        renderer.setAngle(angle);

                        renderer.deleteCurrentTexture();
                        renderer.loadTexture(c, R.drawable.alan2);
                    }
                    else if(angle == -100){
                        angle = 80;
                        renderer.setAngle(angle);

                        renderer.deleteCurrentTexture();
                        renderer.loadTexture(c, R.drawable.alan1);

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
}