package com.wizardlybump17.wlib.adapter.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

public class LegacyStringUtil extends StringUtil {

    @Override
    public Color getColor(String string) {
        return Color.WHITE;
    }

    @Override
    public ChatColor toBungeeColor(Color color) {
        return ChatColor.RESET;
    }
}
