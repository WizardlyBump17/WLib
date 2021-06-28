package com.wizardlybump17.wlib.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final char[] ALPHA_NUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static boolean checkPercentage(double percentage) {
        return RANDOM.nextDouble(101) <= percentage;
    }

    public static String randomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            builder.append(ALPHA_NUMERIC_CHARS[RANDOM.nextInt(ALPHA_NUMERIC_CHARS.length)]);
        return builder.toString();
    }
}
