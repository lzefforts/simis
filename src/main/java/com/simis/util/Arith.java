package com.simis.util;

import java.math.BigDecimal;

/**
 * Created by 一拳超人
 */
public class Arith {
    private static final int DEF_DIV_SCALE = 10;

    private Arith() {
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        if(v1 == null) {
            v1 = BigDecimal.valueOf(0L);
        }

        if(v2 == null) {
            v2 = BigDecimal.valueOf(0L);
        }

        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2);
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2);
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2);
    }

    public static double div(double v1, double v2) {
        return div(v1, v2, 10);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, 10);
    }

    public static double div(double v1, double v2, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            return b1.divide(b2, scale, 4).doubleValue();
        }
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b1 = new BigDecimal(v1.toString());
            BigDecimal b2 = new BigDecimal(v2.toString());
            return b1.divide(b2, scale, 4);
        }
    }

    public static double round(double v, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(Double.toString(v));
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4).doubleValue();
        }
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(v.toString());
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 4);
        }
    }

    public static BigDecimal roundUp(BigDecimal v, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(v.toString());
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 0);
        }
    }

    public static BigDecimal roundDown(BigDecimal v, int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } else {
            BigDecimal b = new BigDecimal(v.toString());
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, 1);
        }
    }

    public static double arithAdd(Double v1, Double v2) {
        if(v1 == null) {
            v1 = Double.valueOf(0.0D);
        }

        if(v2 == null) {
            v2 = Double.valueOf(0.0D);
        }

        return add(v1.doubleValue(), v2.doubleValue());
    }
}
