package com.lodestarapp.cs491.lodestar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.lodestarapp.cs491.lodestar.Controllers.VenueController;
import com.lodestarapp.cs491.lodestar.VR.MatrixCalculator;
import com.lodestarapp.cs491.lodestar.VR.Renderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private int mCurrentPhotoPos = 0;
    private boolean mIsCardboardTriggered = false;

    float mDeltaY = 0.0f;
    float arrowAngle = 0.0f;

    String coords;
    VenueController vc = new VenueController();
    byte[] panorama;
    boolean incoming = false;
    ArrayList<Float> arrows;
    ArrayList<String> panoids;
    LinearLayout ll1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mDeltaY = data.getInt("VrAngle");
            coords  = data.getString("BitmapName");
            arrowAngle = mDeltaY;
        }
        Log.d(TAG,coords);

        ll1 = findViewById(R.id.ll1);
        ll1.setVisibility(View.VISIBLE);

        cv = (CardboardView) findViewById(R.id.cardboard_view);
        cv.setRenderer(this);
        setCardboardView(cv);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Toast toast =  Toast.makeText(getApplicationContext(), "Bring the phone in horizontal position", Toast.LENGTH_LONG);
        //toast.show();

        vc = new VenueController();
        vc.getPanorama(coords,"50","low", getApplicationContext(),new VenueController.VolleyCallback(){

            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String encoded = result.getString("lowRes");
                    double pano_deg = result.getJSONObject("Projection").getDouble("pano_yaw_deg") ;

                    double angle  = (result.getJSONObject("Location").getDouble("best_view_direction_deg")+ 450 - pano_deg )%360.0;
                    //pv.setAngle((float) angle);
                    panorama = Base64.decode(encoded, Base64.DEFAULT);
                    //renderer.loadTexture(getApplicationContext(), b);
                    incoming = true;

                    JSONArray links=result.getJSONArray("Links");
                    JSONObject jo;
                    arrows  = new ArrayList<>();
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

                    vc.getPanorama(coords,"50","high", getApplicationContext(),new VenueController.VolleyCallback(){
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String encoded = result.getString("highRes");
                                panorama = Base64.decode(encoded, Base64.DEFAULT);
                                incoming = true;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


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
        renderer.loadTexture(this, R.drawable.gray);
        //renderer.setAngle(arrowAngle);
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
            if(headRotatiton < 0)
                headRotatiton += 360;

            for(int i=0;i<arrows.size();i++){
                if( distance(headRotatiton, arrows.get(i)) < 20){
                    Log.i("Check:",i + " touched: " + arrows.get(i));
                    String url = "http://cbk0.google.com/cbk?output=json&panoid=" + panoids.get(i);
                    Log.i("Check:",url);
                    renderer.deleteCurrentTexture();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog = new ProgressDialog(VRActivity.this, R.style.Theme_MyDialog);
                            //progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setTitle("Loading...");
                            progressDialog.setMessage("Retrieving panorama from the server");
                            progressDialog.show();
                        }
                    });


                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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

                                vc.getPanorama(location,"50","high", getApplicationContext(),new VenueController.VolleyCallback(){

                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        try {
                                            String encoded = result.getString("highRes");
                                            byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
                                            renderer.setArrows(arrows);
                                            panorama = Base64.decode(encoded, Base64.DEFAULT);
                                            incoming = true;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                }
                                            });
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




            Log.d("eye view", headRotatiton+ "");
        }


        checkGLError("onReadyToDraw");
    }


    public void onDrawEye(Eye eye) {
        if(panorama!=null &&incoming){
            incoming = false;
            renderer.setArrows(arrows);
            renderer.loadTexture(this, panorama);
        }

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
        //renderer.loadTexture(this, getPhotoIndex());
        checkGLError("loading texture");
    }

    //private int getPhotoIndex() { return mResourceId[mCurrentPhotoPos++ % mResourceId.length];  }

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

    private float distance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }


}

