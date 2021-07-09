package com.wizardlybump17.wlib.util;

public class ArrayUtils {

    public static byte[] arrayOfRange(byte from, byte to) {
        byte[] result = new byte[Math.abs(to - from)];
        int index = 0;
        for (byte b = from; b <= to; b++)
            result[index++] = b;
        return result;
    }

    public static short[] arrayOfRange(short from, short to) {
        short[] result = new short[Math.abs(to - from)];
        int index = 0;
        for (short s = from; s <= to; s++)
            result[index++] = s;
        return result;
    }

    public static int[] arrayOfRange(int from, int to) {
        int[] result = new int[Math.abs(to - from) + 1];
        int index = 0;
        for (int i = from - 1; i < to;)
            result[index++] = i++;
        return result;
    }

    public static long[] arrayOfRange(long from, long to) {
        long[] result = new long[(int) Math.abs(to - from)];
        int index = 0;
        for (long l = from; l <= to; l++)
            result[index++] = l;
        return result;
    }
}
