package com.lodestarapp.cs491.lodestar.VR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.lodestarapp.cs491.lodestar.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;
import static java.lang.Math.sin;



public class Renderer {

    private static final int MAXIMUM_ALLOWED_DEPTH = 5;
    private static final int VERTEX_MAGIC_NUMBER = 5;
    private static final int NUM_FLOATS_PER_VERTEX = 3;
    private static final int NUM_FLOATS_PER_TEXTURE = 2;
    private static final int AMOUNT_OF_NUMBERS_PER_VERTEX_POINT = 3;
    private static final int AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT = 2;
    public static final double ONE_EIGHTY_DEGREES = Math.PI;
    public static final double THREE_SIXTY_DEGREES = ONE_EIGHTY_DEGREES * 2;
    public static final double ONE_TWENTY_DEGREES = THREE_SIXTY_DEGREES / 3;
    public static final double NINETY_DEGREES = Math.PI / 2;


    private float[] mRotationMatrix = new float[16];



    private static final long POWER_CLAMP = 0x00000000ffffffffL;

    private final List<FloatBuffer> mVertexBuffer = new ArrayList<FloatBuffer>();

    private final List<float[]> mVertices = new ArrayList<float[]>();
    private final List<FloatBuffer> mTextureBuffer = new ArrayList<FloatBuffer>();
    private final List<float[]> mTexture = new ArrayList<float[]>();
    private  int mTotalNumStrips;
    static final int CORDS_PER_VERTEX = 3;
    private int mMVPMatrixHandle;

    private int mPositionHandle;
    private int programHandle;
    private int mTextureCoordinateHandle;
    private int mTextureDataHandle0[] = new int[3];
    private final int vertexStride = CORDS_PER_VERTEX * 4;

    static float triangleCoords[] = {
            -0.0f,  -0.622008459f/2-2, -0.9f-2, // top
            -0.5f, -0.311004243f/2-2, 0.0f-2, // bottom left
            0.5f, -0.311004243f/2-2f, 0.0f-2  // bottom right
    };

    static float triangleCoords2[] = {
            -0.0f,  -0.622008459f/2-2f, -2.85f, // top
            -0.5f, -0.311004243f/2-2f, -1.955f, // bottom left
            0.5f, -0.311004243f/2-2f, -1.95f  // bottom right
    };

    float textureCoords[] = {
            0.5f, 1.0f,
            0.1f, 0.05f,
            0.87f, 0.05f,
            0.9f, 1.0f // t right
    };




    float straight[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    float color[] = { 0.188f, 0.345f, 0.906f, 1.0f };
    private FloatBuffer vertexBuffer;
    private FloatBuffer vertexBuffer1;
    private FloatBuffer colorBuffer;
    private FloatBuffer arrowBuffer;

    Arrow arr;
    float arrowAngle = 0.0f;
    boolean ifEGLPresent = true;

    ArrayList<Float> arrows;

    public Renderer(final Context context, final int depth, final float radius) {

        IntBuffer il = IntBuffer.allocate(1);
        GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, il);
        int limit = il.get();
        String manufacturer = android.os.Build.MANUFACTURER;
        Log.i("device", manufacturer);
        if(manufacturer.equals("HUAWEI") )
         ifEGLPresent = false;

        arrows = new ArrayList<>();


        final String vertexShader = getVertexShader(context);
        final String fragmentShader = getFragmentShader(context);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle,fragmentShaderHandle, new String[]{"a_Position","a_TexCoordinate","a_alpha"});
        GLES20.glUseProgram(programHandle);

        final int d = Math.max(1, Math.min(MAXIMUM_ALLOWED_DEPTH, depth));

        this.mTotalNumStrips = power(2, d - 1) * VERTEX_MAGIC_NUMBER;
        final int numVerticesPerStrip = power(2, d) * 3;
        final double altitudeStepAngle = ONE_TWENTY_DEGREES / power(2, d);
        final double azimuthStepAngle = THREE_SIXTY_DEGREES / this.mTotalNumStrips;
        double x, y, z, h, altitude, azimuth;

