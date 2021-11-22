package com.wizardlybump17.wlib.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final char[] ALPHANUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] NUMERIC_CHARS = "0123456789".toCharArray();

    /**
     * Returns if {@link ThreadLocalRandom#nextDouble(double)} is smaller than the given percentage
     * @param percentage the percentage to be checked
     * @return if the percentage was successfully checked
     */
    public static boolean checkPercentage(double percentage) {
        return RANDOM.nextDouble(101) <= percentage;
    }

    /**
     * Generates an alphanumeric string
     * @param length the result size
     * @return the random string
     */
    public static String randomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            builder.append(ALPHANUMERIC_CHARS[RANDOM.nextInt(ALPHANUMERIC_CHARS.length)]);
        return builder.toString();
    }

    /**
     * Generates a random numeric only string
     * @param length the result size
     * @return the random string
     */
    public static String randomNString(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            builder.append(NUMERIC_CHARS[RANDOM.nextInt(NUMERIC_CHARS.length)]);
        return builder.toString();
    }

    /**
     * @param array the array
     * @param <T> the array type
     * @return a random element from the array
     */
    public static <T> T randomElement(T[] array) {
        return array[RANDOM.nextInt(array.length)];
    }
}
