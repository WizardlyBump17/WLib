package com.wizardlybump17.wlib.util;

@Deprecated
public class PercentageUtil {

    public static boolean check(double percentage) {
        return Math.random() * 100 <= percentage;
    }
}
