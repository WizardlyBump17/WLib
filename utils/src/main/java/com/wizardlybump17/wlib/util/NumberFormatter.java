package com.wizardlybump17.wlib.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//https://gist.github.com/arantesxyz/8788c1a7ac73bb2610426456cf8cdee6
public class NumberFormatter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    public static final NumberFormatter SIMPLE_FORMATTER = new NumberFormatter();

    private final List<String> suffixes;

    public NumberFormatter() {
        suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ");
    }

    public NumberFormatter(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    public String formatNumber(double value) {
        if (suffixes.isEmpty())
            return DECIMAL_FORMAT.format(value);

        boolean negative = value < 0;
        int index = 0;
        value = Math.abs(value);

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            if (index + 1 == suffixes.size())
                break;
            value = tmp;
            ++index;
        }

        return (negative ? "-" : "") +  DECIMAL_FORMAT.format(value) + suffixes.get(index);
    }

    public double parseString(String value) throws NumberFormatException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }

        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.find())
            throw new NumberFormatException("Invalid format");

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);

        int index = suffixes.indexOf(suffix.toUpperCase());

        return amount * Math.pow(1000, index);
    }
}