        for (int stripNum = 0; stripNum < this.mTotalNumStrips; stripNum++) {
            final float[] vertices = new float[numVerticesPerStrip * NUM_FLOATS_PER_VERTEX];
            final float[] texturePoints = new float[numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE];
            int vertexPos = 0;
            int texturePos = 0;

            altitude = NINETY_DEGREES;
            azimuth = stripNum * azimuthStepAngle;

            for (int vertexNum = 0; vertexNum < numVerticesPerStrip; vertexNum += 2) {
                y = radius * sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * sin(azimuth);
                x = h * Math.cos(azimuth);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                texturePoints[texturePos++] = (float) (1 - azimuth / THREE_SIXTY_DEGREES);
                texturePoints[texturePos++] = (float) (1 - (altitude + NINETY_DEGREES) / ONE_EIGHTY_DEGREES);

                altitude -= altitudeStepAngle;
                azimuth -= azimuthStepAngle / 2.0;
                y = radius * sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * sin(azimuth);
                x = h * Math.cos(azimuth);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                texturePoints[texturePos++] = (float) (1 - azimuth / THREE_SIXTY_DEGREES);
                texturePoints[texturePos++] = (float) (1 - (altitude + NINETY_DEGREES) / ONE_EIGHTY_DEGREES);

                azimuth += azimuthStepAngle;
            }

            this.mVertices.add(vertices);
            this.mTexture.add(texturePoints);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_VERTEX * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            FloatBuffer fb = byteBuffer.asFloatBuffer();
            fb.put(this.mVertices.get(stripNum));
            fb.position(0);
            this.mVertexBuffer.add(fb);

            byteBuffer = ByteBuffer.allocateDirect(numVerticesPerStrip * NUM_FLOATS_PER_TEXTURE * Float.SIZE);
            byteBuffer.order(ByteOrder.nativeOrder());
            fb = byteBuffer.asFloatBuffer();
            fb.put(this.mTexture.get(stripNum));
            fb.position(0);
            this.mTextureBuffer.add(fb);
        }

        int vertexCount = 30;
        float r = 1.0f;
        float center_x = 1.0f;
        float center_y = -2.0f;
        float center_z = 1.0f;
        float buffer[] = new float[vertexCount*3]; // (x,y) for each vertex
        int idx = 0;

        buffer[idx++] = center_x;
        buffer[idx++] = center_y;
        buffer[idx++] = center_z;

// Outer vertices of the circle
        int outerVertexCount = vertexCount-1;

