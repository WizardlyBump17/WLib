package com.wizardlybump17.wlib.util;

import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
public class DateUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final long date;

    public String getDifference(long anotherDate) {
        StringBuilder result = new StringBuilder();

        long diff = Math.abs(date - anotherDate);
        long seconds = diff / 1000 % 60;
        long minutes = diff / (60 * 1000) % 60;
        long hours = diff / (60 * 60 * 1000) % 24;
        long days = diff / (24 * 60 * 60 * 1000);

        if (days > 0) result.append(days).append(" days, ");
        if (hours > 0) result.append(hours).append(" hours, ");
        if (minutes > 0) result.append(minutes).append(" minutes, ");
        if (seconds > 0) result.append(seconds).append(" seconds");
        if (result.toString().isEmpty()) return "now";

        return result.toString().endsWith(", ") ? result.substring(0, result.length() - 2) : result.toString();
    }

    public String format(DateFormat format) {
        switch (format) {
            case DATE:
                return DATE_FORMAT.format(date);
            case FULL:
                return FULL_FORMAT.format(date);
            case TIME:
                return TIME_FORMAT.format(format);
            case DURATION:
                return toDuration();
        }
        return "";
    }

    private String toDuration() {
        return getDifference(0);
    }

    public enum DateFormat {
        DATE, FULL, TIME, DURATION
    }
}
