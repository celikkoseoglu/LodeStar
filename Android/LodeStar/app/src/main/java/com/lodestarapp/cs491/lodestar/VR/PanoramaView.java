package com.lodestarapp.cs491.lodestar.VR;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class PanoramaView extends GLSurfaceView {
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


    public PanoramaView(Context context) {
        super(context);
        c = context;
        setEGLContextClientVersion(2);

        setRenderer(pr = new PanoRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }



    @Override
    public void onPause(){
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
                        pr.mDeltaX = 0;
                        /*
                        if(pr.mDeltaX >= Math.PI )
                            deltaY = 0;

                        pr.mDeltaX += -deltaY*0.2;

                        String TAG = "pano ";

                        Log.d(TAG, "" +pr.mDeltaX );*/

                    }
                }
                xBefore = x;
                yBefore = y;
            }
            requestRender();

        }
        return true;
    }

    private class PanoRenderer implements Renderer {
        volatile public float mDeltaX, mDeltaY, mDeltaZ;


        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES20.glClearColor(1f, 1f, 1f, 1f);
            renderer = new com.lodestarapp.cs491.lodestar.VR.Renderer(c, 50, 5f);
            renderer.loadTexture(c, R.drawable.photo_sphere_2);

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

            mDeltaX = 0.0f;
            mDeltaY = 0.0f;
            mDeltaZ = 0.0f;

            Matrix.multiplyMM(temp, 0, curRotation, 0, accRotation, 0);
            System.arraycopy(temp, 0, accRotation, 0, 16);

            Matrix.multiplyMM(temp, 0, mIdentity, 0, accRotation, 0);
            System.arraycopy(temp, 0, mIdentity, 0, 16);



            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            multiplyMM(mView, 0, mIdentity, 0, mCamera, 0);

            multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);

            renderer.draw(mViewProjectionMatrix);

            //checkGLError("onDrawEye");
        }
    }
}