        for (int i = 0; i < outerVertexCount; ++i){
            float percent = (i / (float) (outerVertexCount-1));
            float rad = percent * (float)Math.PI;

            //Vertex position
            float outer_x = (float) (center_x + radius * Math.cos(rad));
            float outer_y = (float) (center_y + radius * sin(rad));
            float outer_z = (float) (center_z + radius * Math.cos(rad));


            buffer[idx++] = outer_x;
            buffer[idx++] = outer_y;
            buffer[idx++] = outer_z;
        }




        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);

        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();

        vertexBuffer.put(triangleCoords);

        vertexBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(triangleCoords2.length * 4);

        bb2.order(ByteOrder.nativeOrder());

        vertexBuffer1 = bb2.asFloatBuffer();

        vertexBuffer1.put(triangleCoords2);

        vertexBuffer1.position(0);




        ByteBuffer bb1 = ByteBuffer.allocateDirect(color.length * 4);

        bb1.order(ByteOrder.nativeOrder());

        colorBuffer = bb1.asFloatBuffer();

        colorBuffer.put(straight);

        colorBuffer.position(0);

        ByteBuffer bb3 = ByteBuffer.allocateDirect(textureCoords.length * 4);

        bb3.order(ByteOrder.nativeOrder());

        arrowBuffer = bb3.asFloatBuffer();

        arrowBuffer.put(textureCoords);

        arrowBuffer.position(0);


    }

    public static String readShader(final Context context, final int resourceId)
    {
        final InputStream is = context.getResources().openRawResource(resourceId);
        final InputStreamReader isr = new InputStreamReader(is);
        final BufferedReader br = new BufferedReader(isr);

        String nl;
        final StringBuilder txt = new StringBuilder();

        try
        {
            while ((nl = br.readLine()) != null)
            {
                txt.append(nl);
                txt.append('\n');
            }
        }
        catch (IOException e)
        {
            return null;
        }

        return txt.toString();
    }

    public String getVertexShader(Context context) {
        return readShader(context, R.raw._vertex_shader);
    }

    public String getFragmentShader(Context context) {
        return readShader(context, R.raw._fragment_shader);
    }

    public void setArrows(ArrayList<Float> ang){
        arrows = new ArrayList<>(ang);
    }

    public void loadTexture(Context c, int rid) {
        mTextureDataHandle0 = getTexture(c, rid);
    }

    public void loadTexture(Context c, byte[] b) {
        final int[] textureHandle = new int[3];
        GLES20.glGenTextures(3, textureHandle, 0);
        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length, options);
            Bitmap resizedbitmap=Bitmap.createBitmap(decodedByte, 0,0, (int) (decodedByte.getWidth()*0.925), decodedByte.getHeight());

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, resizedbitmap, 0);
            decodedByte.recycle();
            resizedbitmap.recycle();

            checkGLError("texture0");
        }

        if (textureHandle[1] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.up_triangle, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            checkGLError("texture1");

        }

        if (textureHandle[2] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.gray, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[2]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            checkGLError("texture2");

        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("error loading texture");
        }

        mTextureDataHandle0 = textureHandle;
    }

    public void deleteCurrentTexture() {
        GLES20.glDeleteTextures(mTextureDataHandle0.length, mTextureDataHandle0, 0);
    }

    public static int[] getTexture(final Context c, final int rid) {
        final int[] textureHandle = new int[3];

        GLES20.glGenTextures(3, textureHandle, 0);
        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), rid, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

            checkGLError("texture0");
        }

        if (textureHandle[1] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.up_triangle, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[1]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            checkGLError("texture1");

        }

        if (textureHandle[2] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.gray, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[2]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);


            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            checkGLError("texture2");

        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("error loading texture");
        }

        return textureHandle;
    }


    public void draw(float[] mvpMatrix) {
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_MVPMatrix");

        mPositionHandle = GLES20.glGetAttribLocation(programHandle,"a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle,"a_TexCoordinate");
        int tl = GLES20.glGetAttribLocation(programHandle,"a_alpha");


        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0[0]);
        GLES20.glUniform1i(mTextureCoordinateHandle, 0);


        for (int i = 0; i < this.mTotalNumStrips; i++) {

            GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer.get(i));

            GLES20.glEnableVertexAttribArray(mPositionHandle);

            GLES20.glVertexAttribPointer(mTextureCoordinateHandle,AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GLES20.GL_FLOAT, false, 0,mTextureBuffer.get(i));

            GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);



            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, this.mVertices.get(i).length / AMOUNT_OF_NUMBERS_PER_VERTEX_POINT);

        }
        /*

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, 0, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);

        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mRotationMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);


        //GLES20.glUniform4fv(tl, 1, color, 0);

        if (ifEGLPresent)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0[1]);
        GLES20.glUniform1i(mTextureCoordinateHandle, 1);

        //GLES20.glColorMask(true,true,true,false);
        GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GLES20.GL_FLOAT, false, 0,arrowBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, -0, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mRotationMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        */


        for(int i=0;i<arrows.size();i++)
            drawArrow(tl, mvpMatrix, arrows.get(i));


        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    public void drawArrow(int tl, float[] mvpMatrix, float angle){

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, angle, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);

        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mRotationMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);


        //GLES20.glUniform4fv(tl, 1, color, 0);
        if (ifEGLPresent)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0[2]);
        GLES20.glUniform1i(mTextureCoordinateHandle, 2);

        GLES20.glVertexAttribPointer(tl,4, GLES20.GL_FLOAT, true,1 , colorBuffer);
        GLES20.glEnableVertexAttribArray(tl);

        GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer1);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GLES20.GL_FLOAT, false, 0,colorBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(tl);

        if (ifEGLPresent)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0[1]);
        GLES20.glUniform1i(mTextureCoordinateHandle, 1);

        //GLES20.glColorMask(true,true,true,false);
        GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GLES20.GL_FLOAT, false, 0,arrowBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, -angle, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, mRotationMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);


    }

    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("glError: ", label + ": glError " + error);
            //throw new RuntimeException(label + ": glError " + error);
        }
    }

    private String byteToString(byte[] arr){
        String s = "";
        for(int i=0;i<arr.length ;i++)
            s += arr[i];
        return s;
    }

    public static int power(final int base, final int raise) {
        int p = 1;
        long b = raise & POWER_CLAMP;

        long powerN = base;

        while (b != 0) {
            if ((b & 1) != 0) {
                p *= powerN;
            }
            b >>>= 1;
            powerN = powerN * powerN;
        }

        return p;
    }


    public int testTouch(float width, float height, float xTouch, float yTouch, float[] mModelView, float[] mProjection, float angle){
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, angle, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);

        Matrix.multiplyMM(mModelView, 0, mModelView, 0, mRotationMatrix, 0);


        Ray r = new Ray((int)width, (int)height, xTouch, yTouch, mModelView, mProjection);

        int coordCount = triangleCoords.length;
        float[] convertedSquare = new float[coordCount];
        float[] resultVector = new float[4];
        float[] inputVector = new float[4];

        for(int i = 0; i < coordCount; i = i + 3){
            inputVector[0] = triangleCoords[i];
            inputVector[1] = triangleCoords[i+1];
            inputVector[2] = triangleCoords[i+2];
            inputVector[3] = 1;
            Matrix.multiplyMV(resultVector, 0, mModelView, 0, inputVector,0);
            convertedSquare[i] = resultVector[0]/resultVector[3];
            convertedSquare[i+1] = resultVector[1]/resultVector[3];
            convertedSquare[i+2] = resultVector[2]/resultVector[3];
        }

        float v0[] = {convertedSquare[0],convertedSquare[1],convertedSquare[2]};
        float v1[] = {convertedSquare[3],convertedSquare[4],convertedSquare[5]};
        float v2[] = {convertedSquare[6],convertedSquare[7],convertedSquare[8]};
        float I[] = new float[3];

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, -0, 0.5f);
        Matrix.rotateM(mRotationMatrix, 0, -angle, 0, 1.0f, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, -0.5f);

        Matrix.multiplyMM(mModelView, 0, mModelView, 0, mRotationMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mModelView, 0);


        return intersectRayAndTriangle(r,v0,v1,v2,I);

    }

    public static int intersectRayAndTriangle(Ray R, float[] v0, float[] v1, float[] v2, float[] I)
    {
        float[] u, v, n;
        float[] dir, w0, w;
        float r, a, b;

        u = Vector.minus(v1, v0);
        v =  Vector.minus(v2, v0);
        n =  Vector.crossProduct(u, v);

        if (Arrays.equals(n, new float[]{0.0f,0.0f,0.0f})){           // triangle is degenerate
            return -1;                 // do not deal with this case
        }
        dir =  Vector.minus(R.P1, R.P0);             // ray direction vector
        w0 = Vector.minus( R.P0 , v0);
        a = - Vector.dot(n,w0);
        b =  Vector.dot(n,dir);
        if (Math.abs(b) <  0.00000001f) {     // ray is parallel to triangle plane
            if (a == 0){                // ray lies in triangle plane
                return 2;
            }else{
                return 0;             // ray disjoint from plane
            }
        }

        // get intersect point of ray with triangle plane
        r = a / b;
        if (r < 0.0f){                   // ray goes away from triangle
            return 0;                  // => no intersect
        }
        // for a segment, also test if (r > 1.0) => no intersect

        float[] tempI =  Vector.addition(R.P0,  Vector.scalarProduct(r, dir));   // intersect point of ray and plane
        I[0] = tempI[0];
        I[1] = tempI[1];
        I[2] = tempI[2];

        // is I inside T?
        float    uu, uv, vv, wu, wv, D;
        uu =  Vector.dot(u,u);
        uv =  Vector.dot(u,v);
        vv =  Vector.dot(v,v);
        w =  Vector.minus(I,v0);
        wu =  Vector.dot(w,u);
        wv = Vector.dot(w,v);
        D = (uv * uv) - (uu * vv);

        // get and test parametric coords
        float s, t;
        s = ((uv * wv) - (vv * wu)) / D;
        if (s < 0.0f || s > 1.0f)        // I is outside T
            return 0;
        t = (uv * wu - uu * wv) / D;
        if (t < 0.0f || (s + t) > 1.0f)  // I is outside T
            return 0;

        return 1;                      // I is in T
    }




    private class Ray {
        float [] P0, P1;

        public Ray(int width, int height, float xTouch, float yTouch, float[] mModelView, float[] mProjection) {
            int[] viewport = {0, 0, width, height};

            float[] nearCoOrds = new float[3];
            float[] farCoOrds = new float[3];
            float[] temp = new float[4];
            float[] temp2 = new float[4];

            float winx = xTouch, winy = (float) viewport[3] - yTouch;

            int result = GLU.gluUnProject(winx, winy, 0.0f, mModelView, 0, mProjection, 0, viewport, 0, temp, 0);

            Matrix.multiplyMV(temp2, 0, mModelView, 0, temp, 0);
            if (result == GL10.GL_TRUE) {
                nearCoOrds[0] = temp2[0] / temp2[3];
                nearCoOrds[1] = temp2[1] / temp2[3];
                nearCoOrds[2] = temp2[2] / temp2[3];
            }

            result = GLU.gluUnProject(winx, winy, 1.0f, mModelView, 0, mProjection, 0, viewport, 0, temp, 0);
            Matrix.multiplyMV(temp2, 0, mModelView, 0, temp, 0);
            if (result == GL10.GL_TRUE) {
                farCoOrds[0] = temp2[0] / temp2[3];
                farCoOrds[1] = temp2[1] / temp2[3];
                farCoOrds[2] = temp2[2] / temp2[3];
            }
            this.P0 = farCoOrds;
            this.P1 = nearCoOrds;
        }
    }

    private boolean checkGL20Support( Context context )
    {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        int EGL_OPENGL_ES2_BIT = 4;
        int[] configAttribs =
                {
                        EGL10.EGL_RED_SIZE, 4,
                        EGL10.EGL_GREEN_SIZE, 4,
                        EGL10.EGL_BLUE_SIZE, 4,
                        EGL10.EGL_SURFACE_TYPE,     EGL10.EGL_WINDOW_BIT,
                        EGL10.EGL_RENDERABLE_TYPE,  EGL_OPENGL_ES2_BIT,
                        EGL10.EGL_NONE
                };

        EGLConfig[] configs = new EGLConfig[10];
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
        //egl.eglTerminate(display);
        return num_config[0] > 0;
    }


}


