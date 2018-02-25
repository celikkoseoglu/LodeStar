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
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.sensors.SensorEventProvider;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.SENSOR_SERVICE;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class PanoramaView extends GLSurfaceView implements SensorEventListener{
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
    PanoRenderer pr;
    private float xBefore;
    private float yBefore;
    private float degBefore;

    float[] rotationMatrix = new float[16];
    float[] remappedRotationMatrix = new float[16];
    float[] orientations = new float[3];
    public PanoramaView(Context context) {
        super(context);
        c = context;
        setEGLContextClientVersion(2);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public PanoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
        setEGLContextClientVersion(2);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onResume(){
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event != null){
            if(event.getPointerCount()==1)
            {
                float x = event.getX();
                float y = event.getY();

                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    if (pr != null)
                    {
                        float deltaX = (x - xBefore) / this.getWidth() * 360;
                        float deltaY = (y - yBefore) / this.getHeight() * 360;
                        pr.mDeltaY += -deltaX*0.2;

                        deltaY=0;
                        if(Math.abs(pr.totDeltaX) + deltaY>60)
                            deltaY = 0;

                        pr.mDeltaX += -deltaY*0.2;
                        pr.totDeltaX += pr.mDeltaX;

                        String TAG = "pano ";

                        //Log.d(TAG, "" +pr.totDeltaX );

                    }
                }
                xBefore = x;
                yBefore = y;
            }
            requestRender();

        }
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (pr != null) {
            String TAG="pano";
            pr.roll = sensorEvent.values[2];     //x
            pr.pitch = sensorEvent.values[1];   //y
            pr.yaw = sensorEvent.values[0];     //z

            /*SensorManager.getRotationMatrixFromVector(
                    rotationMatrix, sensorEvent.values);

            SensorManager.remapCoordinateSystem(rotationMatrix,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    remappedRotationMatrix);


            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            for(int i = 0; i < 3; i++) {
                orientations[i] = (float)(Math.toDegrees(orientations[i]));
            }*/
            Log.i(TAG,Float.toString(sensorEvent.values[0])+" "+Float.toString(sensorEvent.values[1])
                    +" "+Float.toString(sensorEvent.values[2]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class PanoRenderer implements Renderer {
        volatile public float mDeltaX, mDeltaY, mDeltaZ;
        volatile public float roll,pitch,yaw;
        volatile public float totDeltaX;


        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(1f, 1f, 1f, 1f);
            renderer = new com.lodestarapp.cs491.lodestar.VR.Renderer(c, 50, 5f);
            renderer.loadTexture(c, R.drawable.airport);

            Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
            Matrix.setIdentityM(accRotation, 0);
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {
            glViewport(0, 0, i, i1);

            MatrixCalculator.perspectiveUpdate(mProjectionMatrix, 90, (float) i / (float) i1, 1f, 10f);

        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            Matrix.setIdentityM(mIdentity, 0);


            Matrix.setIdentityM(curRotation, 0);

            Matrix.rotateM(curRotation, 0, mDeltaX, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(curRotation, 0, mDeltaY, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(curRotation, 0, mDeltaZ, 0.0f, 0.0f, 1.0f);

            //Matrix.multiplyMM(curRotation,0,curRotation,0,rotationMatrix,0);

            mDeltaX = 0.0f;
            mDeltaY = 0.0f;
            mDeltaZ = 0.0f;

            Matrix.multiplyMM(temp, 0, curRotation, 0, accRotation, 0);
            System.arraycopy(temp, 0, accRotation, 0, 16);

            Matrix.multiplyMM(temp, 0, mIdentity, 0, accRotation, 0);
            System.arraycopy(temp, 0, mIdentity, 0, 16);

            Matrix.rotateM(mIdentity, 0, pr.roll, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(mIdentity, 0, pr.pitch, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mIdentity, 0, pr.yaw, 0.0f, 0.0f, 1.0f);



            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            multiplyMM(mView, 0, mIdentity, 0, mCamera, 0);



            multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);

            renderer.draw(mViewProjectionMatrix);

            //checkGLError("onDrawEye");
        }
    }
}