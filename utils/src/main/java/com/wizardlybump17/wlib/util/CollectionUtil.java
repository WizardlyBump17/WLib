package com.wizardlybump17.wlib.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CollectionUtil<E> {

    @Getter
    private final Collection<E> collection;

    public CollectionUtil<String> replace(String old, String replacement) {
        List<String> result = new ArrayList<>(collection.size());
        for (E e : collection)
            result.add(e.toString().replace(old, replacement));
        return new CollectionUtil<String>(result);
    }

    public CollectionUtil<String> replace(char old, char replacement) {
        List<String> result = new ArrayList<>(collection.size());
        for (E e : collection)
            result.add(e.toString().replace(old, replacement));
        return new CollectionUtil<String>(result);
    }

    public E getIf(Predicate<E> predicate) {
        for (E e : collection)
            if (predicate.test(e))
                return e;
        return null;
    }

    public static List<String> breakLines(String string, int length, String prefix) {
        List<String> result = new ArrayList<>();
        int current = 0;
        while (current < string.length())
            result.add(prefix + string.substring(current, current + length > string.length() ? (current = string.length()) : (current += length)));
        return result;
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[]{};

    public static List<String> breakLines(String string, int length, String prefix, boolean spaces) {
        if (spaces) {
            List<String> result = new ArrayList<>();

            final String[] split = string.split(" ");

            List<String> tempList = new ArrayList<>(split.length + 1);
            for (String s : split) {
                tempList.add(s);

                final String join = String.join(" ", tempList.toArray(EMPTY_STRING_ARRAY));
                if (join.length() >= length) {
                    result.add(prefix + join);
                    tempList.clear();
                }
            }

            return result;
        }

        return breakLines(string, length, prefix);
    }

    public static List<String> breakLines(String string, int length, String prefix, boolean spaces, String... firstPrefixes) {
        if (!spaces)
            return breakLines(string, length, prefix, firstPrefixes);

        final List<String> strings = breakLines(string, length, prefix, true);

        for (int i = 0; i < firstPrefixes.length && i < strings.size(); i++)
            strings.set(i, firstPrefixes[i] + strings.get(i).substring(prefix.length()));

        return strings;
    }

    public static List<String> breakLines(String string, int length, String prefix, String... firstPrefixes) {
        List<String> result = new ArrayList<>(string.length() / length + 1);
        int current = 0;
        while (current < string.length())
            result.add(prefix + string.substring(current, current + length > string.length() ? (current = string.length()) : (current += length)));

        for (int i = 0; i < firstPrefixes.length && i < result.size(); i++)
            result.set(i, firstPrefixes[i] + result.get(i).substring(prefix.length()));

        return result;
    }
}
