package com.wizardlybump17.wlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final Map<Pattern, Long> TIME_UNITS = new HashMap<>();

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

    public static final Pattern SPECIFIC_DATE = Pattern.compile("(\\d+)/(\\d+)/(\\d+)");
    public static final Pattern SPECIFIC_TIME = Pattern.compile("(\\d+):(\\d+):(\\d+)(:(\\d+))?");
    public static final Pattern SPECIFIC_DATE_TIME = Pattern.compile("((\\d+)/(\\d+)/(\\d+)) ((\\d+):(\\d+):(\\d+)(:(\\d+))?)");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    /**
     * Returns a Date object from a specific date or time.
     * @param date the date or time as String
     * @return the Date object or null if the date or time is invalid
     */
    public static Date fromDate(String date) {
        Date result = fromSpecificDate(date);
        if (result != null)
            return result;

        result = fromSpecificTime(date);
        if (result != null)
            return result;

        result = fromSpecificDateTime(date);
        return result;
    }

    private static Date fromSpecificDate(String date) {
        Matcher matcher = SPECIFIC_DATE.matcher(date);
        if (!matcher.matches())
            return null;

        int day = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int year = Integer.parseInt(matcher.group(3));

        if (day < 1 || day > 31)
            return null;
        if (month < 1 || month > 12)
            return null;
        if (year < 1)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    private static Date fromSpecificTime(String time) {
        Matcher matcher = SPECIFIC_TIME.matcher(time);
        if (!matcher.matches())
            return null;

        int hour = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));
        int second = Integer.parseInt(matcher.group(3));
        int millisecond;
        try {
            millisecond = Integer.parseInt(matcher.group(5));
        } catch (IndexOutOfBoundsException ignored) {
            millisecond = 0;
        }

        if (hour < 0 || hour > 23)
            return null;
        if (minute < 0 || minute > 59)
            return null;
        if (second < 0 || second > 59)
            return null;
        if (millisecond < 0 || millisecond > 999)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);

        return calendar.getTime();
    }

    private static Date fromSpecificDateTime(String dateTime) {
        Matcher matcher = SPECIFIC_DATE_TIME.matcher(dateTime);
        if (!matcher.matches())
            return null;

        Date date = fromSpecificDate(matcher.group(1));
        Date time = fromSpecificTime(matcher.group(5));

        if (date == null || time == null)
            return null;

        return new Date(date.getTime() + time.getTime());
    }

    /**
     * @param date1 the first date
     * @param date2 the second date
     * @param fullName if the full name of the time unit should be used
     * @return the difference between the two dates in a beautiful String
     */
    public static String getDifference(long date1, long date2, boolean fullName) {
        long diff = Math.abs(date1 - date2);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        StringBuilder sb = new StringBuilder();
        if (diffDays > 0)
            sb.append(diffDays).append(" ").append(fullName ? "days" : "d").append(" ");
        if (diffHours > 0)
            sb.append(diffHours).append(" ").append(fullName ? "hours" : "h").append(" ");
        if (diffMinutes > 0)
            sb.append(diffMinutes).append(" ").append(fullName ? "minutes" : "m").append(" ");
        if (diffSeconds > 0)
            sb.append(diffSeconds).append(" ").append(fullName ? "seconds" : "s").append(" ");

        return sb.toString();
    }

    /**
     * @param date the date
     * @param fullName if the full name of the time unit should be used
     * @return the difference between the current date and the given date in a beautiful String
     */
    public static String getDifference(long date, boolean fullName) {
        return getDifference(date, System.currentTimeMillis(), fullName);
    }

    public static String format(long date, FormatType type) {
        switch (type) {
            case DATE:
                return DATE_FORMAT.format(date);
            case TIME:
                return TIME_FORMAT.format(date);
            case FULL:
                return FULL_FORMAT.format(date);
            default:
                return null;
        }
    }

    public static Long toMillis(String string) {
        try {
            return Long.parseLong(string) * 1000L;
        } catch (NumberFormatException ignored) {
        }
        long result = 0;

        for (Map.Entry<Pattern, Long> entry : TIME_UNITS.entrySet()) {
            final Matcher matcher = entry.getKey().matcher(string);
            if (!matcher.find())
                continue;

            result += Long.parseLong(matcher.group(2)) * entry.getValue();
        }

        return result == 0 ? null : result;
    }

    public enum FormatType {
        DATE, FULL, TIME
    }
}
