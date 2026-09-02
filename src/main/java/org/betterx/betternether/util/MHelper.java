package org.betterx.betternether.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import java.util.Random;

public class MHelper {
    public static final float PI = (float) Math.PI;
    public static final float PI2 = (float) (Math.PI * 2);

    public static int randRange(int min, int max, RandomSource random) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        return min + random.nextInt(max - min + 1);
    }

    public static float randRange(float min, float max, RandomSource random) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            float t = min;
            min = max;
            max = t;
        }
        return min + random.nextFloat() * (max - min);
    }

    public static int randRange(int min, int max, Random random) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }
        return min + random.nextInt(max - min + 1);
    }

    public static float randRange(float min, float max, Random random) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            float t = min;
            min = max;
            max = t;
        }
        return min + random.nextFloat() * (max - min);
    }

    public static double lengthSqr(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    public static double length(double x, double y, double z) {
        return Math.sqrt(lengthSqr(x, y, z));
    }

    public static float wrap(float value, float side) {
        float res = value % side;
        return res < 0 ? res + side : res;
    }

    public static double wrap(double value, double side) {
        double res = value % side;
        return res < 0 ? res + side : res;
    }

    public static int floor(double x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public static float min(float a, float b, float c) {
        return Math.min(a, Math.min(b, c));
    }

    public static float max(float a, float b, float c) {
        return Math.max(a, Math.max(b, c));
    }
}
