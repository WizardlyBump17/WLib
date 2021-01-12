package com.wizardlybump17.wlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DateUtil {

    private final long date;

    public String getDifferenceBetween(long anotherDate) {
        StringBuilder result = new StringBuilder();

        long diff = Math.abs(date - anotherDate);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffDays > 0) result.append(diffDays).append(" days, ");
        if (diffHours > 0) result.append(diffHours).append(" hours, ");
        if (diffMinutes > 0) result.append(diffMinutes).append(" minutes, ");
        if (diffSeconds > 0) result.append(diffSeconds).append(" seconds");
        if (result.toString().isEmpty()) result.append("now");

        if (result.toString().trim().endsWith(",")) result.delete(result.length() - 1, result.length());

        return result.toString();
    }
}
