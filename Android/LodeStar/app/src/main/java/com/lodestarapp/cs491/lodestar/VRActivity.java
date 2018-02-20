package com.lodestarapp.cs491.lodestar;

import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.lodestarapp.cs491.lodestar.VR_sample_code.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR_sample_code.Sphere;

import javax.microedition.khronos.egl.EGLConfig;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class VRActivity extends CardboardActivity implements CardboardView.StereoRenderer{
    private static final String TAG = "VR ";
    private CardboardView cv;
    private Sphere mSphere;


    private final float[] mCamera = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];

    private float CAMERA_Z = 0.5f;
    private float[] mView = new float[16];
    private CardboardView mCardboardView;
    private int[] mResourceId = {R.drawable.airport, R.drawable.photo_sphere_2 };
    private int mCurrentPhotoPos = 0;
    private boolean mIsCardboardTriggered;
    private MediaPlayer mMediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);

        cv = (CardboardView) findViewById(R.id.cardboard_view);
        cv.setRenderer(this);
        setCardboardView(cv);

        Toast toast =  Toast.makeText(getApplicationContext(), "Bring the phone in horizontal position", Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    public void onFinishFrame(Viewport p0) {

    }

    @Override
    public void onSurfaceChanged(int p0, int p1) {
        glViewport(0, 0, p0, p1);

        MatrixCalculator.perspectiveM(mProjectionMatrix, 90, (float) p0 / (float) p1, 1f, 10f);
    }

    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(1f, 1f, 0f, 1f);// Dark background so text shows up well.

        mSphere = new Sphere(this, 50, 5f);
        mSphere.loadTexture(this, getPhotoIndex());
        checkGLError("onSurfaceCreated");

    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        //checkGLError("onReadyToDraw");
    }


    public void onDrawEye(Eye eye) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);

        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);

        mSphere.draw(mViewProjectionMatrix);

        //checkGLError("onDrawEye");

        if (mIsCardboardTriggered) {
            mIsCardboardTriggered = false;
            resetTexture();
        }
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        mIsCardboardTriggered = true;


    }


    private void resetTexture() {
        mSphere.deleteCurrentTexture();
        checkGLError("after deleting texture");
        mSphere.loadTexture(this, getPhotoIndex());
        checkGLError("loading texture");
    }

    private int getPhotoIndex() {
        return mResourceId[mCurrentPhotoPos++ % mResourceId.length];


    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mMediaPlayer != null)
            mMediaPlayer.stop();

    }

    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            //throw new RuntimeException(label + ": glError " + error);
        }
    }

}
