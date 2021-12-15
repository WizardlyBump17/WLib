package com.wizardlybump17.wlib.util;

import java.util.Collection;
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
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random byte from the array
     */
    public static byte randomElement(byte[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random short from the array
     */
    public static short randomElement(short[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random int from the array
     */
    public static int randomElement(int[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random long from the array
     */
    public static long randomElement(long[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random float from the array
     */
    public static float randomElement(float[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random double from the array
     */
    public static double randomElement(double[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random char from the array
     */
    public static char randomElement(char[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param array the array
     * @return a random boolean from the array
     */
    public static boolean randomElement(boolean[] array) {
        if (array.length == 0)
            throw new ArrayIndexOutOfBoundsException("Array is empty");
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * @param collection the collection
     * @param <T> the collection type
     * @return a random element from the collection
     */
    @SuppressWarnings("unchecked")
    public static <T> T randomElement(Collection<T> collection) {
        if (collection.isEmpty())
            throw new ArrayIndexOutOfBoundsException("Collection is empty");
        return (T) randomElement(collection.toArray());
    }

    /**
     * Returns a random int with the given digits size.
     * If {@code zeroAtStart}, the first digit may be 0, which will makes the final result not the same size as the given length
     * @param length the digits size
     * @param zeroAtStart if the int should start with 0
     * @return the random int
     */
    public static int randomInt(int length, boolean zeroAtStart) {
        StringBuilder result = new StringBuilder(randomNString(length));
        while (result.charAt(0) == '0' && !zeroAtStart)
            result.setCharAt(0, randomElement(NUMERIC_CHARS));
        return Integer.parseInt(result.toString());
    }
}
