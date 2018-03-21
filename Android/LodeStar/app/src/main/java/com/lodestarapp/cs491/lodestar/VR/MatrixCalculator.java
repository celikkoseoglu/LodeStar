package com.lodestarapp.cs491.lodestar.VR;

public class MatrixCalculator {

    public static void perspectiveUpdate(float[] x, float deg, float aspect, float n, float f) {

        final float ang = (float) (deg * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan (ang / 2.0));

        x[0] = a / aspect;
        x[1] = 0f;
        x[2] = 0f;
        x[3] = 0f;
        x[4] = 0f;
        x[5] = a;
        x[6] = 0f;
        x[7] = 0f;
        x[8] = 0f;
        x[9] = 0f;
        x[10] = -((f + n) / (f - n));
        x[11] = -1f;
        x[12] = 0f;
        x[13] = 0f;
        x[14] = -((2f * f * n) / (f - n));
        x[15] = 0f;
    }


}
