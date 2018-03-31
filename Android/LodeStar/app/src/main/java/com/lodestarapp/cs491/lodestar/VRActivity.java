package com.lodestarapp.cs491.lodestar;

import android.content.pm.ActivityInfo;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class VRActivity extends CardboardActivity implements CardboardView.StereoRenderer{
    private static final String TAG = "VR ";
    private CardboardView cv;
    private Renderer renderer;
    private float headRotatiton;


    private final float[] mCamera = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];

    private float CAMERA_Z = 0.5f;
    private float[] mView = new float[16];
    private int[] mResourceId = {R.drawable.airport1 , R.drawable.photo_sphere};
    private int mCurrentPhotoPos = 0;
    private boolean mIsCardboardTriggered = false;

    float mDeltaY = 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);

        cv = (CardboardView) findViewById(R.id.cardboard_view);
        cv.setRenderer(this);
        setCardboardView(cv);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        //Toast toast =  Toast.makeText(getApplicationContext(), "Bring the phone in horizontal position", Toast.LENGTH_LONG);
        //toast.show();

    }

    @Override
    public void onFinishFrame(Viewport p0) {

    }

    @Override
    public void onSurfaceChanged(int p0, int p1) {
        glViewport(0, 0, p0, p1);

        MatrixCalculator.perspectiveUpdate(mProjectionMatrix, 90, (float) p0 / (float) p1, 1f, 10f);
    }

    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(1f, 1f, 1f, 1f);


        renderer = new Renderer(this, 50, 5f);
        renderer.loadTexture(this, getPhotoIndex());
        checkGLError("onSurfaceCreated");

    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, (float)Math.sin(mDeltaY), 0.0f, (float) -Math.cos(mDeltaY), 0.0f, 1.0f, 0.0f);

        if(mIsCardboardTriggered){
            mIsCardboardTriggered = false;
            float[] angles = new float[3];
            headTransform.getEulerAngles(angles,0);
            for(int i=0;i<angles.length;i++)
                angles[i] = (float) Math.toDegrees((float)angles[i]);

            headRotatiton = angles[1];
            if(Math.abs(headRotatiton) < 20)
                resetTexture();

            //Log.d("eye view", floatToString(angles));
        }


        checkGLError("onReadyToDraw");
    }


    public void onDrawEye(Eye eye) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);


        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);

        renderer.draw(mViewProjectionMatrix);

        checkGLError("onDrawEye");


    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        mIsCardboardTriggered = true;


    }


    private void resetTexture() {
        renderer.deleteCurrentTexture();
        checkGLError("after deleting texture");
        renderer.loadTexture(this, getPhotoIndex());
        checkGLError("loading texture");
    }

    private int getPhotoIndex() {
        return mResourceId[mCurrentPhotoPos++ % mResourceId.length];
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsCardboardTriggered = true;

                //Toast toast =  Toast.makeText(getApplicationContext(), "touched", Toast.LENGTH_SHORT);
                //toast.show();
        }

        return true;
    }




        private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            //throw new RuntimeException(label + ": glError " + error);
        }
    }

    public static String floatToString(float arr[]){
        String s = "";
        for(int i=0;i<arr.length;i++)
            s += arr[i] + " ";

        return s;
    }

    public float[] set(float[] q) {
        float r[]= new float[3];
        if (q[3] > 1)
        {
            float d = (float) Math.sqrt(q[0]*q[0]+q[1]*q[1]+q[2]*q[2]+q[3]*q[3]);
            for(int i=0;i<4;i++)
                q[i] = q[i] / d;
        }
        double angle = 2 * Math.acos(q[3]);
        float s = (float) Math.sqrt(1-q[3]*q[3]);

        if (s < 0.001) {
            r[0] = q[0]; // if it is important that axis is normalised then replace with x=1; y=z=0;
            r[1] = q[1];
            r[2] = q[2];
        } else {
            r[0] = q[0] /s;// if it is important that axis is normalised then replace with x=1; y=z=0;
            r[1] = q[1] /s;
            r[2] = q[2] /s;
        }
        return r;
    }


}

