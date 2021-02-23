package com.wizardlybump17.wlib.util;

import java.math.BigInteger;
import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static BigInteger nextBigInteger(BigInteger bound) {
        BigInteger result = new BigInteger(bound.bitLength(), RANDOM);
        while (result.compareTo(bound) >= 0) result = new BigInteger(bound.bitLength(), RANDOM);
        return result;
    }

    public static BigInteger nextBigInteger(BigInteger bi1, BigInteger bi2) {
        if (bi1.compareTo(bi2) == 0) return bi1;
        BigInteger min = min(bi1, bi2);
        return nextBigInteger(max(bi1, bi2).subtract(min)).add(min);
    }

    private static BigInteger min(BigInteger bi1, BigInteger bi2) {
        return bi1.compareTo(bi2) < 0 ? bi1 : bi2;
    }

    private static BigInteger max(BigInteger bi1, BigInteger bi2) {
        return bi1.compareTo(bi2) > 0 ? bi1 : bi2;
    }

    public static boolean checkPercentage(double percentage) {
        return 100 * RANDOM.nextDouble() <= percentage;
    }
}
