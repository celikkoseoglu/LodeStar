package com.lodestarapp.cs491.lodestar.VR;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.List;

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
    private int mTextureDataHandle0[] = new int[1];
    private final int vertexStride = CORDS_PER_VERTEX * 4;


    public Renderer(final Context context, final int depth, final float radius) {

        final String vertexShader = getVertexShader(context);
        final String fragmentShader = getFragmentShader(context);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        programHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle,fragmentShaderHandle, new String[]{"a_Position","a_TexCoordinate"});
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
                y = radius * Math.sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * Math.sin(azimuth);
                x = h * Math.cos(azimuth);
                vertices[vertexPos++] = (float) x;
                vertices[vertexPos++] = (float) y;
                vertices[vertexPos++] = (float) z;

                texturePoints[texturePos++] = (float) (1 - azimuth / THREE_SIXTY_DEGREES);
                texturePoints[texturePos++] = (float) (1 - (altitude + NINETY_DEGREES) / ONE_EIGHTY_DEGREES);

                altitude -= altitudeStepAngle;
                azimuth -= azimuthStepAngle / 2.0;
                y = radius * Math.sin(altitude);
                h = radius * Math.cos(altitude);
                z = h * Math.sin(azimuth);
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
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

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

        if (textureHandle[0] == 0) {
            throw new RuntimeException("error loading texture");
        }

        return textureHandle;
    }


    public void draw(float[] mvpMatrix) {
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_MVPMatrix");

        mPositionHandle = GLES20.glGetAttribLocation(programHandle,"a_Position");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle,"a_TexCoordinate");

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
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
