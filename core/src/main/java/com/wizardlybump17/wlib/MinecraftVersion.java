package com.wizardlybump17.wlib;

import lombok.NonNull;
import org.bukkit.Bukkit;

public enum MinecraftVersion {

    V1_20_5;

    public static @NonNull MinecraftVersion getVersion() {
        return switch (Bukkit.getServer().getClass().getName().split("\\.")[3]) {
            case "v1_20_R4" -> V1_20_5;
            default -> throw new IllegalStateException("Unsupported server version");
        };
    }
}
