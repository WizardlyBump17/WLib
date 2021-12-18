package com.wizardlybump17.wlib.util;

public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <E extends Enum<E>> String getName(Enum<E> e) {
        String name = e.name().replace('_', ' ');
        String[] s = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : s)
            sb.append(s1.substring(0, 1).toUpperCase()).append(s1.substring(1).toLowerCase()).append(" ");
        return sb.toString().trim();
    }
}
