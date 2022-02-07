package com.wizardlybump17.wlib.adapter.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtil {

    public static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})>");

    public abstract Color getColor(String string);

    public abstract ChatColor toBungeeColor(Color color);

    public String colorize(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);

        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find()) {
            String hex = "<#" + matcher.group(1) + ">";
            Color color = getColor(hex);
            string = string.replaceFirst(hex, toBungeeColor(color).toString());
        }

        return string;
    }
}
