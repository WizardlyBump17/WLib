package com.wizardlybump17.wlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DateUtil {

    private final long date;

    public String getDifferenceBetween(long anotherDate) {
        return new DateUtil(Math.abs(date - anotherDate)).toFancyString();
    }

    public String toFancyString() {
        StringBuilder result = new StringBuilder();

        long seconds = date / 1000 % 60;
        long minutes = date / (60 * 1000) % 60;
        long hours = date / (60 * 60 * 1000) % 24;
        long days = date / (24 * 60 * 60 * 1000);

        if (days > 0) result.append(days).append(" days, ");
        if (hours > 0) result.append(hours).append(" hours, ");
        if (minutes > 0) result.append(minutes).append(" minutes, ");
        if (seconds > 0) result.append(seconds).append(" seconds");
        if (result.toString().isEmpty()) result.append("now");

        if (result.toString().trim().endsWith(",")) result.delete(result.length() - 1, result.length());

        return result.toString();
    }
}
