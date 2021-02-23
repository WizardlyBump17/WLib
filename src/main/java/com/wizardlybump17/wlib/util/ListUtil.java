package com.wizardlybump17.wlib.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class ListUtil {

    private final List<String> list;

    public ListUtil replace(String old, String replace) {
        if (list == null) return new ListUtil(new ArrayList<>());
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.replace(old, replace));
        return new ListUtil(result);
    }

    public ListUtil replace(char old, char replace) {
        if (list == null) return new ListUtil(new ArrayList<>());
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.replace(old, replace));
        return new ListUtil(result);
    }

    public ListUtil toLowerCase() {
        if (list == null) return new ListUtil(new ArrayList<>());
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.toLowerCase());
        return new ListUtil(result);
    }

    public ListUtil toUpperCase() {
        if (list == null) return new ListUtil(new ArrayList<>());
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.toUpperCase());
        return new ListUtil(result);
    }

    public ListUtil contact(List<String> list) {
        if (this.list == null) return new ListUtil(list);
        List<String> newList = new ArrayList<>(this.list);
        newList.addAll(list);
        return new ListUtil(newList);
    }

    public ListUtil trim() {
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.trim());
        return new ListUtil(result);
    }

    public String join(String joinString) {
        return join(joinString, 0, list.size());
    }

    public String join(String joinString, int startAt, int endAt) {
        StringBuilder result = new StringBuilder();
        for (int i = startAt; i < endAt; i++)
            result.append(list.get(i)).append(joinString);
        return result.substring(0, result.length() - joinString.length());
    }

    @Deprecated
    public static List<String> breakLines(String string, int length) {
        List<String> result = new ArrayList<>();
        int last = 0;
        for (int i = 0; i < string.length(); i += length) {
            if (i != 0) result.add(string.substring(last, i));
            last = i;
        }
        if (string.length() % length != 0) result.add(string.substring(last - length));
        return result;
    }
}
