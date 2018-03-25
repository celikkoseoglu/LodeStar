package com.lodestarapp.cs491.lodestar.VR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLUtils;

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
import java.util.List;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

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
    private int mTextureDataHandle0[] = new int[2];
    private final int vertexStride = CORDS_PER_VERTEX * 4;

    static float triangleCoords[] = {
            -0.0f,  -0.622008459f/2-2, -0.9f-2, // top
            -0.5f, -0.311004243f/2-2, 0.0f-2, // bottom left
            0.5f, -0.311004243f/2-2, 0.0f-2  // bottom right
    };

    static float triangleCoords2[] = {
            -0.0f,  -0.622008459f/2-2, -2.85f, // top
            -0.5f, -0.311004243f/2-2, -1.955f, // bottom left
            0.5f, -0.311004243f/2-2, -1.95f  // bottom right
    };

    float textureCoords[] = {
            0.5f, 1.0f,
            0.15f, 0.05f,
            0.9f, 0.05f,
            0.9f, 1.0f // t right
            };



    float straight[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    float color[] = { 0.188f, 0.345f, 0.906f, 1.0f };
    private FloatBuffer vertexBuffer;
    private FloatBuffer vertexBuffer1;
    private FloatBuffer colorBuffer;
    private FloatBuffer arrowBuffer;

    public Renderer(final Context context, final int depth, final float radius) {

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

    public void loadTexture(Context c, int rid) {
        mTextureDataHandle0 = getTexture(c, rid);
    }

    public void deleteCurrentTexture() {
        GLES20.glDeleteTextures(mTextureDataHandle0.length, mTextureDataHandle0, 0);
    }

    public static int[] getTexture(final Context c, final int rid) {
        final int[] textureHandle = new int[2];

        GLES20.glGenTextures(2, textureHandle, 0);


        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            final Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), rid, options);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
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
        GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer1);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);



        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,AMOUNT_OF_NUMBERS_PER_TEXTURE_POINT, GLES20.GL_FLOAT, false, 0,arrowBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle0[1]);
        GLES20.glUniform1i(mTextureCoordinateHandle, 1);





        //GLES20.glUniform4fv(tl, 1, color, 0);
        GLES20.glVertexAttribPointer(tl,4, GLES20.GL_FLOAT, true,1 , colorBuffer);
        GLES20.glEnableVertexAttribArray(tl);





        //GLES20.glColorMask(true,true,true,false);
        GLES20.glVertexAttribPointer(mPositionHandle, CORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glDisableVertexAttribArray(tl);








        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisable(GLES20.GL_CULL_FACE);




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
















}
