package com.wizardlybump17.wlib;

import lombok.NonNull;
import org.bukkit.Bukkit;

public enum MinecraftVersion {

    V1_20_6,
    V1_21,
    UNKNOWN;

    public static @NonNull MinecraftVersion getVersion() {
        return switch (Bukkit.getMinecraftVersion()) {
            case "1.20.6" -> V1_20_6;
            case "1.21" -> V1_21;
            default -> UNKNOWN;
        };
    }
}
