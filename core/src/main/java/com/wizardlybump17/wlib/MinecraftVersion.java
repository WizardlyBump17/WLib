package com.wizardlybump17.wlib;

import lombok.NonNull;
import org.bukkit.Bukkit;

public enum MinecraftVersion {

    V1_16_5,
    V1_17_1,
    V1_18,
    V1_18_2,
    V1_19,
    V1_19_3,
    V1_19_4,
    V1_20_1,
    V1_20_2,
    V1_20_4;

    public static @NonNull MinecraftVersion getVersion() {
        return switch (Bukkit.getServer().getClass().getName().split("\\.")[3]) {
            case "v1_16_R3" -> V1_16_5;
            case "v1_17_R1" -> V1_17_1;
            case "v1_18_R1" -> V1_18;
            case "v1_18_R2" -> V1_18_2;
            case "v1_19_R1" -> V1_19;
            case "v1_19_R2" -> V1_19_3;
            case "v1_19_R3" -> V1_19_4;
            case "v1_20_R1" -> V1_20_1;
            case "v1_20_R2" -> V1_20_2;
            case "v1_20_R3" -> V1_20_4;
            default -> throw new IllegalStateException("Unsupported server version");
        };
    }
}
