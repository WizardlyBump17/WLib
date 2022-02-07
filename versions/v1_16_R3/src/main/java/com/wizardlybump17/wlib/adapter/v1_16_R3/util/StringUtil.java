package com.wizardlybump17.wlib.adapter.v1_16_R3.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

import java.util.regex.Matcher;

public class StringUtil extends com.wizardlybump17.wlib.adapter.util.StringUtil {

    public Color getColor(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        if (!matcher.matches()) {
            try {
                org.bukkit.ChatColor bukkitColor = org.bukkit.ChatColor.valueOf(string.toUpperCase());
                java.awt.Color color = bukkitColor.asBungee().getColor();
                return Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
            } catch (IllegalArgumentException ignored) {
            }
            return null;
        }

        String hex = "#" + matcher.group(1);
        java.awt.Color javaColor = java.awt.Color.decode(hex);
        return Color.fromRGB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue());
    }

    public ChatColor toBungeeColor(Color color) {
        return ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue()));
    }
}
