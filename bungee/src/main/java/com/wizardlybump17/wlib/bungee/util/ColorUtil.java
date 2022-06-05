package com.wizardlybump17.wlib.bungee.util;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColorUtil {

    public static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{3})>");

    public static String format(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);

        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find()) {
            String group = matcher.group(1);
            String hex = "<#" + group + ">";
            StringBuilder builder = new StringBuilder("§x");
            for (char c : group.toCharArray())
                builder.append("§").append(c);
            string = string.replaceFirst(hex, builder.toString());
        }

        return string;
    }
}
