package com.wizardlybump17.wlib.list;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static List<String> replace(List<String> list, String old, String replace) {
        if(list == null) return null;
        List<String> result = new ArrayList<>();
        for (String s : list) result.add(s.replace(old, replace));
        return result;
    }
}
