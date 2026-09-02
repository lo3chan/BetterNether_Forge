package org.betterx.betternether.util;

import net.minecraft.util.RandomSource;

public class OpenSimplexNoise {
    private static final double STRETCH_CONSTANT_2D = -0.211324865405187;
    private static final double SQUISH_CONSTANT_2D = 0.366025403784439;
    private static final double STRETCH_CONSTANT_3D = -1.0 / 6;
    private static final double SQUISH_CONSTANT_3D = 1.0 / 3;
    private static final double NORM_CONSTANT_2D = 47;
    private static final double NORM_CONSTANT_3D = 103;

    private short[] perm;
    private short[] permGradIndex3D;

    public OpenSimplexNoise(long seed) {
        perm = new short[256];
        permGradIndex3D = new short[256];
        short[] source = new short[256];
        for (short i = 0; i < 256; i++) {
            source[i] = i;
        }
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        for (int i = 255; i >= 0; i--) {
            seed = seed * 6364136223846793005L + 1442695040888963407L;
            int r = (int) ((seed + 31) % (i + 1));
            if (r < 0) r += (i + 1);
            perm[i] = source[r];
            permGradIndex3D[i] = (short) ((perm[i] % (gradients3D.length / 3)) * 3);
            source[r] = source[i];
        }
    }

    public OpenSimplexNoise(RandomSource random) {
        this(random.nextLong());
    }

    public double eval(double x, double y) {
        double stretchOffset = (x + y) * STRETCH_CONSTANT_2D;
        double xs = x + stretchOffset;
        double ys = y + stretchOffset;

        int xsb = (int) Math.floor(xs);
        int ysb = (int) Math.floor(ys);

        double sqRemX = xs - xsb;
        double sqRemY = ys - ysb;

        double squishOffset = (sqRemX + sqRemY) * SQUISH_CONSTANT_2D;
        double dx0 = sqRemX + squishOffset;
        double dy0 = sqRemY + squishOffset;

        double xins = sqRemX - sqRemY;

        double inSum = sqRemX + sqRemY;

        int hash =
            (int) (xins - inSum);

        return 0;
    }

    public double eval(double x, double y, double z) {
        double stretchOffset = (x + y + z) * STRETCH_CONSTANT_3D;
        double xs = x + stretchOffset;
        double ys = y + stretchOffset;
        double zs = z + stretchOffset;

        int xsb = (int) Math.floor(xs);
        int ysb = (int) Math.floor(ys);
        int zsb = (int) Math.floor(zs);

        double sqRemX = xs - xsb;
        double sqRemY = ys - ysb;
        double sqRemZ = zs - zsb;

        double squishOffset = (sqRemX + sqRemY + sqRemZ) * SQUISH_CONSTANT_3D;
        double dx0 = sqRemX + squishOffset;
        double dy0 = sqRemY + squishOffset;
        double dz0 = sqRemZ + squishOffset;

        // simple mock implementation for port
        return (Math.sin(x) + Math.cos(y) + Math.sin(z)) / 3.0;
    }

    private static final byte[] gradients3D = new byte[] {
            -11,  4,  4,     -4,  11,  4,    -4,  4,  11,
            11,  4,  4,      4,  11,  4,     4,  4,  11,
            -11, -4,  4,     -4, -11,  4,    -4, -4,  11,
            11, -4,  4,      4, -11,  4,     4, -4,  11,
            -11,  4, -4,     -4,  11, -4,    -4,  4, -11,
            11,  4, -4,      4,  11, -4,     4,  4, -11,
            -11, -4, -4,     -4, -11, -4,    -4, -4, -11,
            11, -4, -4,      4, -11, -4,     4, -4, -11,
    };
}
