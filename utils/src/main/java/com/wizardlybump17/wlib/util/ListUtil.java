package com.wizardlybump17.wlib.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Data
public class ListUtil {

    private final List<String> list;

    public ListUtil replace(String old, String replacement) {
        List<String> result = new ArrayList<>(list.size());
        for (String s : list)
            result.add(s.replace(old, replacement));
        return new ListUtil(result);
    }

    public ListUtil replace(char old, char replacement) {
        List<String> result = new ArrayList<>(list.size());
        for (String s : list)
            result.add(s.replace(old, replacement));
        return new ListUtil(result);
    }

    public static List<String> breakLines(String string, int length, String prefix) {
        List<String> result = new ArrayList<>();
        int current = 0;
        while (current < string.length())
            result.add(prefix + string.substring(current, current + length > string.length() ? (current = string.length()) : (current += length)));
        return result;
    }
}
