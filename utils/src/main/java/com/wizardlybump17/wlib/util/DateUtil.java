package com.wizardlybump17.wlib.util;

import lombok.RequiredArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class DateUtil {

    private static final Map<Pattern, TimeUnit> TIME_UNITS = new HashMap<>();

    static {
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(milliseconds|millisecond|ms))"), TimeUnit.MILLISECOND);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(seconds|second|sec|s))"), TimeUnit.SECOND);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(minutes|minute|min|m))"), TimeUnit.MINUTE);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(hours|hour|h))"), TimeUnit.HOUR);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(days|day|d))"), TimeUnit.DAY);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(weeks|week|w))"), TimeUnit.WEEK);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(months|month|mo))"), TimeUnit.MONTH);
        TIME_UNITS.put(Pattern.compile("((\\d+) ?(years|year|y))"), TimeUnit.YEAR);
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final long date;

    public String getDifference(long anotherDate, boolean full) {
        StringBuilder result = new StringBuilder();

        long diff = Math.abs(date - anotherDate);
        long seconds = diff / 1000 % 60;
        long minutes = diff / (60 * 1000) % 60;
        long hours = diff / (60 * 60 * 1000) % 24;
        long days = diff / (24 * 60 * 60 * 1000);

        if (days > 0)
            result.append(days).append(full ? " days, " : "d ");
        if (hours > 0)
            result.append(hours).append(full ? " hours, " : "h ");
        if (minutes > 0)
            result.append(minutes).append(full ? " minutes, " : "m ");
        if (seconds > 0)
            result.append(seconds).append(full ? " seconds" : "s");
        if (result.toString().isEmpty())
            return "now";

        return result.toString().trim().endsWith(",")
                ? result.toString().trim().substring(0, result.length() - 2)
                : result.toString().trim();
    }

    public String format(DateFormat format) {
        switch (format) {
            case DATE:
                return DATE_FORMAT.format(date);
            case FULL:
                return FULL_FORMAT.format(date);
            case TIME:
                return TIME_FORMAT.format(date);
            case DURATION:
                return toDuration();
        }
        return "";
    }

    private String toDuration() {
        return getDifference(0, true);
    }

    public static long toMillis(String string) {
        try {
            return Long.parseLong(string) * 1000L;
        } catch (NumberFormatException ignored) {
        }
        long result = 0;

        for (Map.Entry<Pattern, TimeUnit> entry : TIME_UNITS.entrySet()) {
            final Matcher matcher = entry.getKey().matcher(string);
            if (!matcher.find())
                continue;

            result += Long.parseLong(matcher.group(2)) * entry.getValue().getMillis();
        }

        return result;
    }

    public enum DateFormat {
        DATE, FULL, TIME, DURATION
    }
}
